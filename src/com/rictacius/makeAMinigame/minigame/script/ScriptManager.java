package com.rictacius.makeAMinigame.minigame.script;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

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
		List<ScriptLine> send = new ArrayList<ScriptLine>();
		File file = script.getFile();
		FileConfiguration config = new YamlConfiguration();
		try {
			config.load(file);
		} catch (FileNotFoundException e) {
			Log.log(ScriptManager.class,
					"Could not read script {" + script.getName() + "} because the script's file could not be found!",
					Log.Level.FATAL, e);
			e.printStackTrace();
		} catch (IOException e) {
			Log.log(ScriptManager.class, "Could not read script {" + script.getName() + "}!", Log.Level.FATAL, e);
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			Log.log(ScriptManager.class, "Could not read script {" + script.getName()
					+ "} because the script's file was configured incorrectly (YAML)!", Log.Level.FATAL, e);
			e.printStackTrace();
		}
		List<String> lines = config.getStringList(section.toString());
		for (String line : lines) {
			ScriptLine sline = new ScriptLine(line, section);
			send.add(sline);
		}
		return send;
	}

	public static List<ScriptLine> readScript(Script script, Script.Section section, Script.Section.EVENT event) {
		if (!section.equals(Script.Section.LISTENER)) {
			return readScript(script, section);
		}
		List<ScriptLine> send = new ArrayList<ScriptLine>();
		File file = script.getFile();
		FileConfiguration config = new YamlConfiguration();
		try {
			config.load(file);
		} catch (FileNotFoundException e) {
			Log.log(ScriptManager.class,
					"Could not read script {" + script.getName() + "} because the script's file could not be found!",
					Log.Level.FATAL, e);
			e.printStackTrace();
		} catch (IOException e) {
			Log.log(ScriptManager.class, "Could not read script {" + script.getName() + "}!", Log.Level.FATAL, e);
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			Log.log(ScriptManager.class, "Could not read script {" + script.getName()
					+ "} because the script's file was configured incorrectly (YAML)!", Log.Level.FATAL, e);
			e.printStackTrace();
		}
		List<String> lines = config.getStringList(section.toString() + "." + event.toString());
		for (String line : lines) {
			ScriptLine sline = new ScriptLine(line, section, event);
			send.add(sline);
		}
		return send;
	}

}
