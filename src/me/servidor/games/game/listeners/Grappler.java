package me.servidor.games.game.listeners;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import me.servidor.games.principal.PartyGames;
import me.servidor.games.game.GameManager;
import me.servidor.games.game.GameType;
import me.servidor.games.gamer.Gamer;
import me.servidor.games.gamer.GamerManager;
import me.servidor.games.gamer.GamerUtils;
import me.servidor.games.managers.Manager;
import me.servidor.games.managers.Messages;
import me.servidor.games.utils.HookUtil;

public class Grappler implements Listener {
	
	private Manager manager;
	private GameManager gameManager;
	private GamerManager gamerManager;
	private GamerUtils gamerUtils;
	private HashMap<UUID, HookUtil> hooks = new HashMap<UUID, HookUtil>();
	
	public Grappler() {
		manager = PartyGames.getManager();
		gameManager = manager.getGameManager();
		gamerUtils = manager.getGamerUtils();
		gamerManager = manager.getGamerManager();
	}

	@EventHandler
	private void onGrappler(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		
		if (manager.getGameManager().getCurrentGame().equals(GameType.GRAPPLER_RACE) && player.getItemInHand().getType().equals(Material.LEASH)) {
			event.setCancelled(true);
			
			Location location1 = player.getLocation();
			
			if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
				
				if (hooks.containsKey(player.getUniqueId()))
					hooks.get(player.getUniqueId()).remove();
				
				Vector direction = location1.getDirection();	
				HookUtil nms = new HookUtil(player.getWorld(), ((CraftPlayer) player).getHandle());
				
				nms.spawn(player.getEyeLocation().add(direction.getX(), direction.getY(), direction.getZ()));
				nms.move(5 * direction.getX(), 5 * direction.getY(), 5 * direction.getZ());
				
				hooks.put(player.getUniqueId(), nms);
				
			} else if (hooks.containsKey(player.getUniqueId()) && hooks.get(player.getUniqueId()).isHooked()) {
				
				Location location2 = hooks.get(player.getUniqueId()).getBukkitEntity().getLocation();

				double distance = location2.distance(location1);
				double vectorX = (1 + 0.07 * distance) * (location2.getX() - location1.getX()) / distance;
				double vectorY = (1 + 0.03 * distance) * (location2.getY() - location1.getY()) / distance;
				double vectorZ = (1 + 0.07 * distance) * (location2.getZ() - location1.getZ()) / distance;

				player.setVelocity(new Vector(vectorX, vectorY, vectorZ));
				
			}
		}
	}
	
	@EventHandler
	private void onRemoveLeash(PlayerItemHeldEvent event) {
		if (hooks.containsKey(event.getPlayer().getUniqueId())) {
			hooks.get(event.getPlayer().getUniqueId()).remove();
			hooks.remove(event.getPlayer().getUniqueId());
		}
	}

	@EventHandler
	public void onCancel(EntityDamageEvent event) {		
		if (event.getEntity() instanceof Player && manager.getGameManager().getCurrentGame().equals(GameType.KANGAROO_RACE) && !event.getCause().equals(DamageCause.FALL)) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	private void onPlayerDeath(PlayerDeathEvent event) {
		if (!gameManager.getCurrentGame().equals(GameType.GRAPPLER_RACE))
			return;
		
		Player died = event.getEntity();
		Gamer gamer = manager.getGamerManager().getGamer(died);
		
		new BukkitRunnable() {
			public void run() {
				died.spigot().respawn();
				gamerUtils.addSpectator(gamer);
				manager.getUtils().teleportPlayer(died, "star.party.grappler.spectator");
			}
		}.runTaskLater(manager.getPlugin(), 1L);
		
		Messages.sendBroadcast("§aO jogador §f" + died.getName() + "§a foi eliminado!");
		Messages.sendBroadcast("§f" + gamerManager.getAliveGamersAmount() + "§a Jogadores restantes.");	
	}
	
}
