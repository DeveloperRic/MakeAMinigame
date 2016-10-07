package com.rictacius.makeAMinigame.minigame.script.operation;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.rictacius.makeAMinigame.util.Log;

public class MathOperation extends Operation {
	private String equation;
	private double solution;
	private Object evaluation;

	public MathOperation(String raw, String equation) {
		super(raw);
		this.equation = equation;
	}

	@Override
	public void run() {
		ScriptEngineManager mgr = new ScriptEngineManager();
		ScriptEngine engine = mgr.getEngineByName("JavaScript");
		try {
			evaluation = engine.eval(equation.replaceAll(" ", ""));
			solution = (double) evaluation;
		} catch (ScriptException e) {
			Log.log(getClass(), "Could not evaluate math operation " + equation, Log.Level.FATAL, e);
		}
	}

	public String getEqution() {
		return equation;
	}

	public double getSolution() {
		return solution;
	}

	public Object getEvaluation() {
		return evaluation;
	}

}
