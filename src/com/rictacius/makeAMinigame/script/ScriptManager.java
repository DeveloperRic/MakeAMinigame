package com.rictacius.makeAMinigame.script;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;

import com.rictacius.makeAMinigame.util.Log;

public class ScriptManager {
	private static HashMap<String, Script> scripts = new HashMap<String, Script>();

	public ScriptManager() {
	}

	public static List<Script> getScripts() {
		List<Script> send = new ArrayList<Script>();
		send.addAll(scripts.values());
		return send;
	}

	public static Script getScript(String name) {
		return scripts.get(name);
	}

	public static void addScript(String name, Script script) {
		scripts.put(name, script);
		script.run(readScript(script, Script.Section.LOAD));
	}

	public static boolean removeScript(String name) {
		Script script = scripts.get(name);
		if (script != null) {
			boolean ended = script.end();
			if (ended) {
				if (scripts.containsKey(name)) {
					scripts.remove(name);
				}
			}
			return ended;
		}
		return false;
	}

	public static List<ScriptLine> readScript(Script script, Script.Section section) {
		Log.log(ScriptManager.class,
				"Reading Script (Section=" + section.toString() + ") " + script.getFile().getPath(), Log.Level.INFO);
		List<ScriptLine> send = new ArrayList<ScriptLine>();
		FileConfiguration config = ScriptUtils.getConfig(script);
		List<String> lines = config.getStringList(section.toString());
		for (int i = 0; i < lines.size(); i++) {
			String line = lines.get(i);
			ScriptLine sline = new ScriptLine(line, section, i, script);
			send.add(sline);
		}
		return send;
	}

	public static List<ScriptLine> readScript(Script script, Script.Section section, Script.Section.EVENT event) {
		if (!section.equals(Script.Section.LISTENER)) {
			return readScript(script, section);
		}
		Log.log(ScriptManager.class, "Reading Script (Section=" + section.toString() + ", Event=" + event.toString()
				+ ") " + script.getFile().getPath(), Log.Level.INFO);
		List<ScriptLine> send = new ArrayList<ScriptLine>();
		FileConfiguration config = ScriptUtils.getConfig(script);
		List<String> lines = config.getStringList(section.toString() + "." + event.toString());
		for (int i = 0; i < lines.size(); i++) {
			String line = lines.get(i);
			ScriptLine sline = new ScriptLine(line, section, event, 1, script);
			send.add(sline);
		}
		return send;
	}

	public static List<ScriptLine> readScript(Script script, Script.Section section, String name) {
		if (!section.equals(Script.Section.FUNCTION) && !section.equals(Script.Section.PROCEDURE)) {
			return readScript(script, section);
		}
		Log.log(ScriptManager.class,
				"Reading Script (Section=" + section.toString() + ", SUB=" + name + ") " + script.getFile().getPath(),
				Log.Level.INFO);
		List<ScriptLine> send = new ArrayList<ScriptLine>();
		FileConfiguration config = ScriptUtils.getConfig(script);
		List<String> lines = config.getStringList(name);
		for (int i = 0; i < lines.size(); i++) {
			String line = lines.get(i);
			ScriptLine sline = new ScriptLine(line, section, i, script);
			send.add(sline);
		}
		return send;
	}

}
