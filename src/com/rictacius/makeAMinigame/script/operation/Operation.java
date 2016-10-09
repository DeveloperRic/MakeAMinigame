package com.rictacius.makeAMinigame.script.operation;

public abstract class Operation {
	protected String raw;

	public Operation(String raw) {
		this.raw = raw;
	}

	public String getRawOperation() {
		return raw;
	}

	public abstract void run();

}
