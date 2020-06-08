package me.servidor.games.item;

import java.util.List;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.servidor.games.principal.PartyGames;

public class InventoryUtils {

	public void animate(Inventory inventory, ItemStack[] items, int startSlot, long time, Sound sound, Player player) {
		new BukkitRunnable() {
			int id = 0;

			public void run() {
				if (id >= items.length || items[id] == null) {
					cancel();
					return;
				}
				inventory.setItem(startSlot + id, items[id]);
				if (sound != null && player != null)
					player.playSound(player.getLocation(), sound, 4.0F, 4.0F);
				if (id > 0 && inventory.getViewers().size() == 0)
					cancel();
				id++;
			}
		}.runTaskTimerAsynchronously(PartyGames.getPlugin(PartyGames.class), 0L, time);
	}

	public void animateBySlot(Inventory inventory, ItemStack[] items, Integer[] slots, long time, Sound sound, Player player) {
		new BukkitRunnable() {
			int id = 0;

			public void run() {
				if (id >= items.length || items[id] == null) {
					cancel();
					return;
				}
				
				inventory.setItem(slots[id], items[id]);
				
				if (sound != null && player != null)
					player.playSound(player.getLocation(), sound, 4.0F, 4.0F);
				
				if (id > 0 && inventory.getViewers().size() == 0)
					cancel();
				
				id++;
			}
		}.runTaskTimerAsynchronously(PartyGames.getPlugin(PartyGames.class), 0L, time);
	}
	
	/* Aliases */
	
	public void animateBySlot(Inventory inventory, List<ItemStack> items, Integer[] slots, long time, Sound sound, Player player) {
		animateBySlot(inventory, items.toArray(new ItemStack[] {}), slots, time, sound, player);
	}
	
	public void animate(Inventory inventory, List<ItemStack> items, int startSlot, long time, Sound sound, Player player) {
		animate(inventory, items.toArray(new ItemStack[] {}), startSlot, time, sound, player);
	}

}
