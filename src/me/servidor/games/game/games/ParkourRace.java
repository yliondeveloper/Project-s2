package me.servidor.games.game.games;

import java.util.ArrayList;

import org.bukkit.scheduler.BukkitRunnable;

import me.servidor.games.principal.PartyGames;
import me.servidor.games.game.GameManager;
import me.servidor.games.game.timer.GameTime;
import me.servidor.games.gamer.Gamer;
import me.servidor.games.gamer.GamerManager;
import me.servidor.games.gamer.GamerUtils;
import me.servidor.games.item.ItemsCache;
import me.servidor.games.managers.Manager;
import me.servidor.games.managers.Messages;

public class ParkourRace extends GameTime {

	private Manager manager;
	private GamerManager gamerManager;
	private GamerUtils gamerUtils;
	private GameManager gameManager;

	private int playerTime;
	private ArrayList<Gamer> end = new ArrayList<>();

	public ParkourRace() {
		manager = PartyGames.getManager();
		gamerManager = manager.getGamerManager();
		gamerUtils = manager.getGamerUtils();
		gameManager = manager.getGameManager();
	}
	
	public void start() {
		new BukkitRunnable() {
			public void run() {
				initTimer(120);
				
				for (Gamer gamer : gamerManager.getGamers())
					ItemsCache.KANGAROO_RACE.build(gamer.getPlayer());
			}
		}.runTaskLater(manager.getPlugin(), 150L);

		for (int i = 0; i < gamerManager.getAliveGamers().size(); i++) {
			Gamer gamer = gamerManager.getAliveGamers().get(i);

			if (!end.contains(gamer)) {
				gamerUtils.prepareGamer(gamer);
				manager.getUtils().teleportPlayer(gamer.getPlayer(), "star.party.parkour.spawns." + (i+1));
			}
		}	
	}

	@Override
	public void timer() {
		if (getTime() == 0 || end.size() == gamerManager.getAliveGamersAmount())
			Messages.sendResults();
		
		if (end.size() == gamerManager.getAliveGamersAmount()) {
			setCancelled(true);
			new BukkitRunnable() {
				public void run() {
					gameManager.sendWinnersMessage(end);
					return;
				}
			}.runTaskLater(manager.getPlugin(), 60L);
		}

		if (getTime() == -3) {
			gameManager.sendWinnersMessage(end);
			setCancelled(true);
			return;
		}
		
		if (playerTime % 10 == 0 && playerTime != 0 && end.size() == 0)
			gameManager.sendPlacementsMessage(manager.getUtils().getLocationFromConfig("star.party.parkour.arrival"));
		
		updateTimeDown();
		playerTime++;
	}

}
