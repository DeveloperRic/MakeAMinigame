package com.rictacius.makeAMinigame.event;

import com.rictacius.makeAMinigame.minigame.Arena;

public class ArenaStartEvent extends MEvent {
	private Arena arena;

	public ArenaStartEvent(Arena arena) {
		super(MEvent.EventType.ARENA_START);
		this.arena = arena;
		deliver(this);
	}
	
	public Arena getArena() {
		return arena;
	}

}
