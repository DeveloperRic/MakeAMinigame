package com.rictacius.makeAMinigame.minigame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Location;

import com.rictacius.makeAMinigame.data.MPlayer;
import com.rictacius.makeAMinigame.util.Utils;

public class Team {
	private Arena arena;
	private String id, name;
	private List<Location> spawns = new ArrayList<Location>();
	private List<MPlayer> players = new ArrayList<MPlayer>();
	private int lastSpawn = -1, kills;
	private HashMap<MPlayer, Integer> pkills = new HashMap<MPlayer, Integer>();

	public Team(Arena arena, String id) {
		this.arena = arena;
		this.id = id;
		this.name = ChatColor.translateAlternateColorCodes('&', arena.getConfig().getString("teams." + id + "name"));
		this.spawns = Utils.stringListToLocaionList(arena.getConfig().getStringList("teams." + id + "spawns"));
	}

	public String getId() {
		return id;
	}

	public List<Location> getSpawns() {
		return spawns;
	}

	public Location getSpawn() {
		if (arena.getBooleanSetting("random-spawns")) {
			Random random = new Random();
			return spawns.get(random.nextInt(spawns.size()));
		} else {
			lastSpawn++;
			return spawns.get(lastSpawn);
		}
	}

	public String getName() {
		return name;
	}

	public List<MPlayer> getPlayers() {
		return players;
	}

	public void addPlayer(MPlayer e) {
		players.add(e);
	}

	public int getKills() {
		return kills;
	}

	public int getKills(MPlayer player) {
		return (pkills.get(player) != null) ? pkills.get(player) : 0;
	}

	public void setKills(int kills) {
		this.kills = kills;
	}

	public void setKills(int kills, MPlayer player) {
		int dif = getKills(player) - kills;
		pkills.put(player, kills);
		setKills(getKills() - dif);
	}

}
