package me.servidor.games.game.timer;

import org.bukkit.scheduler.BukkitRunnable;

import me.servidor.games.principal.PartyGames;

public abstract class GameTime {

	private int time;
	private boolean cancel;
	
	public int getTime() {
		return time;
	}
	
	public void setTime(int time) {
		this.time = time;
	}
	
	public void updateTimeDown() {
		time--;
	}
	
	public void updateTimeUp() {
		time++;
	}
	
	public boolean isCancelled() {
		return cancel;
	}
	
	public void setCancelled(boolean bool) {
		this.cancel = bool;
	}
	
	public abstract void timer();
	
	public void initTimer(int time) {
		this.time = time;
		new BukkitRunnable() {
			public void run() {
				
				if (cancel) {
					cancel();
					return;
				}
				
				timer();
			}
		}.runTaskTimer(PartyGames.getManager().getPlugin(), 0L, 20L);
	}
	
	public void initTimer() {
		initTimer(0);
	}
	
}
