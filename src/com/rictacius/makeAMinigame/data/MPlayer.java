package com.rictacius.makeAMinigame.data;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class MPlayer {
	private Player player;
	private HashMap<String, Object> settings = new HashMap<String, Object>();

	public MPlayer(Player player) {
		this.player = player;
	}

	/**
	 * @return the player
	 */
	public Player getPlayer() {
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

	public void message(String message) {
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
	}

}
