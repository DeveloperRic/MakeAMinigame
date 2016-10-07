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
			getCommand("eutils").setExecutor(new eutils(this));
		} catch (Exception e) {
			Methods.sendColoredMessage(ChatColor.AQUA, ("Error while registering commands!"), ChatColor.RED);
			Methods.sendColoredMessage(ChatColor.AQUA, ("Trace:"), ChatColor.RED);
			e.printStackTrace();
		}
		Methods.sendColoredMessage(ChatColor.AQUA, ("Commands successfuly registered!"), ChatColor.LIGHT_PURPLE);
	}

	public void registerEvents() {
		try {
			PluginManager pm = getServer().getPluginManager();

			pm.registerEvents(new PData(), this);
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
	private File configf, chatchannelsf, reportsf, expf, votesf, bossbarf, nicksf;
	private FileConfiguration config, channels, reports, exp, votes, bossbar, nicks;

	public FileConfiguration getChannelsConfig() {
		return this.channels;
	}

	public FileConfiguration getReportsConfig() {
		return this.reports;
	}

	public FileConfiguration getExperienceConfig() {
		return this.exp;
	}

	public FileConfiguration getVotesConfig() {
		return this.votes;
	}

	public FileConfiguration getBossBarConfig() {
		return this.bossbar;
	}

	public FileConfiguration getNicksConfig() {
		return this.nicks;
	}

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
			channels = YamlConfiguration.loadConfiguration(chatchannelsf);
		} catch (Exception e) {
			errors++;
			trace = e.getStackTrace();
			traces.add(trace);
			file = "Message Config File";
			errorFiles.add(file);
		}
		try {
			reports = YamlConfiguration.loadConfiguration(reportsf);
		} catch (Exception e) {
			errors++;
			trace = e.getStackTrace();
			traces.add(trace);
			file = "Reports Config File";
			errorFiles.add(file);
		}
		try {
			exp = YamlConfiguration.loadConfiguration(expf);
		} catch (Exception e) {
			errors++;
			trace = e.getStackTrace();
			traces.add(trace);
			file = "Experience Config File";
			errorFiles.add(file);
		}
		try {
			votes = YamlConfiguration.loadConfiguration(votesf);
		} catch (Exception e) {
			errors++;
			trace = e.getStackTrace();
			traces.add(trace);
			file = "Vote Config File";
			errorFiles.add(file);
		}
		try {
			bossbar = YamlConfiguration.loadConfiguration(bossbarf);
		} catch (Exception e) {
			errors++;
			trace = e.getStackTrace();
			traces.add(trace);
			file = "BossBar Config File";
			errorFiles.add(file);
		}
		try {
			nicks = YamlConfiguration.loadConfiguration(nicksf);
		} catch (Exception e) {
			errors++;
			trace = e.getStackTrace();
			traces.add(trace);
			file = "Nicks Config File";
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
			getChannelsConfig().save(chatchannelsf);
		} catch (Exception ex) {
			Methods.sendColoredMessage(ChatColor.GOLD, ("Could not save config to " + chatchannelsf), ChatColor.RED);
			Methods.sendColoredMessage(ChatColor.GOLD, ("Trace:"), ChatColor.RED);
			ex.printStackTrace();
		}
		try {
			getReportsConfig().save(reportsf);
		} catch (Exception ex) {
			Methods.sendColoredMessage(ChatColor.GOLD, ("Could not save config to " + reportsf), ChatColor.RED);
			Methods.sendColoredMessage(ChatColor.GOLD, ("Trace:"), ChatColor.RED);
			ex.printStackTrace();
		}
		try {
			getExperienceConfig().save(expf);
		} catch (Exception ex) {
			Methods.sendColoredMessage(ChatColor.GOLD, ("Could not save config to " + expf), ChatColor.RED);
			Methods.sendColoredMessage(ChatColor.GOLD, ("Trace:"), ChatColor.RED);
			ex.printStackTrace();
		}
		try {
			getVotesConfig().save(votesf);
		} catch (Exception ex) {
			Methods.sendColoredMessage(ChatColor.GOLD, ("Could not save config to " + votesf), ChatColor.RED);
			Methods.sendColoredMessage(ChatColor.GOLD, ("Trace:"), ChatColor.RED);
			ex.printStackTrace();
		}
		try {
			getBossBarConfig().save(bossbarf);
		} catch (Exception ex) {
			Methods.sendColoredMessage(ChatColor.GOLD, ("Could not save config to " + bossbarf), ChatColor.RED);
			Methods.sendColoredMessage(ChatColor.GOLD, ("Trace:"), ChatColor.RED);
			ex.printStackTrace();
		}
		try {
			getNicksConfig().save(nicksf);
		} catch (Exception ex) {
			Methods.sendColoredMessage(ChatColor.GOLD, ("Could not save config to " + nicksf), ChatColor.RED);
			Methods.sendColoredMessage(ChatColor.GOLD, ("Trace:"), ChatColor.RED);
			ex.printStackTrace();
		}
	}

	public void saveChannelsFile() {
		if (channels == null || chatchannelsf == null) {
			return;
		}
		try {
			getChannelsConfig().save(chatchannelsf);
		} catch (Exception ex) {
			Methods.sendColoredMessage(ChatColor.GOLD, ("Could not save config to " + chatchannelsf), ChatColor.RED);
			Methods.sendColoredMessage(ChatColor.GOLD, ("Trace:"), ChatColor.RED);
			ex.printStackTrace();
		}
	}

	public void saveReportsFile() {
		try {
			getReportsConfig().save(reportsf);
		} catch (Exception ex) {
			Methods.sendColoredMessage(ChatColor.GOLD, ("Could not save config to " + reportsf), ChatColor.RED);
			Methods.sendColoredMessage(ChatColor.GOLD, ("Trace:"), ChatColor.RED);
			ex.printStackTrace();
		}
	}

	public void saveExperienceFile() {
		try {
			getExperienceConfig().save(expf);
		} catch (Exception ex) {
			Methods.sendColoredMessage(ChatColor.GOLD, ("Could not save config to " + expf), ChatColor.RED);
			Methods.sendColoredMessage(ChatColor.GOLD, ("Trace:"), ChatColor.RED);
			ex.printStackTrace();
		}
	}

	public void saveVotesFile() {
		try {
			getVotesConfig().save(votesf);
		} catch (Exception ex) {
			Methods.sendColoredMessage(ChatColor.GOLD, ("Could not save config to " + votesf), ChatColor.RED);
			Methods.sendColoredMessage(ChatColor.GOLD, ("Trace:"), ChatColor.RED);
			ex.printStackTrace();
		}
	}

	public void saveBossBarFile() {
		try {
			getBossBarConfig().save(bossbarf);
		} catch (Exception ex) {
			Methods.sendColoredMessage(ChatColor.GOLD, ("Could not save config to " + bossbarf), ChatColor.RED);
			Methods.sendColoredMessage(ChatColor.GOLD, ("Trace:"), ChatColor.RED);
			ex.printStackTrace();
		}
	}

	public void saveNicksFile() {
		try {
			getNicksConfig().save(nicksf);
		} catch (Exception ex) {
			Methods.sendColoredMessage(ChatColor.GOLD, ("Could not save config to " + nicksf), ChatColor.RED);
			Methods.sendColoredMessage(ChatColor.GOLD, ("Trace:"), ChatColor.RED);
			ex.printStackTrace();
		}
	}

	private void createFiles() {
		try {
			configf = new File(getDataFolder(), "config.yml");
			chatchannelsf = new File(getDataFolder(), "chatchannels.yml");
			reportsf = new File(getDataFolder(), "reports.yml");
			expf = new File(getDataFolder(), "experience.yml");
			votesf = new File(getDataFolder(), "votes.yml");
			bossbarf = new File(getDataFolder(), "bossbar.yml");
			nicksf = new File(getDataFolder(), "nicks.yml");

			if (!configf.exists()) {
				configf.getParentFile().mkdirs();
				saveResource("config.yml", false);
			}
			if (!chatchannelsf.exists()) {
				chatchannelsf.getParentFile().mkdirs();
				saveResource("chatchannels.yml", false);
			}
			if (!reportsf.exists()) {
				reportsf.getParentFile().mkdirs();
				saveResource("reports.yml", false);
			}
			if (!expf.exists()) {
				expf.getParentFile().mkdirs();
				saveResource("experience.yml", false);
			}
			if (!votesf.exists()) {
				votesf.getParentFile().mkdirs();
				saveResource("votes.yml", false);
			}
			if (!bossbarf.exists()) {
				bossbarf.getParentFile().mkdirs();
				saveResource("bossbar.yml", false);
			}
			if (!nicksf.exists()) {
				nicksf.getParentFile().mkdirs();
				saveResource("nicks.yml", false);
			}

			config = new YamlConfiguration();
			channels = new YamlConfiguration();
			reports = new YamlConfiguration();
			exp = new YamlConfiguration();
			votes = new YamlConfiguration();
			bossbar = new YamlConfiguration();
			nicks = new YamlConfiguration();
			try {
				config.load(configf);
				channels.load(chatchannelsf);
				reports.load(reportsf);
				exp.load(expf);
				votes.load(votesf);
				bossbar.load(bossbarf);
				nicks.load(nicksf);
			} catch (Exception e) {
				Methods.sendColoredMessage(ChatColor.LIGHT_PURPLE, ("Error while registering config!"), ChatColor.RED);
				e.printStackTrace();
			}
			getConfig().options().copyDefaults(true);
			getChannelsConfig().options().copyDefaults(true);
			getReportsConfig().options().copyDefaults(true);
			getExperienceConfig().options().copyDefaults(true);
			getVotesConfig().options().copyDefaults(true);
			getBossBarConfig().options().copyDefaults(true);
			getNicksConfig().options().copyDefaults(true);
			saveAllConfigFiles();
		} catch (Exception e) {
			Methods.sendColoredMessage(ChatColor.LIGHT_PURPLE, ("Error while registering config!"), ChatColor.RED);
			e.printStackTrace();
		}
	}
}
