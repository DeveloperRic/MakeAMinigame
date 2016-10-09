package com.rictacius.makeAMinigame.util.exception;

public class ScriptException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ScriptException() {
	}

	public ScriptException(String arg0) {
		super(arg0);
	}

	public ScriptException(Throwable arg0) {
		super(arg0);
	}

	public ScriptException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public ScriptException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

}
