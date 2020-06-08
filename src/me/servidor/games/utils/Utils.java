package me.servidor.games.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import me.servidor.games.item.ItemsCache;
import me.servidor.games.managers.Constants;
import me.servidor.games.managers.Manager;

public class Utils {

	private Manager manager;

	public Utils(Manager manager) {
		this.manager = manager;
	}

	public void createBox(String[] text) {
		String format = "+---------------------------------------------------+";
		System.out.println(format);
		for (String line : text) {
			if (format.length() > line.length()) {
				for (int substring = format.length() - line.length(), i = 0; i < substring / 2; ++i) {
					line = " " + line + " ";
				}
			}
			line = "|" + ((line.length() % 2 == 0) ? line.substring(0, line.length() - 1)
					: line.substring(0, line.length() - 2)) + "|";
			System.out.println(line);
		}
		System.out.println(format);
	}

	public enum TimeFormatMode {
		COMPLETE, INCOMPLETE
	}

	public String formatTime(int time, TimeFormatMode format) {
		if (format.equals(TimeFormatMode.COMPLETE)) {
			String m = " minuto", s = " segundo";
			if (time >= 60) {
				Integer minutes = time / 60, seconds = time - minutes * 60;
				String minute = (minutes > 1 ? minutes + m + "s" : minutes + m),
						second = (seconds > 1 ? "e " + seconds + s + "s" : "e " + seconds + s);

				return minute + (seconds != 0 ? " " + second : "");
			}
			return (time > 1 ? time + s + "s" : time + s);
		} else {
			if (time >= 60) {
				int minutes = time / 60, seconds = time - minutes * 60;
				return minutes + "m " + (seconds != 0 ? seconds + "s" : "");
			}
			return (time > 0 ? time + "s" : "");
		}
	}

	public void log(String log) {
		System.out.println("PartyGames - " + log);
	}

	public void debug(String debug) {
		if (Constants.DEBUG_MODE)
			System.out.println("PartyGames(debug) - " + debug);
	}

	public void sleep() {
		try {
			Thread.sleep(500L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void registerInConfig(Object where, Object toSet) {
		manager.getPlugin().getConfig().set(String.valueOf(where), String.valueOf(toSet));
		manager.getPlugin().saveConfig();
	}

	public void registerLocationConfig(Player player, String config) {
		FileConfiguration file = manager.getPlugin().getConfig();
		Location location = player.getLocation();

		file.set(config + ".x", location.getX());
		file.set(config + ".y", location.getY());
		file.set(config + ".z", location.getZ());
		file.set(config + ".pitch", location.getPitch());
		file.set(config + ".yaw", location.getYaw());

		manager.getPlugin().saveConfig();
	}

	public Location getLocationFromConfig(String config) {
		FileConfiguration file = manager.getPlugin().getConfig();
		Location location = new Location(Bukkit.getWorld("world"), file.getDouble(config + ".x"),
				file.getDouble(config + ".y"), file.getDouble(config + ".z"));

		location.setPitch(file.getLong(config + ".pitch"));
		location.setYaw(file.getLong(config + ".yaw"));

		return location;
	}

	public void teleportPlayer(Player player, String config) {
		FileConfiguration file = manager.getPlugin().getConfig();

		if (!file.contains(config + ".x"))
			return;

		player.teleport(getLocationFromConfig(config));
	}

	public int getMaximumId(String config, int start) {
		int max = 0;

		for (int i = start; i < 100; i++)
			max = (manager.getPlugin().getConfig().contains(config + i) ? i : max);

		return max;
	}

	public List<Block> getBlocksFromTwoPoints(Location loc1, Location loc2) {
		List<Block> blocks = new ArrayList<Block>();

		int topBlockX = (loc1.getBlockX() < loc2.getBlockX() ? loc2.getBlockX() : loc1.getBlockX());
		int bottomBlockX = (loc1.getBlockX() > loc2.getBlockX() ? loc2.getBlockX() : loc1.getBlockX());

		int topBlockY = (loc1.getBlockY() < loc2.getBlockY() ? loc2.getBlockY() : loc1.getBlockY());
		int bottomBlockY = (loc1.getBlockY() > loc2.getBlockY() ? loc2.getBlockY() : loc1.getBlockY());

		int topBlockZ = (loc1.getBlockZ() < loc2.getBlockZ() ? loc2.getBlockZ() : loc1.getBlockZ());
		int bottomBlockZ = (loc1.getBlockZ() > loc2.getBlockZ() ? loc2.getBlockZ() : loc1.getBlockZ());

		for (int x = bottomBlockX; x <= topBlockX; x++) {
			for (int z = bottomBlockZ; z <= topBlockZ; z++) {
				for (int y = bottomBlockY; y <= topBlockY; y++) {
					blocks.add(loc1.getWorld().getBlockAt(x, y, z));
				}
			}
		}

		return blocks;
	}

	public boolean isNumber(String string) {
		try {
			Integer.valueOf(string);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public String captalize(String toCaptalize) {
		return toCaptalize.substring(0, 1).toUpperCase() + toCaptalize.substring(1);
	}

	public void fillInventory(Inventory inventory, ItemsCache item, int quantity) {
		for (int i = 0; i < quantity; ++i) {
			if (inventory.getItem(i) == null)
				item.build(inventory);
		}
	}

	public void fillInventory(Player player, ItemsCache item, int quantity) {
		fillInventory(player.getInventory(), item, quantity);
	}

}
