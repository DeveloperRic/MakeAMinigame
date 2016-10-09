package com.rictacius.makeAMinigame.minigame.script;

import java.util.HashMap;

import com.rictacius.makeAMinigame.data.MPlayer;
import com.rictacius.makeAMinigame.minigame.script.operation.ErrorOperation;
import com.rictacius.makeAMinigame.minigame.script.operation.MPlayerOperation;
import com.rictacius.makeAMinigame.minigame.script.operation.MathOperation;
import com.rictacius.makeAMinigame.minigame.script.operation.NullOperation;
import com.rictacius.makeAMinigame.minigame.script.operation.Operation;
import com.rictacius.makeAMinigame.minigame.script.operation.ReturnOperation;
import com.rictacius.makeAMinigame.minigame.script.operation.SetOperation;
import com.rictacius.makeAMinigame.util.Log;

public class ScriptLine {
	private String line;
	private String raw;
	private Script.Section section;
	private Script.Section.EVENT event;
	private boolean isevent;
	private int number;
	private Script script;

	public ScriptLine(String line, Script.Section section, int number, Script script) {
		this.line = line.trim();
		this.raw = line;
		this.number = number;
		this.script = script;
	}

	public ScriptLine(String line, Script.Section section, Script.Section.EVENT event, int number, Script script) {
		this.line = line.trim();
		this.raw = line;
		this.number = number;
		this.script = script;
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
		if (!validate()) {
			return new ErrorOperation(raw, "ScriptLine " + number + " (" + line + ") is invalid!", this,
					Log.Level.FATAL);
		}
		line = ScriptUtils.removeKeys(line);
		try {
			if (line.startsWith("var")) {
				line = line.substring(3).trim();
				String name = line.replaceAll(" ", "");
				boolean set = script.addVariable(name);
				if (!set) {
					return new ErrorOperation(raw, "Variable " + name + " already exisits in " + script.getName() + "!",
							this, Log.Level.FATAL);
				}
				return new NullOperation(raw);
			} else if (line.startsWith("set")) {
				line = line.substring(3).trim();
				String[] data = line.split("=");
				String left = data[0].trim();
				String right = data[1].trim();
				return new SetOperation(raw, left, right, script, this);
			} else if (line.startsWith("return")) {
				line = line.substring(6).trim();
				return new ReturnOperation(raw, line, script);
			} else if (line.startsWith("math")) {
				line = line.substring(4).trim();
				return new MathOperation(raw, "", script, line);
			} else if (line.startsWith("mplayer")) {
				line = line.substring(7).trim();
				String command = line.split(" ")[0];
				if (command.equals("create")) {
					return new MPlayerOperation.Create(raw, line.split(" ")[1], script);
				} else if (command.equals("message")) {
					String[] data = line.split(" ");
					String parg = data[1];
					MPlayer player = (MPlayer) script.getVariable(parg);
					int length = data[0].length() + data[1].length() + 2;
					String message = line.substring(length);
					HashMap<String, Object> vars = script.getVariables();
					for (String key : vars.keySet()) {
						Object value = vars.get(key);
						message = message.replaceAll("%" + key + "%", String.valueOf(value));
					}
					return new MPlayerOperation.Message(raw, player, message);
				}
			} else if (line.startsWith("player")) {
			}
		} catch (Exception e) {
			return new ErrorOperation(raw, "ScriptLine " + number + " (" + raw + ") is invalid!", this, Log.Level.FATAL,
					e);
		}
		return null;
	}

	public boolean validate() {
		boolean valid;
		valid = ScriptUtils.checkPointers(line);
		return valid;
	}

	public Script.Section.EVENT getEvent() {
		return event;
	}

	public boolean isEvent() {
		return isevent;
	}

	public int getLineNumber() {
		return number;
	}

	public Script getScript() {
		return script;
	}

}
