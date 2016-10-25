package com.rictacius.makeAMinigame.event;

import com.rictacius.makeAMinigame.minigame.Arena;

public class ArenaStartingEvent extends MEvent {
	private Arena arena;

	public ArenaStartingEvent(Arena arena) {
		super(MEvent.EventType.ARENA_STARTING);
		this.arena = arena;
	}

	public Arena getArena() {
		return arena;
	}

}
