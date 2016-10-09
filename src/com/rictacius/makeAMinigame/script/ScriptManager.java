package com.rictacius.makeAMinigame.script;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;

import com.rictacius.makeAMinigame.script.operation.ErrorOperation;
import com.rictacius.makeAMinigame.script.operation.Operation;
import com.rictacius.makeAMinigame.script.operation.RunOperation;
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

	public static List<ScriptLine> readScript(Script script, Script.Section section, Script.Section.EventType event) {
		if (!section.equals(Script.Section.EVENT)) {
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

	public static void compile(List<ScriptLine> lines) {
		if (lines.isEmpty())
			return;
		for (ScriptLine line : lines) {
			Script script = line.getScript();
			Operation op = line.parse();
			Log.log(ScriptManager.class, "Compiling ScriptLine (Script=" + script.getFile().getPath() + ") ",
					Log.Level.INFO);
			if (op instanceof ErrorOperation) {
				ErrorOperation eop = (ErrorOperation) op;
				Exception error = eop.getException();
				Log.Level lvl = eop.getLevel();
				if (error != null) {
					Log.log(ScriptManager.class,
							"Found error " + error.getClass().getSimpleName() + " message = " + eop.getMessage(), lvl);
				} else {
					Log.log(ScriptManager.class, "Found error {unknown} Message = " + eop.getMessage(), lvl);
				}
				eop.run();
			} else if (op instanceof RunOperation) {
				RunOperation rop = (RunOperation) op;
				compile(rop.getLines());
			}
		}
	}

	public static List<String> getFunctions(Script script) {
		List<String> send = new ArrayList<String>();
		FileConfiguration config = ScriptUtils.getConfig(script);
		Map<String, Object> values = config.getValues(false);
		for (String key : values.keySet()) {
			if (ScriptUtils.isFunction(key, script)) {
				send.add(key);
			}
		}
		return send;
	}

}
