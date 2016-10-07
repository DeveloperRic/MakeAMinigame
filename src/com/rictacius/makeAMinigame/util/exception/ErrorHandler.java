package com.rictacius.makeAMinigame.util.exception;

import com.rictacius.makeAMinigame.util.Log;

public class ErrorHandler {

	public ErrorHandler() {
	}

	public static void throwScriptError(Class<?> source, String message, Log.Level lvl) {
		try {
			throw new ScriptException("[" + source.getSimpleName() + "] " + message);
		} catch (Exception e) {
			Log.log(source, message, lvl, e);
		}
	}

	public static void throwScriptError(Class<?> source, String message, Log.Level lvl, Exception error) {
		try {
			throw new ScriptException("[" + source.getSimpleName() + "] " + message);
		} catch (Exception e) {
			Log.log(source, message, lvl, error);
			Log.log(source, message, lvl, e);
		}
	}

}
