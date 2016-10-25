package com.rictacius.makeAMinigame.event;

import com.rictacius.makeAMinigame.minigame.Arena;

public class ArenaCreatedEvent extends MEvent {
	private Arena arena;

	public ArenaCreatedEvent(Arena arena) {
		super(MEvent.EventType.ARENA_CREATED);
		this.arena = arena;
		deliver(this);
	}

	public Arena getArena() {
		return arena;
	}

}
