package com.rictacius.makeAMinigame.minigame;

import java.util.ArrayList;
import java.util.List;

import com.rictacius.makeAMinigame.script.Script;
import com.rictacius.makeAMinigame.toggle.Toggle;

public class Minigame {
	private List<Toggle> toggles = new ArrayList<Toggle>();
	private List<Arena> arenas = new ArrayList<Arena>();
	private String id;
	private String name;
	private Script script;

	public Minigame(String id, String name, Script script) {
		this.id = id;
		this.script = script;
		this.name = name;
	}

	public Arena createArena() {
		Arena arena = new Arena(this);
		addArena(arena);
		return arena;
	}

	public void addArena(Arena arena) {
		arenas.add(arena);
	}

	public List<Toggle> getToggles() {
		return toggles;
	}

	public void setToggles(List<Toggle> toggles) {
		this.toggles = toggles;
	}

	public String getId() {
		return id;
	}

	public Script getScript() {
		return script;
	}

	public void setScript(Script script) {
		this.script = script;
	}

	public String getName() {
		return name;
	}

}
