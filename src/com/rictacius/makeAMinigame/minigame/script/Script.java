package com.rictacius.makeAMinigame.minigame.script;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import com.rictacius.makeAMinigame.minigame.script.operation.Operation;
import com.rictacius.makeAMinigame.minigame.script.operation.ReturnOperation;
import com.rictacius.makeAMinigame.util.Log;

public class Script {
	private File file;
	private String name;
	private HashMap<String, Object> variables = new HashMap<String, Object>();
	public static final String nul = "_UNDEFINED_";

	public static enum Section {
		LOAD, RUN, ARENA_START, ARENA_FINISH, END, LISTENER, FUNCTION, PROCEDURE;

		public static enum EVENT {
			ARENA_CREATED;
		}
	}

	public Script(String name, File file) {
		this.name = name;
		this.file = file;
		Log.log(getClass(), "Created new Script " + name + " for file " + file.getPath(), Log.Level.INFO);
	}

	public void run(List<ScriptLine> lines) {
		Log.log(getClass(), "Running Script " + file.getPath(), Log.Level.INFO);
		for (ScriptLine line : lines) {
			Operation operation = line.parse();
			operation.run();
		}
	}

	public Object function(List<ScriptLine> lines) {
		for (ScriptLine line : lines) {
			Operation operation = line.parse();
			if (operation instanceof ReturnOperation) {
				ReturnOperation returnOp = (ReturnOperation) operation;
				return returnOp;
			}
		}
		return null;
	}

	public boolean addVariable(String key) {
		if (variables.containsKey(key)) {
			return false;
		}
		variables.put(key, nul);
		return true;
	}

	public boolean setVariable(String key, Object value) {
		if (!variables.containsKey(key)) {
			return false;
		}
		variables.put(key, value);
		return true;
	}

	public Object getVariable(String key) {
		return variables.get(key);
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, Object> getVariables() {
		return (HashMap<String, Object>) variables.clone();
	}

	public boolean end() {
		run(ScriptManager.readScript(this, Section.END));
		return true;
	}

	public File getFile() {
		return file;
	}

	public String getName() {
		return name;
	}

}
