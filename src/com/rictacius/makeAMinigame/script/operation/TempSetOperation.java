package com.rictacius.makeAMinigame.script.operation;

import java.util.List;

import com.rictacius.makeAMinigame.script.Script;
import com.rictacius.makeAMinigame.script.ScriptLine;
import com.rictacius.makeAMinigame.script.ScriptManager;
import com.rictacius.makeAMinigame.script.ScriptUtils;
import com.rictacius.makeAMinigame.script.Script.Section;
import com.rictacius.makeAMinigame.util.Log;
import com.rictacius.makeAMinigame.util.exception.ErrorHandler;

public class TempSetOperation extends Operation {
	private String left;
	private String right;
	private Script script;
	private ScriptLine source;

	public TempSetOperation(String raw, String left, String right, Script script, ScriptLine source) {
		super(raw);
		this.left = left;
		this.right = right;
		this.source = source;
		this.script = script;
	}

	@Override
	public void run() {
		try {
			String variable = left;
			if (ScriptUtils.isFunction(left, script)) {
				ReturnOperation op = (ReturnOperation) script
						.function(ScriptManager.readScript(script, Script.Section.FUNCTION, left));
				variable = op.getVariable();
			}
			Object value = null;
			if (ScriptUtils.isFunction(right, script)) {
				List<ScriptLine> lines = ScriptManager.readScript(script, Section.FUNCTION, right);
				Log.log(getClass(), script.getName() + " > Searching for variable in function " + right,
						Log.Level.INFO);
				for (ScriptLine line : lines) {
					Operation operation = line.parse();
					if (operation instanceof ReturnOperation) {
						ReturnOperation returnop = (ReturnOperation) operation;
						Object found = returnop.extract();
						if (found != null)
							Log.log(getClass(), script.getName() + " > Found variable in function " + right
									+ " variable=" + returnop.getVariable(), Log.Level.INFO);
						value = found;
					} else {
						operation.run();
					}
				}
			} else {
				String classname = right.substring(right.indexOf('(') + 1, right.indexOf(')') + 1);
				String var = right.substring(right.indexOf(')') + 1).trim();
				Object svar = script.getVariable(var);
				if (svar != null) {
					try {
						Class<?> caster = Class.forName(classname);
						value = caster.cast(svar);
					} catch (Exception e) {
						try {
							if (classname.equalsIgnoreCase("int")) {
								value = (int) svar;
							} else if (classname.equalsIgnoreCase("boolean")) {
								value = (boolean) svar;
							} else if (classname.equalsIgnoreCase("String")) {
								value = (String) svar;
							} else if (classname.equalsIgnoreCase("long")) {
								value = (long) svar;
							} else if (classname.equalsIgnoreCase("float")) {
								value = (float) svar;
							}
						} catch (Exception e1) {
							ErrorHandler.throwScriptError(getClass(), "Invalid cast parameter " + classname,
									Log.Level.WARNING, e1);
							return;
						}
					}
				} else {
					ScriptLine sub = new ScriptLine(right, source.getSection(), source.getLineNumber(), script);
					Operation op = sub.parse();
					Log.log(getClass(),
							script.getName() + " > Script subline is of type " + op.getClass().getSimpleName(),
							Log.Level.INFO);
					try {
						ReturnOperation returnop = (ReturnOperation) op;
						value = returnop.extract();
					} catch (Exception e1) {
						op.run();
						ErrorHandler.throwScriptError(getClass(), "Error with script subline " + right, Log.Level.FATAL,
								e1);
						return;
					}
				}
			}
			Log.log(getClass(), script.getName() + " > SetVariable " + variable + " to instanceof "
					+ value.getClass().getSimpleName(), Log.Level.INFO);
			script.setTempVariable(variable, value);
		} catch (Exception e1) {
			ErrorHandler.throwScriptError(getClass(),
					script.getName() + " > Invalid arguments for SetOperation! Line=" + source.getLine(),
					Log.Level.FATAL, e1);
			return;
		}
	}

	public String getLeft() {
		return left;
	}

	public String getRight() {
		return right;
	}

	public Script getScript() {
		return script;
	}

	public ScriptLine getSource() {
		return source;
	}

}
