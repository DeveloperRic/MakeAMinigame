package com.rictacius.makeAMinigame.minigame.script.operation;

import java.util.UUID;

import org.bukkit.Bukkit;

import com.rictacius.makeAMinigame.data.MPlayer;
import com.rictacius.makeAMinigame.minigame.script.Script;
import com.rictacius.makeAMinigame.minigame.script.ScriptManager;
import com.rictacius.makeAMinigame.minigame.script.ScriptUtils;
import com.rictacius.makeAMinigame.util.Log;
import com.rictacius.makeAMinigame.util.exception.ErrorHandler;

public abstract class MPlayerOperation extends Operation {

	public MPlayerOperation(String raw) {
		super(raw);
	}

	public abstract void run();

	public static class Create extends ReturnOperation {
		private String args;

		public Create(String raw, String variable, Script script) {
			super(raw, variable, script);
			args = variable;
		}

		@Override
		public void run() {
			extract();
		}

		@Override
		public Object extract() {
			MPlayer mplayer = null;
			try {
				if (ScriptUtils.isFunction(args, script)) {
					ReturnOperation op = (ReturnOperation) script
							.function(ScriptManager.readScript(script, Script.Section.FUNCTION, args));
					mplayer = (MPlayer) op.extract();
				} else if (Bukkit.getPlayer(args) != null) {
					mplayer = new MPlayer(Bukkit.getPlayer(args));
				} else {
					try {
						String var = script.getVariable(args).toString();
						if (Bukkit.getPlayer(var) != null) {
							mplayer = new MPlayer(Bukkit.getPlayer(var));
						} else if (Bukkit.getPlayer(UUID.fromString(var)) != null) {
							mplayer = new MPlayer(Bukkit.getPlayer(UUID.fromString(var)));
						}
					} catch (Exception e) {
						ErrorHandler.throwScriptError(getClass(),
								"Invalid arguments for MPlayer Create Operation! Args=" + args + " Line=" + raw, Log.Level.FATAL, e);
					}
				}
			} catch (Exception e) {
				ErrorHandler.throwScriptError(getClass(), "Invalid arguments for MPlayer Create Operation! Line=" + raw,
						Log.Level.FATAL, e);
			}
			return mplayer;
		}

	}

	public static class Message extends MPlayerOperation {
		private MPlayer player;
		private String message;

		public Message(String raw, MPlayer player, String message) {
			super(raw);
			this.player = player;
			this.message = message;
		}

		@Override
		public void run() {
			player.message(message);
		}

	}

}
