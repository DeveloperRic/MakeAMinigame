package com.rictacius.makeAMinigame.minigame;

import java.util.ArrayList;
import java.util.List;

import com.rictacius.makeAMinigame.minigame.toggle.Toggle;

public class Minigame {
	private List<Toggle> toggles = new ArrayList<Toggle>();
	private String id;

	public Minigame(String id) {
		this.id = id;
	}

	/**
	 * @return the toggles
	 */
	public List<Toggle> getToggles() {
		return toggles;
	}

	/**
	 * @param toggles the toggles to set
	 */
	public void setToggles(List<Toggle> toggles) {
		this.toggles = toggles;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

}
