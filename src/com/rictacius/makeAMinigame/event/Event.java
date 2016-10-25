package com.rictacius.makeAMinigame.event;

public abstract class Event {
	protected Type type;

	public static enum Type {

	}

	public Event(Type type) {
		this.type = type;
	}
	
	public abstract void onArrival(Object... args);
	
	public abstract void 

}
