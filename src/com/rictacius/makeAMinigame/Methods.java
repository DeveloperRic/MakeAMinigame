package com.rictacius.makeAMinigame;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;

final class Methods {

	public static void sendColoredMessage(ChatColor ecolor, String message, ChatColor color) {
		Bukkit.getConsoleSender()
				.sendMessage(ecolor + "[" + Main.pl.getDescription().getName() + "] " + color + message);
	}

	public static void sendColoredConsoleMessage(ChatColor ecolor, String message) {
		ConsoleCommandSender c = Bukkit.getServer().getConsoleSender();
		c.sendMessage(ecolor + message);

	}

	public static void sendColoredConsoleMessage(net.md_5.bungee.api.ChatColor ecolor, String message) {
		ConsoleCommandSender c = Bukkit.getServer().getConsoleSender();
		c.sendMessage(ecolor + message);
	}
}
