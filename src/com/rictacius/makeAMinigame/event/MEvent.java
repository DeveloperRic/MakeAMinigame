package com.rictacius.makeAMinigame.event;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.rictacius.makeAMinigame.minigame.Minigame;
import com.rictacius.makeAMinigame.minigame.MinigameManager;
import com.rictacius.makeAMinigame.script.Script;
import com.rictacius.makeAMinigame.script.Script.Handler;

public abstract class MEvent {

	public static enum EventType {
		ARENA_CREATED, ARENA_START, ARENA_FINISH, ARENA_STARTING, ARENA_START_TIME, PLAYER_JOIN_ARENA, PLAYER_QUIT_ARENA;
	}

	protected MEvent.EventType type;
	protected UUID eventID = generateID();

	public MEvent(MEvent.EventType type) {
		this.type = type;
	}

	public static void deliver(MEvent event) {
		for (Minigame minigame : MinigameManager.getMinigameInstances()) {
			Script script = minigame.getScript();
			Handler handler = script.getHandler();
			if (handler.accepts(event.getType())) {
				handler.call(event);
			}
		}
	}

	private static List<MEvent> events = new ArrayList<MEvent>();

	private static UUID generateID() {
		UUID id = null;
		int tries = 0;
		while (tries <= 10) {
			boolean unique = true;
			id = UUID.randomUUID();
			for (MEvent event : events) {
				if (event.getID() != null) {
					if (event.getID().equals(id)) {
						unique = false;
					}
				}
			}
			if (unique) {
				break;
			} else {
				tries++;
			}
		}
		return id;
	}

	public UUID getID() {
		return eventID;
	}

	public MEvent.EventType getType() {
		return type;
	}

}
