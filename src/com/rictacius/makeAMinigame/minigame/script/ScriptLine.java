package com.rictacius.makeAMinigame.minigame.script;

import com.rictacius.makeAMinigame.minigame.script.operation.Operation;

public class ScriptLine {
	private String line;
	private Script.Section section;
	private Script.Section.EVENT event;
	private boolean isevent;

	public ScriptLine(String line, Script.Section section) {
		this.line = line;
	}

	public ScriptLine(String line, Script.Section section, Script.Section.EVENT event) {
		this.line = line;
		this.isevent = true;
		this.event = event;
	}

	public String getLine() {
		return line;
	}

	public Script.Section getSection() {
		return section;
	}

	public Operation parse() {
		// TODO ScriptLine Psrsing
		return null;
	}

	public Script.Section.EVENT getEvent() {
		return event;
	}

	public boolean isEvent() {
		return isevent;
	}

}
