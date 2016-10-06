package com.rictacius.makeAMinigame.minigame.script.operation;

public abstract class Operation {
	private String raw;

	public Operation(String raw) {
		this.raw = raw;
	}

	public String getRawOperation() {
		return raw;
	}

	public abstract void run();

}
