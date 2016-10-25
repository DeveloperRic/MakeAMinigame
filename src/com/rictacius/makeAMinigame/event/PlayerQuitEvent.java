package com.rictacius.makeAMinigame.event;

import com.rictacius.makeAMinigame.data.MPlayer;
import com.rictacius.makeAMinigame.minigame.Arena;

public class PlayerQuitEvent extends MEvent {
	private MPlayer player;
	private boolean forced;
	private Arena arena;

	public PlayerQuitEvent(Arena arena, MPlayer player, boolean forced) {
		super(MEvent.EventType.PLAYER_QUIT_ARENA);
		this.arena = arena;
		this.player = player;
		this.forced = forced;
		deliver(this);
	}

	public Arena getArena() {
		return arena;
	}

	public MPlayer getPlayer() {
		return player;
	}

	public boolean isForced() {
		return forced;
	}

}
