package me.servidor.games.game.games;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import me.servidor.games.principal.PartyGames;
import me.servidor.games.game.timer.GameTime;
import me.servidor.games.gamer.Gamer;
import me.servidor.games.gamer.GamerManager;
import me.servidor.games.gamer.GamerUtils;
import me.servidor.games.item.ItemsCache;
import me.servidor.games.managers.Manager;
import me.servidor.games.utils.Utils;

public class LavaChallenge extends GameTime implements Listener {

	private Manager manager;
	private GamerManager gamerManager;
	private GamerUtils gamerUtils;
	private Utils utils;
	
	private static ArrayList<Gamer> losers = new ArrayList<>();
	private static int endTime;
	
	public LavaChallenge() {
		manager = PartyGames.getManager();
		gamerManager = manager.getGamerManager();
		gamerUtils = manager.getGamerUtils();
		utils = manager.getUtils();
	}
	
	public void start() {	
		new BukkitRunnable() {
			public void run() {
				initTimer();
				
				for (Block blocks : utils.getBlocksFromTwoPoints(utils.getLocationFromConfig("star.party.lava.spawns.2"), utils.getLocationFromConfig("star.party.lava.spawns.3"))) {
					if (blocks.getType().equals(Material.GLASS))
						blocks.setType(Material.AIR);
				}
			}
		}.runTaskLater(manager.getPlugin(), 150L);

		for (Gamer gamer : gamerManager.getGamers()) {
			gamerUtils.prepareGamer(gamer);
			ItemsCache.LAVA_CHALLENGE.build(gamer.getPlayer());
			utils.fillInventory(gamer.getPlayer(), ItemsCache.SOUP, 36);
			utils.teleportPlayer(gamer.getPlayer(), "star.party.lava.spawns.1");
		}
	}
	
	public void updateEndTime() {
		endTime++;
	}
	
	public int getEndTime() {
		return endTime;
	}
	
	public ArrayList<Gamer> getLosers() {
		return losers;
	}
	
	public void addLoser(Gamer gamer) {
		losers.add(gamer);
	}

	@Override
	public void timer() {	
		
		if (gamerManager.getAliveGamersAmount() == 0) {
			setCancelled(true);
			return;
		}
		
		for (Gamer gamer : gamerManager.getGamers())
			gamer.getPlayer().setLevel(getTime());
		
		updateTimeUp();
	}

}
