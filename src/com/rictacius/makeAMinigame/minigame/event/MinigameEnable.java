package com.rictacius.makeAMinigame.minigame.event;

import com.rictacius.makeAMinigame.minigame.Minigame;

public abstract class MinigameEnable extends Event {
	@SuppressWarnings("unused")
	private Minigame minigame;

	public MinigameEnable(Type type, Minigame minigame) {
		super(type);
		
	}

}
