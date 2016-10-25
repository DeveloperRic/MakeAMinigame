package com.rictacius.makeAMinigame.script;

import java.io.File;
import java.util.HashMap;

import org.bukkit.Bukkit;

import com.rictacius.makeAMinigame.Main;
import com.rictacius.makeAMinigame.data.MPlayer;
import com.rictacius.makeAMinigame.script.Script.Section;
import com.rictacius.makeAMinigame.script.operation.ErrorOperation;
import com.rictacius.makeAMinigame.script.operation.MPlayerOperation;
import com.rictacius.makeAMinigame.script.operation.MathOperation;
import com.rictacius.makeAMinigame.script.operation.NullOperation;
import com.rictacius.makeAMinigame.script.operation.Operation;
import com.rictacius.makeAMinigame.script.operation.ReturnOperation;
import com.rictacius.makeAMinigame.script.operation.RunOperation;
import com.rictacius.makeAMinigame.script.operation.SetOperation;
import com.rictacius.makeAMinigame.script.operation.TempSetOperation;
import com.rictacius.makeAMinigame.util.Log;

public class ScriptLine {
	private String line;
	private String raw;
	private Script.Section section;
	private Script.Section.EventType event;
	private boolean isevent;
	private int number;
	private Script script;

	public ScriptLine(String line, Script.Section section, int number, Script script) {
		this.line = line.trim();
		this.raw = line;
		this.number = number;
		this.script = script;
	}

	public ScriptLine(String line, Script.Section section, Script.Section.EventType event, int number, Script script) {
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
			} else if (line.startsWith("tempset")) {
				line = line.substring(3).trim();
				String[] data = line.split("=");
				String left = data[0].trim();
				String right = data[1].trim();
				return new TempSetOperation(raw, left, right, script, this);
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
				} else if (command.equals("reflect")) {
					String[] data = line.split(" ");
					MPlayer mplayer = null;
					if (script.getVariable(data[1]) != null) {
						mplayer = (MPlayer) script.getVariable(data[1]);
					} else {
						mplayer = new MPlayer(Bukkit.getPlayer(data[1]));
					}
					String method = data[2];
					if (data.length >= 4) {
						String rawa = line.substring(line.indexOf('['), line.indexOf(']') + 1);
						if (rawa.contains(",")) {
							String[] rargs = rawa.split(",");
							Object[] args = new Object[rargs.length];
							for (int i = 0; i < rargs.length; i++) {
								String arg = rargs[i];
								if (script.getVariable(arg) != null) {
									args[i] = script.getVariable(arg);
								} else if (arg.contains("\"") && arg.contains("\"")) {
									args[i] = arg;
								} else {
									args[i] = Double.parseDouble(arg);
								}
							}
							return new MPlayerOperation.Reflect(raw, data[1], script, mplayer, method, args);
						} else {
							return new MPlayerOperation.Reflect(raw, data[1], script, mplayer, method, rawa);
						}
					} else {
						return new MPlayerOperation.Reflect(raw, data[1], script, mplayer, method);
					}
				} else if (command.equals("extract")) {
					String[] data = line.split(" ");
					MPlayer mplayer = null;
					if (script.getVariable(data[1]) != null) {
						mplayer = (MPlayer) script.getVariable(data[1]);
					} else {
						mplayer = new MPlayer(Bukkit.getPlayer(data[1]));
					}
					return new MPlayerOperation.Extract(raw, data[1], script, mplayer);
				}
			} else if (line.startsWith("run")) {
				line = line.substring(3).trim();
				String[] args = line.split(" ");
				String command = args[0];
				if (command.equals("script")) {
					String path = args[1];
					String name = path;
					if (path.contains(File.separator)) {
						name = path.substring(path.lastIndexOf(File.separator.charAt(0)) + 1);
					}
					Script script = new Script(name, new File(Main.pl.getDataFolder(), path));
					boolean transfer = Boolean.parseBoolean(args[3]);
					if (transfer) {
						Log.log(getClass(), script.getName() + " > Transfering variables in " + this.script.getName()
								+ " to " + script.getName(), Log.Level.INFO);
						HashMap<String, Object> vars = this.script.getVariables();
						boolean worked = false;
						for (String key : vars.keySet()) {
							worked = script.addVariable(key);
							worked = script.setVariable(key, vars.get(key));
						}
						if (!worked) {
							Log.log(getClass(), script.getName() + " > Could not transfer variables in "
									+ this.script.getName() + " to " + script.getName(), Log.Level.WARNING);
						}
					}
					String section = args[2];
					return new RunOperation(raw, script, ScriptManager.readScript(script, Section.PROCEDURE, section));
				} else if (command.equals("procedure")) {
					String name = args[1];
					return new RunOperation(raw, script, ScriptManager.readScript(script, Section.PROCEDURE, name));
				} else if (command.equals("function")) {
					String function = args[2];
					if (!ScriptUtils.isFunction(function, script)) {
						return new ErrorOperation(raw, "The specified function {" + function + "} is undefined!", this,
								Log.Level.FATAL);
					}
					return new RunOperation(raw, script, ScriptManager.readScript(script, Section.FUNCTION, function));
				}
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

	public Script.Section.EventType getEvent() {
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
