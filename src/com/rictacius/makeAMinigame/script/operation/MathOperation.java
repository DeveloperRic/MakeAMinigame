package com.rictacius.makeAMinigame.script.operation;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.rictacius.makeAMinigame.script.Script;
import com.rictacius.makeAMinigame.util.Log;

public class MathOperation extends ReturnOperation {
	private String equation;
	private Object evaluation = "_UNKNOWN_";

	public MathOperation(String raw, String variable, Script script, String equation) {
		super(raw, variable, script);
		this.equation = equation.replaceAll(" ", "");
	}

	@Override
	public void run() {
		ScriptEngineManager mgr = new ScriptEngineManager();
		ScriptEngine engine = mgr.getEngineByName("JavaScript");
		try {
			evaluation = engine.eval(equation);
			Log.log(getClass(), script.getName() + " > Math operation = " + evaluation, Log.Level.INFO);
		} catch (ScriptException e) {
			Log.log(getClass(), script.getName() + " > Could not evaluate math operation " + equation, Log.Level.FATAL,
					e);
		}
		if (evaluation.equals("_UNKNOWN_")) {
			Log.log(getClass(), script.getName() + " > Could not evaluate math operation " + equation, Log.Level.FATAL);
		}
	}

	public String getEqution() {
		return equation;
	}

	@Override
	public Object extract() {
		run();
		return evaluation;
	}

}
