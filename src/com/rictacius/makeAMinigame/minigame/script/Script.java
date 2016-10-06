package com.rictacius.makeAMinigame.minigame.script;

import java.io.File;
import java.util.List;

import com.rictacius.makeAMinigame.minigame.script.operation.Operation;

public class Script {
	private File file;
	private String name;

	public static enum Section {
		LOAD, RUN, ARENA_START, ARENA_FINISH, END, LISTENER;
		
		public static enum EVENT {
			ARENA_CREATED;
		}
	}

	public Script(String name, File file) {
		this.name = name;
		this.file = file;
	}

	public void run(List<ScriptLine> lines) {
		for (ScriptLine line : lines) {
			Operation operation = line.parse();
			operation.run();
		}
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
