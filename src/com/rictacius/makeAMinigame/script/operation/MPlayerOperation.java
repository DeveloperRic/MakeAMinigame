package com.rictacius.makeAMinigame.script.operation;

import java.lang.reflect.Method;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.rictacius.makeAMinigame.data.MPlayer;
import com.rictacius.makeAMinigame.script.Script;
import com.rictacius.makeAMinigame.script.ScriptManager;
import com.rictacius.makeAMinigame.script.ScriptUtils;
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
								"Invalid arguments for MPlayer Create Operation! Args=" + args + " Line=" + raw,
								Log.Level.FATAL, e);
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

	public static class Reflect extends ReturnOperation {
		private MPlayer mplayer;
		private String method;
		private Object[] args;
		private Player player;

		public Reflect(String raw, String variable, Script script, MPlayer player, String method, Object... args) {
			super(raw, variable, script);
			this.mplayer = player;
			this.method = method;
			this.args = args;
			this.player = mplayer.player();
		}

		@Override
		public Object extract() {
			try {
				Class<?>[] a = new Class<?>[args.length];
				for (int b = 0; b < args.length; b++) {
					a[b] = args.getClass();
				}
				Method c = player.getClass().getMethod(method, a);
				Object d = c.invoke(player);
				Log.log(getClass(), script.getName() + " > Extracted Object of instance " + d.getClass().getSimpleName()
						+ " from reflect operation " + raw, Log.Level.INFO);
				return d;
			} catch (Exception e) {
				Log.log(getClass(), script.getName() + " > Could not extract Object from reflect operation " + raw,
						Log.Level.WARNING, e);
				return null;
			}
		}

	}

	public static class Extract extends ReturnOperation {
		private MPlayer player;

		public Extract(String raw, String variable, Script script, MPlayer player) {
			super(raw, variable, script);
			this.player = player;
		}

		@Override
		public Object extract() {
			return player.player();
		}

	}

}
