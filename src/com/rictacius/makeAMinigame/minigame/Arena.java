package com.rictacius.makeAMinigame.minigame;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;

import com.rictacius.makeAMinigame.data.MPlayer;
import com.rictacius.makeAMinigame.script.Script;
import com.rictacius.makeAMinigame.script.ScriptManager;

public class Arena {
	private Minigame minigame;
	private World world;
	private Location spawn;
	private Location lobby;
	private int max;
	private int min;
	private List<MPlayer> players = new ArrayList<MPlayer>();
	private Script script;
	private boolean running;

	public Arena(Minigame minigame, World world, Location spawn, Location lobby, int min, int max, Script script) {
		this.minigame = minigame;
		this.world = world;
		this.spawn = spawn;
		this.lobby = lobby;
		this.min = min;
		this.max = max;
		this.script = script;
		script.run(ScriptManager.readScript(script, Script.Section.LISTENER, Script.Section.EVENT.ARENA_CREATED));
	}

	public void start() {
		script.run(ScriptManager.readScript(script, Script.Section.ARENA_START));
	}

	public Minigame getMinigame() {
		return minigame;
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

}
