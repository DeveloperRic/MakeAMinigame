package com.rictacius.makeAMinigame.event;

import com.rictacius.makeAMinigame.data.MPlayer;
import com.rictacius.makeAMinigame.minigame.Arena;

public class PlayerJoinEvent extends MEvent {
	private MPlayer player;
	private Arena arena;

	public PlayerJoinEvent(MPlayer player, Arena arena) {
		super(EventType.PLAYER_JOIN_ARENA);
		this.player = player;
		this.arena = arena;
	}

	/**
	 * @return the player
	 */
	public MPlayer getPlayer() {
		return player;
	}

	/**
	 * @return the arena
	 */
	public Arena getArena() {
		return arena;
	}

}
