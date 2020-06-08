package me.servidor.games.item;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import me.servidor.games.game.games.ItemBuilder;

public enum ItemsCache {

	KANGAROO_RACE(new ItemBuilder[] { new ItemBuilder().setMaterial(Material.FIREWORK).setName("§eKangaroo") }, new Integer[] {4}),
	GRAPPLER_RACE(new ItemBuilder[] { new ItemBuilder().setMaterial(Material.LEASH).setName("§eGrappler") }, new Integer[] {4}),
	LAVA_CHALLENGE(new ItemBuilder[] {new ItemBuilder().setMaterial(Material.RED_MUSHROOM).setAmount(64).setBreakable(),
				   new ItemBuilder().setMaterial(Material.BROWN_MUSHROOM).setAmount(64).setBreakable(), 
			       new ItemBuilder().setMaterial(Material.BOWL).setAmount(64).setBreakable()}, new Integer[] {13,14,15}),
	
	SOUP(new ItemBuilder[] {new ItemBuilder().setMaterial(Material.MUSHROOM_SOUP).setName("§eSopa").setBreakable()}, null),
	
	SPECTATOR(new ItemBuilder[] {new ItemBuilder().setMaterial(Material.REDSTONE).setName("§aSair §7(Botão Direito)").setBreakable(), 
			  new ItemBuilder().setMaterial(Material.COMPASS).setName("§aJogadores §7(Botão Direito)").setBreakable(), 
			  new ItemBuilder().setMaterial(Material.SLIME_BALL).setName("§aPreferências §7(Botão Direito)").setBreakable()}, new Integer[] {2,4,6}),
	
	ADMIN(new ItemBuilder[] {}, null);
	
	private ItemBuilder[] items;
	private Integer[] slots;
	
	ItemsCache(ItemBuilder[] items, Integer[] slots) {
		this.items = items;
		this.slots = slots;
	}
	
	public ItemBuilder getItem(int id) {
		return id <= items.length-1 ? items[id] : items[0];
	}
	
	public Integer[] getSlots() {
		return slots;
	}
	
	public void build(Inventory inventory) {
		if (slots != null) {
			int id = 0;
			for (Integer slot : slots) {
				getItem(id).build(inventory, slot);
				id++;
			}
		} else {
			for (int i = 0; i < items.length; i++) {
				getItem(i).build(inventory);
			}
		}
	}
	
	public void build(Player player) {
		if (slots != null) {
			int id = 0;
			for (Integer slot : slots) {
				getItem(id).build(player.getInventory(), slot);
				id++;
			}
		} else {
			for (int i = 0; i < items.length; i++) {
				getItem(i).build(player.getInventory());
			}
		}
	}
	
}