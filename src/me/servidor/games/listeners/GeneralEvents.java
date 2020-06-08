package me.servidor.games.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import me.servidor.games.principal.PartyGames;
import me.servidor.games.game.GameManager;
import me.servidor.games.game.GameState;
import me.servidor.games.game.GameType;
import me.servidor.games.gamer.Gamer;
import me.servidor.games.managers.Manager;
import me.servidor.games.managers.Messages;
import me.servidor.games.utils.Utils.TimeFormatMode;

public class GeneralEvents implements Listener {

	private Manager manager;
	private GameManager gameManager;
	
	public GeneralEvents() {
		manager = PartyGames.getManager();
		gameManager = manager.getGameManager();
	}
	
	@EventHandler
	private void onPlayerLogin(PlayerLoginEvent event) {
		
		Player player = event.getPlayer();
		
		if (event.getResult().equals(Result.KICK_FULL) && player.hasPermission("partyGames.event.joinfull"))
			event.disallow(Result.KICK_FULL, Messages.getPrefixOut() + "\n§aServer Cheio\n§aCompre §7(§fVIP§7) §apara conseguir\n§auma vaga sempre reservada.\n§f" + Messages.getWebSite());
		
		if (event.getResult().equals(Result.KICK_WHITELIST) && player.hasPermission("partyGames.event.maintenance"))
			event.disallow(Result.KICK_FULL, Messages.getPrefixOut() + "\n§aServer em Manutenção\n§aVoltaremos o mais breve possivel.\n§f" + Messages.getWebSite());
		
		if (!gameManager.getGameState().equals(GameState.PREGAME) && player.hasPermission("partyGames.cmd.admin") )
			event.disallow(Result.KICK_FULL, Messages.getPrefixOut() + "\n§aA partida já iniciou!\n§aTente algum outro ip do nossa rede.\n§f" + Messages.getWebSite());
		
	}
	
	@EventHandler
	private void onPlayerJoin(PlayerJoinEvent e) {
		e.setJoinMessage(null);
		
		Player player = e.getPlayer();
		Gamer gamer = manager.getGamerManager().getGamer(player);
		
		if (gameManager.getGameState().equals(GameState.PREGAME)) {
			manager.getGamerUtils().prepareGamer(gamer);
			
			player.sendMessage(" ");
			player.sendMessage("§aBem-vindo §a" + player.getName() + "§a.");
			player.sendMessage(" ");
			player.sendMessage("§aEstamos com §f" + Bukkit.getOnlinePlayers().size() + "§a jogadores conectados a esse servidor.");
			player.sendMessage("§aO jogo irá iniciar em §f" + manager.getUtils().formatTime(manager.getPreGame().getTime(), TimeFormatMode.COMPLETE) + "§a.");
			player.sendMessage(" ");
			
			player.teleport(player.getWorld().getSpawnLocation());
			
			Messages.sendBroadcastWithBypass("§f" + player.getName() + "§a entrou no jogo §7(§f" + Bukkit.getOnlinePlayers().size() + "§7/§f" + Bukkit.getMaxPlayers() + "§7)", player);
			
		} else {
			manager.getGamerUtils().addSpectator(gamer);
		}		
	}
	
	@EventHandler
	private void onPlayerInteract(PlayerInteractEvent event) {
		
		Player player = event.getPlayer();
		ItemStack item = player.getItemInHand();
		
		if (player.getItemInHand() == null)
			return;
		
		if (item.getType().equals(Material.MUSHROOM_SOUP) && event.getAction().name().contains("RIGHT")) {
			event.setCancelled(true);
			Damageable player2 = player;
			if (player2.getHealth() != player2.getMaxHealth()) {
				player.setHealth((player2.getHealth() + 7.0 > player2.getMaxHealth()) ? player2.getMaxHealth() : (player2.getHealth() + 7.0));
				player.getItemInHand().setType(Material.BOWL);
			}
		}
	}
	
	@EventHandler
	private void onPlayerMove(PlayerMoveEvent event) {	
		if (manager.getGameManager().getCurrentGame() != null && manager.getGameManager().getCurrentGame().isParalyzed() && manager.getGameSeletion().getGameTime() != null && !manager.getGameSeletion().getGameTime().isCancelled()) {
			if (event.getFrom().getX() != event.getTo().getX() || event.getFrom().getY() != event.getTo().getY() || event.getFrom().getZ() != event.getTo().getZ()) {
				event.getPlayer().teleport(event.getFrom().setDirection(event.getTo().getDirection()));
			}
		}
	}
	
	@EventHandler
	private void onPlayerDrop(PlayerDropItemEvent event) {
		ItemStack item = event.getItemDrop().getItemStack();
		
		if (item.hasItemMeta() && !item.getType().equals(Material.MUSHROOM_SOUP) && !item.getType().equals(Material.BOWL))
			event.setCancelled(true);
	}
	
	@EventHandler
	private void onEntityDamage(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {

			
			if (gameManager.getGameState().equals(GameState.PREGAME) || gameManager.getGameState().equals(GameState.GAME_SELECTION))
				event.setCancelled(true);

			
			if (!manager.getGamerManager().getGamer(event.getEntity().getUniqueId()).isAlive())
				event.setCancelled(true);

			
			if (gameManager.getCurrentGame().equals(GameType.KANGAROO_RACE) || gameManager.getCurrentGame().equals(GameType.GRAPPLER_RACE))
				event.setCancelled(true);
			
		}
	}
	
	@EventHandler
	private void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Player) {
			if (!manager.getGamerManager().getGamer(event.getDamager().getUniqueId()).isAlive())
				event.setCancelled(true);		
		}
		if (event.getEntity() instanceof Player) {
			if (!manager.getGamerManager().getGamer(event.getEntity().getUniqueId()).isAlive())
				event.setCancelled(true);		
		}
	}


	@EventHandler
	private void onPlayerPickupItem(PlayerPickupItemEvent event) {
		Player player = event.getPlayer();
		
		if (!manager.getGamerManager().getGamer(player).isAlive()) {
			event.setCancelled(true);
			return;
		}
	}
	
}
