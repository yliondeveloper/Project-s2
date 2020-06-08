package me.servidor.games.gamer;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import me.servidor.games.principal.PartyGames;
import me.servidor.games.item.ItemsCache;

public class GamerUtils {
	
	public void removePottionEffects(Gamer gamer) {
		for (PotionEffect effects : gamer.getPlayer().getActivePotionEffects()) {
			gamer.getPlayer().removePotionEffect(effects.getType());
		}
	}

	public void prepareGamer(Gamer gamer) {	
		Player player = gamer.getPlayer();
		
		player.getInventory().clear();
		player.getInventory().setArmorContents(null);
		player.setGameMode(GameMode.ADVENTURE);
		player.setAllowFlight(false);
		player.setHealth(20.0D);
		player.setFoodLevel(20);
		player.setFireTicks(0);
		player.setLevel(0);
		
		removePottionEffects(gamer);	
	}
	
	public void addSpectator(Gamer gamer) {		
		Player player = gamer.getPlayer();
		
		prepareGamer(gamer);
		
		player.setAllowFlight(true);
		
		ItemsCache.SPECTATOR.build(player);
		
		gamer.setAlive(false);
		
		for (Gamer gamers : PartyGames.getManager().getGamerManager().getGamers()) {
			if (gamers.isAlive()) {
				gamers.getPlayer().hidePlayer(player);
			} else if (gamer.isShowing()) {
				gamer.getPlayer().showPlayer(player);
			} else {
				gamer.getPlayer().hidePlayer(player);
			}
		}
		
	}
	
}
