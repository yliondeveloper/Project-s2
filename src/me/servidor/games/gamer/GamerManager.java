package me.servidor.games.gamer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;

public class GamerManager {

	private HashMap<UUID, Gamer> gamers = new HashMap<UUID, Gamer>();
	
	public HashMap<UUID, Gamer> getGamersData() {
		return gamers;
	}
	
	public Collection<Gamer> getGamers() {
		return gamers.values();
	}
	
	public Gamer getGamer(UUID uniqueId) {
		return gamers.get(uniqueId);
	}
	
	public Gamer getGamer(Player player) {
		return gamers.get(player.getUniqueId());
	}
	
	public Gamer addGamer(UUID uniqueId) {
		gamers.put(uniqueId, new Gamer(uniqueId));
		return gamers.get(uniqueId);
	}
	
	public void removeGamer(Player player) {
		gamers.remove(player.getUniqueId());
	}
	
	public boolean isGamer(Player player) {
		return gamers.containsKey(player.getUniqueId());
	}
	
	public boolean isAlive(Player player) {
		return getAliveGamers().contains(gamers.get(player.getUniqueId()));
	}
	
	public List<Gamer> getAliveGamers() {
		List<Gamer> aliveGamers = new ArrayList<Gamer>();
		for (Gamer gamer : gamers.values()) {
			if (gamer.isAlive()) 
				aliveGamers.add(gamer);
		}
		
		return aliveGamers;
	}
	
	public int getAliveGamersAmount() {
		return getAliveGamers().size();
	}
	
}
