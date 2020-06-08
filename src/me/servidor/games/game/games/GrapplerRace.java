package me.servidor.games.game.games;

import java.util.ArrayList;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
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
import me.servidor.games.game.games.ItemBuilder;

public class GrapplerRace extends GameTime {

	private Manager manager;
	private GamerManager gamerManager;
	private GamerUtils gamerUtils;
	private GameManager gameManager;

	private int playerTime;
	private ArrayList<Gamer> end = new ArrayList<>();

	public GrapplerRace() {
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
					ItemsCache.GRAPPLER_RACE.build(gamer.getPlayer());
			}
		}.runTaskLater(manager.getPlugin(), 150L);

		for (int i = 0; i < gamerManager.getAliveGamers().size(); i++) {
			Gamer gamer = gamerManager.getAliveGamers().get(i);

			if (!end.contains(gamer)) {
				gamerUtils.prepareGamer(gamer);
				manager.getUtils().teleportPlayer(gamer.getPlayer(), "starpvp.party.grappler.spawns." + (i + 1));
			}
		}

		/* Check Gamers */

		new BukkitRunnable() {
			public void run() {

				if (isCancelled()) {
					cancel();
					return;
				}

				for (Gamer gamers : gamerManager.getGamers()) {
					if (gamers.isAlive() && !end.contains(gamers) && gamers.getPlayer().getLocation().add(0, -1, 0)
							.getBlock().getType().equals(Material.DIAMOND_BLOCK)) {

						gamerUtils.prepareGamer(gamers);

						ItemStack chestplate = null;

						if (end.size() > 0) {
							chestplate = new ItemBuilder().setColor(Material.LEATHER_CHESTPLATE, Color.YELLOW,
									"§aPeitoral");
							Messages.sendBroadcast("§aO jogador §f" + gamers.getName() + "§a terminou a corrida §e("
									+ playerTime + " Segundos§e)");
						} else {
							Messages.sendBroadcast("§aO jogador §f" + gamers.getName()
									+ "§a foi o primeiro a terminar a corrida §e(" + playerTime + " Segundos§e)");
							Messages.sendBroadcast("§aA corrida será finalizada em §f30 §asegundos!");
							setTime(30);
							chestplate = new ItemBuilder().setColor(Material.LEATHER_CHESTPLATE, Color.GREEN,
									"§aPeitoral do vencedor!");
						}

						gamers.getPlayer().getInventory().setChestplate(chestplate);
						end.add(gamers);
					}
					gamers.getPlayer().setLevel(getTime());
				}
			}
		}.runTaskTimer(manager.getPlugin(), 100L, 1L);

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
			gameManager
					.sendPlacementsMessage(manager.getUtils().getLocationFromConfig("starpvp.party.grappler.arrival"));

		updateTimeDown();
		playerTime++;
	}

}
