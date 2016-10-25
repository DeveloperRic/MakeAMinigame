package com.rictacius.makeAMinigame.minigame;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.rictacius.makeAMinigame.Main;
import com.rictacius.makeAMinigame.util.Log;
import com.rictacius.makeAMinigame.util.config.ConfigUtil;

public class MinigameManager {
	private static HashMap<String, Minigame> minigames = new HashMap<String, Minigame>();

	public MinigameManager() {
	}

	public static Minigame getMinigame(String id) {
		return minigames.get(id);
	}

	public static List<Minigame> getMinigameInstances() {
		List<Minigame> minigames = new ArrayList<Minigame>();
		for (Minigame minigame : MinigameManager.minigames.values()) {
			minigames.add(minigame);
		}
		return minigames;
	}

	public static void addMinigame(Minigame minigame) {
		minigames.put(minigame.getId(), minigame);
	}

	public static void loadMinigames() {
	}

	public static File getFile(String id) {
		File file = new File(Main.pl.getDataFolder(), "minigame/" + id + ".yml");
		if (!file.exists()) {
			file = ConfigUtil.outputClassFile(MinigameManager.class, "minigame.yml", "minigame/" + id + ".yml");
		}
		return file;
	}

	public static FileConfiguration getConfig(String id) {
		File file = getFile(id);
		FileConfiguration config = new YamlConfiguration();
		try {
			config.load(file);
		} catch (FileNotFoundException e) {
			Log.log(MinigameManager.class, "Could not find Minigame file!", Log.Level.FATAL, e);
			return null;
		} catch (IOException e) {
			Log.log(MinigameManager.class, "Could not load Minigame file!", Log.Level.FATAL, e);
			return null;
		} catch (InvalidConfigurationException e) {
			Log.log(MinigameManager.class, "Could not lod Minigame file! (Invalid Configuration) ", Log.Level.FATAL, e);
			return null;
		}
		return config;
	}

	public static boolean saveMinigame(File file, FileConfiguration config) {
		try {
			config.save(file);
		} catch (IOException e) {
			Log.log(MinigameManager.class, "Could not save Minigame file!", Log.Level.FATAL, e);
			return false;
		}
		return true;
	}

	public static void createMinigame(String id) {
		File file = getFile(id);
		FileConfiguration config = getConfig(id);
		saveMinigame(file, config);
	}

}
