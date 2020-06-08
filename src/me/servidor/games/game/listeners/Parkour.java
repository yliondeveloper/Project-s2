package me.servidor.games.game.listeners;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import me.servidor.games.principal.PartyGames;
import me.servidor.games.game.GameManager;
import me.servidor.games.game.GameType;
import me.servidor.games.gamer.GamerManager;
import me.servidor.games.gamer.GamerUtils;
import me.servidor.games.managers.Manager;
import me.servidor.games.utils.Utils;
import me.servidor.games.game.listeners.Storage;

public class Parkour implements Listener {

	private GameManager gameManager;
	private GamerManager gamerManager;
	private GamerUtils gamerUtils;
	private Manager manager;
	private Utils utils;

	private Storage<UUID, Integer, Integer> parkourData;

	public Parkour() {
		manager = PartyGames.getManager();
		gameManager = manager.getGameManager();
		gamerManager = manager.getGamerManager();
		gamerUtils = manager.getGamerUtils();
		utils = manager.getUtils();

		parkourData = new Storage<>();
	}

	public int getLastCheckpoint(Player player) {
		return parkourData.getValue(player.getUniqueId());
	}

	public int getAttempts(Player player) {
		return parkourData.getSubValue(player.getUniqueId());
	}

	@EventHandler
	private void onPlayerMove(PlayerMoveEvent event) {
		if (!gameManager.getCurrentGame().equals(GameType.PARKOUR_RACE))
			return;
		
		if (event.getPlayer().getLocation().getBlockY() <= Integer.valueOf(manager.getPlugin().getConfig().getString("star.party.parkour.height"))) {
			Player player = event.getPlayer();
			
			if (gamerManager.isAlive(player)) {
				if (getAttempts(player) == 10) {
					parkourData.remove(player.getUniqueId(), getLastCheckpoint(player), getAttempts(player));
					gamerUtils.addSpectator(gamerManager.getGamer(player));
					manager.getUtils().teleportPlayer(player, "star.party.parkour.Spectator");
					return;
				} else {
					parkourData.put(player.getUniqueId(), getLastCheckpoint(player), getAttempts(player) + 1);
				}
			}
		}

		if (event.getPlayer().getLocation().subtract(0, 1, 0).getBlock().getType().name().contains("PLATE")) {
			Player player = event.getPlayer();
			Location location = player.getLocation().subtract(0, 1, 0);
			Integer nextCheckpoint = getLastCheckpoint(player) + 1;

			Location checkpoint = utils.getLocationFromConfig("star.party.parkour.checkpoint." + nextCheckpoint);

			if (location.getBlockX() == checkpoint.getBlockX() && location.getBlockY() == checkpoint.getBlockY() && location.getBlockZ() == checkpoint.getBlockZ()) {
				parkourData.put(player.getUniqueId(), nextCheckpoint, getAttempts(player));
				player.sendMessage("§aVocê passou no §f" + nextCheckpoint + "º §acheckpoint!");
			}
		}
	}

}
