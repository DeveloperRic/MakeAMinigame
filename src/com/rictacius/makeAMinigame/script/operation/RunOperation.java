package com.rictacius.makeAMinigame.script.operation;

import java.util.List;

import com.rictacius.makeAMinigame.script.Script;
import com.rictacius.makeAMinigame.script.ScriptLine;

public class RunOperation extends Operation {
	private Script script;
	private List<ScriptLine> lines;

	public RunOperation(String raw, Script script, List<ScriptLine> lines) {
		super(raw);
		this.script = script;
		this.lines = lines;
	}

	@Override
	public void run() {
		script.run(lines);
	}

	public Script getScript() {
		return script;
	}

	public List<ScriptLine> getLines() {
		return lines;
	}

}
