package com.rictacius.makeAMinigame.minigame.script;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.rictacius.makeAMinigame.util.Log;

public class ScriptUtils {

	public ScriptUtils() {
	}

	public static boolean checkPointers(String input) {
		boolean valid = true;
		return valid;
	}

	public static String removeKeys(String input) {
		input = input.replaceAll(";", "");
		return input;
	}

	public static boolean isFunction(String input, Script script) {
		FileConfiguration config = getConfig(script);
		List<String> section = config.getStringList(input);
		if (section.isEmpty())
			return false;
		String end = section.get(section.size() - 1);
		if (end.startsWith("return")) {
			return true;
		} else {
			return false;
		}
	}

	public static FileConfiguration getConfig(Script script) {
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
		return config;
	}

}
