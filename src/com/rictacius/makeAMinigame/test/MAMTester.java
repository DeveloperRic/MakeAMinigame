package com.rictacius.makeAMinigame.test;

import java.io.File;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;

import com.rictacius.makeAMinigame.Main;
import com.rictacius.makeAMinigame.script.Script;
import com.rictacius.makeAMinigame.script.ScriptManager;
import com.rictacius.makeAMinigame.util.Log;

import net.md_5.bungee.api.ChatColor;

public class MAMTester implements CommandExecutor, Listener {
	public static final boolean testing = true;
	public static final String testPlayer = "RictAcius";

	public MAMTester() {
	}

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if (!testing) {
			sender.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD
					+ "This command is only for use by alpha and pre-release testers!");
			return true;
		}
		if (args[0].equalsIgnoreCase("runscript")) {
			sender.sendMessage("-----------------------");
			String path = args[1];
			sender.sendMessage("PATH=" + path);
			String name = path;
			if (path.contains(File.separator)) {
				name = path.substring(path.lastIndexOf(File.separator.charAt(0)) + 1);
			}
			sender.sendMessage("NAME=" + name);
			Script script = new Script(name, new File(Main.pl.getDataFolder(), path));
			sender.sendMessage("Created script (" + name + ") ");
			sender.sendMessage("Loading script " + name);
			sender.sendMessage("-----------------------");
			ScriptManager.addScript(name, script);
		} else if (args[0].equalsIgnoreCase("compile")) {
			sender.sendMessage("-----------------------");
			sender.sendMessage("THIS COMMAND IS MEANT FOR THE IDENTIFICATION OF COMPILE ERRORS. "
					+ "IT MAY NOT DETECT RUNTIME ERRORS!");
			String path = args[1];
			sender.sendMessage("PATH=" + path);
			String name = path;
			if (path.contains(File.separator)) {
				name = path.substring(path.lastIndexOf(File.separator.charAt(0)) + 1);
			}
			sender.sendMessage("NAME=" + name);
			Script script = new Script(name, new File(Main.pl.getDataFolder(), path));
			sender.sendMessage("Created script (" + name + ") ");
			sender.sendMessage("Compiling script " + name);
			for (Script.Section sec : Script.Section.values()) {
				if (sec.equals(Script.Section.PROCEDURE) || sec.equals(Script.Section.FUNCTION))
					continue;
				try {
					ScriptManager.compile(ScriptManager.readScript(script, sec));
				} catch (Exception e) {
					Log.log(getClass(), "Could not compile Section " + sec.toString() + " in script " + name,
							Log.Level.WARNING, e);
				}
			}
			for (String function : ScriptManager.getFunctions(script)) {
				try {
					ScriptManager.compile(ScriptManager.readScript(script, Script.Section.FUNCTION, function));
				} catch (Exception e) {
					Log.log(getClass(), "Could not compile Function " + function + " in script " + name,
							Log.Level.WARNING, e);
				}
			}
			sender.sendMessage("-------COMPLETE--------");
		}
		return true;
	}

}
