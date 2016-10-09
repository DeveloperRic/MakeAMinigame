package com.rictacius.makeAMinigame.test;

import java.io.File;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;

import com.rictacius.makeAMinigame.Main;
import com.rictacius.makeAMinigame.script.Script;
import com.rictacius.makeAMinigame.script.ScriptManager;

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
		}
		return true;
	}

}
