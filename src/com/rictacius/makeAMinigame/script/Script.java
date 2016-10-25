package com.rictacius.makeAMinigame.script;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.rictacius.makeAMinigame.event.MEvent;
import com.rictacius.makeAMinigame.script.operation.Operation;
import com.rictacius.makeAMinigame.script.operation.ReturnOperation;
import com.rictacius.makeAMinigame.util.Log;

public class Script {
	private File file;
	private String name;
	private HashMap<String, Object> variables = new HashMap<String, Object>();
	private HashMap<String, Object> tempvariables = new HashMap<String, Object>();
	private static Handler handler;
	public static final String nul = "_UNDEFINED_";

	public static enum Section {
		LOAD, RUN, END, EVENT, FUNCTION, PROCEDURE
	}

	public Script(String name, File file) {
		this.name = name;
		this.file = file;
		handler = new Handler(this, ScriptManager.generateHandlerTypes(this));
		Log.log(getClass(), "Created new Script " + name + " for file " + file.getPath(), Log.Level.INFO);
	}

	public void run(List<ScriptLine> lines) {
		Log.log(getClass(), "Running ScriptLines " + file.getPath(), Log.Level.INFO);
		Section prevsec = null;
		for (ScriptLine line : lines) {
			if (prevsec != null) {
				if (!prevsec.equals(line.getSection())) {
					Log.log(getClass(), " != " + line.getSection().toString() + " =! ", Log.Level.INFO);
				}
			}
			Operation operation = line.parse();
			operation.run();
		}
	}

	public class Handler {
		Script script;
		List<MEvent.EventType> handlertypes;
		List<MEvent> events = new ArrayList<MEvent>();

		public Handler(Script script, List<MEvent.EventType> handlertypes) {
			this.script = script;
			this.handlertypes = handlertypes;
		}

		public void call(MEvent e) {
			List<ScriptLine> lines = ScriptManager.readScript(script, Script.Section.EVENT, e.getType());
			events.add(e);
			script.run(lines);
		}

		public boolean accepts(MEvent.EventType type) {
			return handlertypes.contains(type);
		}

		public void closeEvent(MEvent.EventType type) {
			for (int i = events.size() - 1; i >= 0; i--) {
				MEvent event = events.get(i);
				if (event.getType().equals(type)) {
					events.remove(i);
				}
			}
		}
	}

	public Handler getHandler() {
		return handler;
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

	public Set<String> getVariables() {
		return variables.keySet();
	}

	public boolean addTempVariable(String key) {
		if (tempvariables.containsKey(key)) {
			return false;
		}
		tempvariables.put(key, nul);
		return true;
	}

	public boolean setTempVariable(String key, Object value) {
		if (!tempvariables.containsKey(key)) {
			return false;
		}
		tempvariables.put(key, value);
		return true;
	}

	public Object getTempVariable(String key) {
		Object var = tempvariables.get(key);
		tempvariables.remove(key);
		return var;
	}

	public Set<String> getTempVariables() {
		return tempvariables.keySet();
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
