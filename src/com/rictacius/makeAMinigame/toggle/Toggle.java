package com.rictacius.makeAMinigame.toggle;

import com.rictacius.makeAMinigame.minigame.Arena;

public abstract class Toggle {
	private Type type;
	private boolean enabled;
	private Arena arena;

	public static enum Type {
	}

	public Toggle(Type type) {
		this.type = type;
	}

	public abstract void onEnable();

	public abstract void onDiable();

	/**
	 * @return the value
	 */
	public Type getValue() {
		return type;
	}

	/**
	 * @return the enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * @param enabled
	 *            the enabled to set
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		if (enabled) {
			onEnable();
		} else {
			onDiable();
		}
	}

	/**
	 * @return the arena
	 */
	public Arena getArena() {
		return arena;
	}

}
