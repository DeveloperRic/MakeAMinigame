package com.rictacius.makeAMinigame.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;

import com.rictacius.makeAMinigame.Main;
import com.rictacius.makeAMinigame.test.MAMTester;

import net.md_5.bungee.api.ChatColor;

public class Log {
	private static boolean enabled = true;
	private static ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
	private static ArrayList<String> timeline = new ArrayList<String>();
	private static Class<?> previous;

	public Log() {
		enabled = true;
	}

	static String prefix() {
		return ChatColor.translateAlternateColorCodes('&', "&7[&aMakeAMinigame&7] &r");
	}

	/**
	 * Defines the level of a Log entry.
	 * 
	 * @author RictAcius
	 *
	 */
	public enum Level {
		FATAL(3), WARNING(2), INFO(1);

		private Level(int lvl) {
			this.lvl = lvl;
		}

		private int lvl;

		public int getLevel() {
			return lvl;
		}
	}

	/**
	 * Logs an event or process
	 * 
	 * @param message
	 *            the event/process to log
	 * @param lvl
	 *            the level of the message
	 * @see Level Log Levels
	 */
	public static void log(Class<?> source, String message, Level lvl) {
		if (enabled) {
			if (timeline.size() >= Integer.parseInt(Main.pl.getConfig().getString("logger-size")))
				timeline.clear();
			String send = source.getSimpleName();
			String raw = "| [" + send + "] |" + message;
			int slvl = lvl.getLevel();
			switch (slvl) {
			case 1:
				send = (prefix() + ChatColor.WHITE + message);
				break;
			case 2:
				send = (prefix() + ChatColor.YELLOW + message);
				break;
			case 3:
				send = (prefix() + ChatColor.RED + message);
				break;
			default:
				send = (prefix() + ChatColor.AQUA + message);
				break;
			}
			SimpleDateFormat formatter = new SimpleDateFormat("d/m/yyyy HH:mm");
			String dateString = formatter.format(new Date());
			if (previous != null) {
				String prev = previous.getName();
				String next = source.getName();
				if (prev.contains("\\.") && next.contains("\\.")) {
					String[] prevd = prev.split("\\.");
					String[] nextd = prev.split("\\.");
					boolean common = false;
					int i;
					int dif = 0;
					for (i = 0; i < prevd.length && i < nextd.length; i++) {
						if (prevd[i].equals(nextd[i])) {
							common = true;
						} else {
							dif = i - 3;
						}
					}
					String degree = "";
					for (int j = 0; j <= dif; j++) {
						if (j > 5)
							break;
						degree = degree + "=";
					}
					if (!common) {
						if (!previous.getName().equals(source.getName())) {
							console.sendMessage("    !" + degree + "!");
						}
					}
				} else {
					if (!previous.getName().equals(source.getName())) {
						console.sendMessage("    !====!");
					}
				}
			}
			if (Boolean.parseBoolean(Main.pl.getConfig().getString("debug")) || MAMTester.testing) {
				console.sendMessage(send);
			}
			timeline.add(dateString + ChatColor.stripColor(raw));
			previous = source;
		}
	}

	/**
	 * Logs an error arising from an event/process
	 * 
	 * @param message
	 *            the event/process to log
	 * @param lvl
	 *            the level of the message
	 * @param e
	 *            the error that occured
	 * @see Level Log Levels
	 */
	public static void log(Class<?> source, String message, Level lvl, Exception e) {
		log(source, "--------------------------------------------------------", lvl);
		log(source, message, lvl);
		log(source, " E: " + e.getClass().getSimpleName() + ", Trace: ", lvl);
		for (StackTraceElement el : e.getStackTrace()) {
			log(source, "  Class= " + el.getClassName() + " , Method= " + el.getMethodName() + " , Loc= "
					+ el.getLineNumber(), lvl);
		}
		log(source, "--------------------------------------------------------", lvl);
	}

	public static void setEnabled(boolean enabled) {
		Log.enabled = enabled;
	}

	/**
	 * @return the timeline
	 */
	public ArrayList<String> getTimeline() {
		return timeline;
	}
}
