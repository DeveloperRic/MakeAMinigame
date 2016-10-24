package com.rictacius.makeAMinigame.data;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

public class MPlayer {
	private Player player;
	private HashMap<String, Object> settings = new HashMap<String, Object>();
	private PlayerInventory savedInventory;

	public MPlayer(Player player) {
		this.player = player;
		player.chat("hi");
	}

	public void message(String message) {
		player.sendMessage(message);
	}

	public void saveInventory() {
		savedInventory = player.getInventory();
	}

	public void loadSavedInventory() {
		player.getInventory().clear();
		for (int i = 0; i < savedInventory.getSize(); i++) {
			player.getInventory().setItem(i, savedInventory.getItem(i));
		}
	}

	public Player player() {
		return player;
	}

	public Object getSetting(String key) {
		Object setting = settings.get(key);
		return setting;
	}

	public void addSetting(String key, Object value) {
		settings.put(key, value);
	}

	public void removeSetting(String key) {
		if (settings.containsKey(key)) {
			settings.remove(key);
		}
	}

}
