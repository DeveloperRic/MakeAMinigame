package com.rictacius.makeAMinigame;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.rictacius.makeAMinigame.test.MAMTester;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

public class Main extends JavaPlugin implements Listener {
	PluginDescriptionFile pdfFile = getDescription();
	Logger logger = getLogger();

	public static Main pl;

	public void onEnable() {
		pl = this;
		Methods.sendColoredMessage(ChatColor.AQUA, ("Registering Config...."), ChatColor.YELLOW);
		createFiles();
		Methods.sendColoredMessage(ChatColor.AQUA, ("Registering Utils...."), ChatColor.YELLOW);
		registerUtils();
		Methods.sendColoredMessage(ChatColor.AQUA, ("Registering Commands...."), ChatColor.YELLOW);
		registerCommands();
		Methods.sendColoredMessage(ChatColor.AQUA, ("Registering Events...."), ChatColor.YELLOW);
		registerEvents();
		Methods.sendColoredMessage(ChatColor.AQUA,
				(pdfFile.getName() + " has been enabled! (V." + pdfFile.getVersion() + ")"), ChatColor.GREEN);
	}

	public void registerUtils() {
		setupChat();
		setupEconomy();
		setupPermissions();
		Methods.sendColoredMessage(ChatColor.AQUA, ("Utils successfuly registered!"), ChatColor.LIGHT_PURPLE);
	}

	public static Permission permission = null;
	public static Economy economy = null;
	public static Chat chat = null;

	private boolean setupPermissions() {
		RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager()
				.getRegistration(net.milkbowl.vault.permission.Permission.class);
		if (permissionProvider != null) {
			permission = permissionProvider.getProvider();
		}
		return (permission != null);
	}

	private boolean setupChat() {
		RegisteredServiceProvider<Chat> chatProvider = getServer().getServicesManager()
				.getRegistration(net.milkbowl.vault.chat.Chat.class);
		if (chatProvider != null) {
			chat = chatProvider.getProvider();
		}

		return (chat != null);
	}

	private boolean setupEconomy() {
		RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager()
				.getRegistration(net.milkbowl.vault.economy.Economy.class);
		if (economyProvider != null) {
			economy = economyProvider.getProvider();
		}

		return (economy != null);
	}

	public void onDisable() {

		Methods.sendColoredMessage(ChatColor.AQUA,
				(pdfFile.getName() + " has been disabled! (V." + pdfFile.getVersion() + ")"), ChatColor.YELLOW);
	}

	public void registerCommands() {
		try {
			getCommand("mamtest").setExecutor(new MAMTester());
		} catch (Exception e) {
			Methods.sendColoredMessage(ChatColor.AQUA, ("Error while registering commands!"), ChatColor.RED);
			Methods.sendColoredMessage(ChatColor.AQUA, ("Trace:"), ChatColor.RED);
			e.printStackTrace();
		}
		Methods.sendColoredMessage(ChatColor.AQUA, ("Commands successfuly registered!"), ChatColor.LIGHT_PURPLE);
	}

	public void registerEvents() {
		try {
			@SuppressWarnings("unused")
			PluginManager pm = getServer().getPluginManager();

		} catch (Exception e) {
			Methods.sendColoredMessage(ChatColor.AQUA, ("Error while registering events!"), ChatColor.RED);
			Methods.sendColoredMessage(ChatColor.AQUA, ("Trace:"), ChatColor.RED);
			e.printStackTrace();
		}
		Methods.sendColoredMessage(ChatColor.AQUA, ("Events successfuly registered!"), ChatColor.LIGHT_PURPLE);
	}

	public void registerConfig() {
		try {
			getConfig().options().copyDefaults(true);
			saveConfig();

		} catch (Exception e) {
			Methods.sendColoredMessage(ChatColor.AQUA, ("Error while registering config!"), ChatColor.RED);
			Methods.sendColoredMessage(ChatColor.AQUA, ("Trace:"), ChatColor.RED);
			e.printStackTrace();
		}
		Methods.sendColoredMessage(ChatColor.AQUA, ("Config successfuly registered!"), ChatColor.LIGHT_PURPLE);
	}

	public static Plugin getPlugin() {
		return Bukkit.getServer().getPluginManager().getPlugin("Exteria_Utilities");
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private File configf;
	private FileConfiguration config;

	public int reloadAllConfigFiles() {
		int errors = 0;
		ArrayList<String> errorFiles = new ArrayList<String>();
		String file = "";
		ArrayList<StackTraceElement[]> traces = new ArrayList<StackTraceElement[]>();
		StackTraceElement[] trace = null;
		try {
			this.reloadConfig();
		} catch (Exception e) {
			errors++;
			trace = e.getStackTrace();
			traces.add(trace);
			file = "Main Config File";
			errorFiles.add(file);
		}
		try {
			// channels = YamlConfiguration.loadConfiguration(chatchannelsf);
		} catch (Exception e) {
			errors++;
			trace = e.getStackTrace();
			traces.add(trace);
			file = "Message Config File";
			errorFiles.add(file);
		}
		if (errors > 0) {
			Methods.sendColoredMessage(ChatColor.GOLD, ("Could not reload all config files!"), ChatColor.RED);
			Methods.sendColoredMessage(ChatColor.GOLD, ("The following files generated erros:"), ChatColor.RED);
			for (String fileName : errorFiles) {
				Methods.sendColoredMessage(ChatColor.GOLD, (ChatColor.GRAY + " - " + ChatColor.RED + fileName),
						ChatColor.RED);
			}
			Methods.sendColoredMessage(ChatColor.GOLD, ("Trace(s):"), ChatColor.RED);
			for (StackTraceElement[] currentTrace : traces) {
				int i = 0;
				Methods.sendColoredMessage(ChatColor.GOLD, (ChatColor.GRAY + "* " + ChatColor.RED + errorFiles.get(i)),
						ChatColor.RED);
				for (StackTraceElement printTrace : currentTrace) {
					Methods.sendColoredMessage(ChatColor.GOLD, (printTrace.toString()), ChatColor.RED);
				}
				i++;
			}
		}
		return errors;
	}

	public void saveAllConfigFiles() {
		saveConfig();
		try {
			// getChannelsConfig().save(chatchannelsf);
		} catch (Exception ex) {
			// Methods.sendColoredMessage(ChatColor.GOLD, ("Could not save
			// config to " + chatchannelsf), ChatColor.RED);
			// Methods.sendColoredMessage(ChatColor.GOLD, ("Trace:"),
			// ChatColor.RED);
			ex.printStackTrace();
		}

	}

	private void createFiles() {
		try {
			configf = new File(getDataFolder(), "config.yml");

			if (!configf.exists()) {
				configf.getParentFile().mkdirs();
				saveResource("config.yml", false);
			}

			config = new YamlConfiguration();
			try {
				config.load(configf);
			} catch (Exception e) {
				Methods.sendColoredMessage(ChatColor.LIGHT_PURPLE, ("Error while registering config!"), ChatColor.RED);
				e.printStackTrace();
			}
			getConfig().options().copyDefaults(true);
			saveAllConfigFiles();
			Methods.sendColoredMessage(ChatColor.AQUA, ("Config successfuly registered!"), ChatColor.LIGHT_PURPLE);
		} catch (Exception e) {
			Methods.sendColoredMessage(ChatColor.LIGHT_PURPLE, ("Error while registering config!"), ChatColor.RED);
			e.printStackTrace();
		}
	}
}
