package com.rictacius.makeAMinigame.minigame.event;

import org.bukkit.entity.Player;

import com.rictacius.makeAMinigame.minigame.Arena;

public class PlayerJoin extends Event {
	private Player player;
	private Arena arena;

	public PlayerJoin(Type cvalue, Player player, Arena arena) {
		super(cvalue);
		setPlayer(player);
		setArena(arena);
		onPlayerJoin();
	}

	public void onPlayerJoin() {
		
	}

	/**
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * @param player
	 *            the player to set
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}

	/**
	 * @return the arena
	 */
	public Arena getArena() {
		return arena;
	}

	/**
	 * @param arena
	 *            the arena to set
	 */
	public void setArena(Arena arena) {
		this.arena = arena;
	}

}
