package com.rictacius.makeAMinigame.minigame.script.operation;

import com.rictacius.makeAMinigame.minigame.script.Script;
import com.rictacius.makeAMinigame.minigame.script.ScriptManager;
import com.rictacius.makeAMinigame.minigame.script.ScriptUtils;

public class SetOperation extends Operation {
	private String left;
	private String right;
	private Script source;

	public SetOperation(String raw, String left, String right, Script source) {
		super(raw);
		this.left = left;
		this.right = right;
	}

	@Override
	public void run() {
		String variable = left;
		if (ScriptUtils.isFunction(left, source)) {
			ReturnOperation op = (ReturnOperation) source
					.function(ScriptManager.readScript(source, Script.Section.FUNCTION, left));
			variable = op.getVariable();
		}
		Object value = null;
		if (ScriptUtils.isFunction(right, source)) {
			ReturnOperation op = (ReturnOperation) source
					.function(ScriptManager.readScript(source, Script.Section.FUNCTION, right));
			value = op.extract();
		} else {
			String classname = right.substring(right.indexOf('(') + 1, right.indexOf(')') + 1);
			String var = right.substring(right.indexOf(')') + 1).trim();
			Object svar = source.getVariable(var);
			try {
				Class<?> caster = Class.forName(classname);
				value = caster.cast(svar);
			} catch (Exception e) {
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
			}
		}
		source.setVariable(variable, value);
	}

	public String getLeft() {
		return left;
	}

	public String getRight() {
		return right;
	}

	public Script getScript() {
		return source;
	}

}
