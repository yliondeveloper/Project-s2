package me.servidor.games.game.listeners;

import java.util.Collections;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.servidor.games.principal.PartyGames;
import me.servidor.games.game.GameType;
import me.servidor.games.game.games.LavaChallenge;
import me.servidor.games.gamer.Gamer;
import me.servidor.games.gamer.GamerManager;
import me.servidor.games.gamer.GamerUtils;
import me.servidor.games.managers.Manager;
import me.servidor.games.managers.Messages;

public class LavaChallenger implements Listener {
	
	private Manager manager;
	private GamerManager gamerManager;
	private GamerUtils gamerUtils;
	private LavaChallenge lavaChallenge;

	public LavaChallenger() {
		this.manager = PartyGames.getManager();
		this.gamerManager = manager.getGamerManager();
		this.gamerUtils = manager.getGamerUtils();
		this.lavaChallenge = new LavaChallenge();
	}

	@EventHandler
	private void onPlayerDeath(PlayerDeathEvent event) {	
		event.setDeathMessage(null);

		if (!manager.getGameManager().getCurrentGame().equals(GameType.LAVA_CHALLENGE))
			return;
		
		Player died = event.getEntity();
		Gamer gamerDied = gamerManager.getGamer(died);
		
		new BukkitRunnable() {
			public void run() {
				died.spigot().respawn();
				gamerUtils.addSpectator(gamerDied);
				manager.getUtils().teleportPlayer(died, "star.party.lava.spectator");
			}
		}.runTaskLater(manager.getPlugin(), 1L);
		
		lavaChallenge.addLoser(gamerDied);
		
		Messages.sendBroadcast("§aO player §f" + died.getName() + "§a foi eliminado! §e(" + lavaChallenge.getTime() + " Segundos§e)");
		
		if (gamerManager.getAliveGamersAmount() > 0) {
			Messages.sendBroadcast("§f" + gamerManager.getAliveGamersAmount() + "§a Jogadores restantes.");
			lavaChallenge.setTime(lavaChallenge.getTime() + 3);
		} else {
			Messages.sendResults();
			lavaChallenge.setCancelled(true);
		}
		
		if ((lavaChallenge.getEndTime() != 0 && lavaChallenge.getTime() == lavaChallenge.getEndTime()) || gamerManager.getAliveGamersAmount() == 0) {
			Collections.reverse(lavaChallenge.getLosers());		
			manager.getGameManager().sendWinnersMessage(lavaChallenge.getLosers());
		}
		
	}
	
}
