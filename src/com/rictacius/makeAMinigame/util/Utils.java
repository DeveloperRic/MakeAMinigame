package com.rictacius.makeAMinigame.util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class Utils {

	public Utils() {
	}

	public static Location stringToLocation(String string) {
		Location l = null;
		try {
			String[] e = string.split(",");
			World w = Bukkit.getServer().getWorld(e[0]);
			l = new Location(w, Double.parseDouble(e[1]), Double.parseDouble(e[2]), Double.parseDouble(e[3]),
					Float.parseFloat(e[4]), Float.parseFloat(e[5]));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return l;
	}

	public static List<Location> stringListToLocaionList(List<String> strings) {
		List<Location> locs = new ArrayList<Location>();
		try {
			for (String string : strings) {
				Location l = null;
				String[] e = string.split(",");
				World w = Bukkit.getServer().getWorld(e[0]);
				l = new Location(w, Double.parseDouble(e[1]), Double.parseDouble(e[2]), Double.parseDouble(e[3]),
						Float.parseFloat(e[4]), Float.parseFloat(e[5]));
				locs.add(l);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return locs;
	}

	public static String locationToString(Location loc) {
		String world = loc.getWorld().getName();
		double x = loc.getX();
		double y = loc.getY();
		double z = loc.getZ();
		float yaw = loc.getYaw();
		float pitch = loc.getPitch();
		String locString = world + "," + x + "," + y + "," + z + "," + yaw + "," + pitch;
		return locString;
	}

}
