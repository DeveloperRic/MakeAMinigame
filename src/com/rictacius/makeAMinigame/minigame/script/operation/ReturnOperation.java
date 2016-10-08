package com.rictacius.makeAMinigame.minigame.script.operation;

import com.rictacius.makeAMinigame.minigame.script.Script;

public class ReturnOperation extends Operation {
	protected String variable;
	protected Script script;

	public ReturnOperation(String raw, String variable, Script script) {
		super(raw);
		this.variable = variable;
		this.script = script;
	}

	@Override
	public void run() {
		extract();
	}

	public Object extract() {
		return script.getVariable(variable);
	}

	public String getVariable() {
		return variable;
	}

}
