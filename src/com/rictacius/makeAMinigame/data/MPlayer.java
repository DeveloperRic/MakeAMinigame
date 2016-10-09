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

	public <T> Object getSetting(Class<T> root, String key) {
		Object setting = settings.get(key);
		T cast = root.cast(setting);
		return cast;
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
