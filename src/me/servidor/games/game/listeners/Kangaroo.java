package me.servidor.games.game.listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import me.servidor.games.principal.PartyGames;
import me.servidor.games.game.GameManager;
import me.servidor.games.game.GameType;

public class Kangaroo implements Listener {
	
	private GameManager gameManager;
	private List<UUID> using = new ArrayList<UUID>();
	
	public Kangaroo() {
		gameManager = PartyGames.getManager().getGameManager();
	}
	
	@EventHandler
	public void onKangaroo(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		
		if (event.getItem() == null || event.getItem().getType().equals(Material.AIR))
			return;
		
		if (gameManager.getCurrentGame().equals(GameType.KANGAROO_RACE) && event.getAction() != Action.PHYSICAL && event.getItem().getType().equals(Material.FIREWORK)) {
			event.setCancelled(true);
			
			if (using.contains(player.getUniqueId()))
				return;
			
			Vector vector = player.getEyeLocation().getDirection();
			
			if (player.isSneaking()) {
				vector = vector.multiply(1.3f).setY(0.7f);
			} else {
				vector = vector.multiply(0.2f).setY(0.9f);
			}
			
			player.setVelocity(vector);
			using.add(player.getUniqueId());
		}
	}

	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onRemove(PlayerMoveEvent event) {
		if (using.contains(event.getPlayer().getUniqueId()) && (event.getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN).getType() != Material.AIR || event.getPlayer().isOnGround())) {
			using.remove(event.getPlayer().getUniqueId());
		}
	}

	@EventHandler
	public void onCancel(EntityDamageEvent event) {		
		if (event.getEntity() instanceof Player && gameManager.getCurrentGame().equals(GameType.KANGAROO_RACE))
			event.setCancelled(true);
	}
	
}
