package com.rictacius.makeAMinigame.minigame;

import java.util.HashMap;

public class MinigameManager {
	private static HashMap<String, Minigame> minigames = new HashMap<String, Minigame>();

	public MinigameManager() {
	}

	public static Minigame getMinigame(String id) {
		return minigames.get(id);
	}

	public static void addMinigame(Minigame minigame) {
		minigames.put(minigame.getId(), minigame);
	}
	
	public static void loadMinigames() {
		
	}

}
