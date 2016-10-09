package com.rictacius.makeAMinigame.script.operation;

import com.rictacius.makeAMinigame.script.ScriptLine;
import com.rictacius.makeAMinigame.util.Log;
import com.rictacius.makeAMinigame.util.exception.ErrorHandler;

public class ErrorOperation extends Operation {
	private String message;
	private ScriptLine line;
	private Log.Level lvl;
	private Exception e;

	public ErrorOperation(String raw, String message, ScriptLine line, Log.Level lvl) {
		super(raw);
		this.message = message;
		this.line = line;
		this.lvl = lvl;
	}

	public ErrorOperation(String raw, String message, ScriptLine line, Log.Level lvl, Exception e) {
		super(raw);
		this.message = message;
		this.line = line;
		this.lvl = lvl;
		this.e = e;
	}

	@Override
	public void run() {
		if (e != null) {
			ErrorHandler.throwScriptError(getClass(), message, lvl, e);
		} else {
			ErrorHandler.throwScriptError(getClass(), message, lvl);
		}
	}

	public ScriptLine getScriptLine() {
		return line;
	}

}
