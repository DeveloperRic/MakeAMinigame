package com.rictacius.makeAMinigame.minigame;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import com.rictacius.makeAMinigame.Main;
import com.rictacius.makeAMinigame.data.MPlayer;
import com.rictacius.makeAMinigame.script.Script;
import com.rictacius.makeAMinigame.script.Script.Section.EventType;
import com.rictacius.makeAMinigame.script.ScriptManager;
import com.rictacius.makeAMinigame.util.PermCheck;
import com.rictacius.makeAMinigame.util.Utils;

public class Arena implements Listener {
	private Minigame minigame;
	private World world;
	private Location spawn;
	private Location lobby;
	private int min, max, startTimer, startTime, timeToEnd, killsToEnd, endTimer, duration;
	private List<MPlayer> players = new ArrayList<MPlayer>();
	private List<Team> teams = new ArrayList<Team>();
	private boolean running, ingame, starting, endsWithKills, endsWithTime;
	private Script script;
	private FileConfiguration config;

	public Arena(Minigame minigame) {
		this.minigame = minigame;
		config = MinigameManager.getConfig(minigame.getId());
		this.world = Bukkit.getWorld(getStringSetting("world"));
		this.spawn = getLocationSetting("spawn");
		this.lobby = getLocationSetting("lobby");
		this.min = getIntegerSetting("minPlayers");
		this.max = getIntegerSetting("maxPlayers");
		this.script = minigame.getScript();
		endsWithKills = getBooleanSetting("game-ends-with-kills");
		killsToEnd = getIntegerSetting("kills-to-end");
		endsWithTime = getBooleanSetting("game-ends-with-time");
		timeToEnd = getIntegerSetting("time-to-end");
		for (String id : config.getConfigurationSection("teams").getKeys(false)) {
			Team team = new Team(this, id);
			teams.add(team);
		}
		runScriptEvent(EventType.ARENA_CREATED);
	}

	public void begin() {
		runScriptEvent(EventType.ARENA_START);
		teleport(spawn);
		for (Team team : teams) {
			for (MPlayer player : players) {
				player.player().teleport(team.getSpawn());
			}
		}
		if (endsWithTime) {

		}
		broadcast(getMessage("game-started"));
	}

	public void join(MPlayer player) {
		if (getBooleanSetting("reset-inv-onleave")) {
			player.saveInventory();
		}
		if (getBooleanSetting("clear-inv-onjoin")) {
			player.player().getInventory().clear();
		}
		player.player().teleport(lobby);
		player.message(getMessage("player-join"));
		int smallestTeam = 0;
		if (teams.size() > 1) {
			for (int i = 0; i < teams.size(); i++) {
				if (teams.get(i).getPlayers().size() <= teams.get(i + 1).getPlayers().size()) {
					smallestTeam = i;
				}
			}
		}
		teams.get(smallestTeam).addPlayer(player);
		player.message(getMessage("join-team").replaceAll("%team%", teams.get(smallestTeam).getName()));
		players.add(player);
		if (players.size() >= min) {
			if (!starting) {
				runScriptEvent(EventType.ARENA_STARTING);
				beginStartTimer();
			}
		}
	}

	public void beginStartTimer() {
		startTimer = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.pl, new Runnable() {
			@Override
			public void run() {
				if (startTime <= 0) {
					cancelStartTimer();
					begin();
				} else {
					runScriptEvent(EventType.ARENA_START_TIME);
					if (startTime % getIntegerSetting("start-timer-message-interval") == 0 || startTime >= 5) {
						broadcast(getMessage("start-timer"));
					}
				}
			}
		}, 0L, 20L);
	}

	public void cancelStartTimer() {
		Bukkit.getScheduler().cancelTask(startTimer);
	}

	public void checkKillCount() {
		if (!endsWithKills)
			return;
		for (int i = 0; i < teams.size(); i++) {
			Team team = teams.get(i);
			if (team.getKills() >= killsToEnd) {
				finishGame(team);
				break;
			}
		}
	}

	public void startEndTimer() {
		endTimer = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.pl, new Runnable() {
			@Override
			public void run() {
				if (duration >= timeToEnd) {
					cancelEndTimer();
					Team winner = null;
					for (int i = 0; i < teams.size(); i++) {
						Team team = teams.get(i);
						if (team.getKills() >= killsToEnd) {
							winner = team;
							break;
						}
					}
					finishGame(winner);
				} else {
					int interval = getIntegerSetting("end-timer-message-interval");
					if (duration % interval == 0 || (timeToEnd - duration) >= 5) {
						broadcast(getMessage("end-timer"));
					}
					duration--;
				}
			}
		}, 0, 20);
	}

	public void cancelEndTimer() {
		Bukkit.getScheduler().cancelTask(endTimer);
	}

	public void finishGame(Team winner) {
		List<String> killTable = drawKillWinnerTable(winner);
		for (String item : killTable) {
			broadcast(item);
		}
		end();
	}

	public List<String> drawKillWinnerTable(Team winner) {
		String name = "Nobody";
		if (winner != null) {
			name = winner.getName();
		}
		MPlayer oneKiller = null;
		MPlayer twoKiller = null;
		MPlayer threeKiller = null;
		for (MPlayer player : players) {
			if (threeKiller != null) {
				if (getTeam(player).getKills(player) > getTeam(threeKiller).getKills(threeKiller)) {
					if (twoKiller != null) {
						if (getTeam(player).getKills(player) > getTeam(twoKiller).getKills(twoKiller)) {
							if (oneKiller != null) {
								if (getTeam(player).getKills(player) > getTeam(oneKiller).getKills(oneKiller)) {
									threeKiller = twoKiller;
									twoKiller = oneKiller;
									oneKiller = player;
								} else {
									threeKiller = twoKiller;
									twoKiller = player;
								}
							} else {
								oneKiller = player;
							}
						} else {
							threeKiller = player;
						}
					} else {
						twoKiller = player;
					}
				}
			} else {
				threeKiller = player;
			}
		}
		List<String> send = new ArrayList<String>();
		List<String> lines = getMessageList("finish-kill-table");
		for (String line : lines) {
			line = line.replaceAll("%team%", name);
			line = line.replaceAll("%1kill%", oneKiller.player().getName());
			line = line.replaceAll("%1count%", "" + getTeam(oneKiller).getKills(oneKiller));
			line = line.replaceAll("%2kill%", twoKiller.player().getName());
			line = line.replaceAll("%2count%", "" + getTeam(twoKiller).getKills(twoKiller));
			line = line.replaceAll("%3kill%", threeKiller.player().getName());
			line = line.replaceAll("%3count%", "" + getTeam(threeKiller).getKills(threeKiller));
			send.add(line);
		}
		return send;
	}

	public void end() {
		runScriptEvent(EventType.ARENA_FINISH);
		players.clear();
		running = false;
		if (getBooleanSetting("restart-onend")) {
			long delay = Long.parseLong(getStringSetting("restart-delay"));
			Bukkit.getScheduler().scheduleSyncDelayedTask(Main.pl, new Runnable() {
				@Override
				public void run() {
					begin();
				}
			}, delay);
		}
	}

	@EventHandler
	public void onBuild(BlockPlaceEvent event) {
		World world = event.getBlock().getWorld();
		if (world.getName().equals(this.world.getName())) {
			Player player = event.getPlayer();
			String bypass = getStringSetting("bypass-build-permission");
			if (!player.isOp()) {
				if (!PermCheck.hasAccess(player, bypass)) {
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void onDestroy(BlockBreakEvent event) {
		World world = event.getBlock().getWorld();
		if (world.getName().equals(this.world.getName())) {
			Player player = event.getPlayer();
			String bypass = getStringSetting("bypass-build-permission");
			if (!player.isOp()) {
				if (!PermCheck.hasAccess(player, bypass)) {
					event.setCancelled(true);
				}
			}
		}
	}

	public Minigame getMinigame() {
		return minigame;
	}

	public FileConfiguration getConfig() {
		return config;
	}

	public World getWorld() {
		return world;
	}

	public Location getSpawnLocation() {
		return spawn;
	}

	public Location getLobbyLocation() {
		return lobby;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public int getMin() {
		return min;
	}

	public void setMin(int min) {
		this.min = min;
	}

	public List<MPlayer> getPlayers() {
		return players;
	}

	public Script getScript() {
		return script;
	}

	public boolean isRunning() {
		return running;
	}

	public boolean isIngame() {
		return ingame;
	}

	public boolean isStarting() {
		return starting;
	}

	public boolean getBooleanSetting(String setting) {
		return Boolean.parseBoolean(config.getString("settings." + setting));
	}

	public String getStringSetting(String setting) {
		return config.getString("settings." + setting);
	}

	public int getIntegerSetting(String setting) {
		return Integer.parseInt(config.getString("settings." + setting));
	}

	public Location getLocationSetting(String setting) {
		return Utils.stringToLocation(config.getString("settings." + setting));
	}

	public String getMessage(String message) {
		return ChatColor.translateAlternateColorCodes('&', config.getString("messages." + message));
	}

	public List<String> getMessageList(String message) {
		List<String> send = new ArrayList<String>();
		List<String> lines = config.getStringList("messages." + message);
		for (String line : lines) {
			send.add(ChatColor.translateAlternateColorCodes('&', line));
		}
		return send;
	}

	public void runScriptEvent(Script.Section.EventType type) {
		script.run(ScriptManager.readScript(script, Script.Section.EVENT, type));
	}

	public void broadcast(String message) {
		for (MPlayer player : players) {
			player.message(message);
		}
	}

	public void teleport(Location loc) {
		for (MPlayer player : players) {
			player.player().teleport(loc);
		}
	}

	public Team getTeam(MPlayer player) {
		for (Team team : teams) {
			if (team.getPlayers().contains(player)) {
				return team;
			}
		}
		return null;
	}

}
