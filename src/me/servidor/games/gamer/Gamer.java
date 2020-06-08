package me.servidor.games.gamer;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Gamer {

	private int wins, loses, xp, score;
	private UUID uniqueId;
	
	private boolean isAlive, showing, voted;
	
	public Gamer(UUID uniqueId) {
		this.uniqueId = uniqueId;
	}

	public int getWins() {
		return wins;
	}

	public int getLoses() {
		return loses;
	}

	public int getXP() {
		return xp;
	}

	public int getScore() {
		return score;
	}
	
	public boolean isAlive() {
		return isAlive;
	}
	
	public boolean isShowing() {
		return showing;
	}
	
	public boolean hasVoted() {
		return voted;
	}

	public void addWin() {
		wins++;
	}

	public void addLose() {
		loses++;
	}

	public void addXP(int amount) {
		xp += amount;
	}

	public void removeXP(int amount) {
		xp -= amount;
	}

	public void setXP(int amount) {
		xp = amount;
	}

	public void addScore(int amount) {
		score += amount;
	}
	
	public void setAlive(boolean alive) {
		isAlive = alive;
	}
	
	public void setShowing(boolean showing) {
		this.showing = showing;
	}
	
	public void setVoted(boolean voted) {
		this.voted = voted;
	}

	public UUID getUniqueId() {
		return uniqueId;
	}

	public Player getPlayer() {
		return Bukkit.getPlayer(uniqueId);
	}
	
	public String getName() {
		return getPlayer().getName();
	}
	
	public void loadProfile() {
		this.isAlive = true;
	}

}
