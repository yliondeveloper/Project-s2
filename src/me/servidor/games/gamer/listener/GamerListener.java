package me.servidor.games.gamer.listener;

import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;

import me.servidor.games.principal.PartyGames;
import me.servidor.games.managers.Messages;

public class GamerListener implements Listener {

	private ReentrantLock reentrantLock = new ReentrantLock();
	
	@EventHandler
	public void onLoadData(AsyncPlayerPreLoginEvent event) {
		UUID uniqueId = event.getUniqueId();

		if (Bukkit.getPlayer(uniqueId) == null) {
			reentrantLock.lock();
			
			PartyGames.getManager().getGamerManager().addGamer(uniqueId).loadProfile();
			PartyGames.getManager().getUtils().sleep();
			
			reentrantLock.unlock();
			return;
		}
		
		event.disallow(Result.KICK_OTHER, Messages.getPrefixOut() + "\n§aDesculpe §f" + event.getName() + "§a, ocorreu um erro ao carregar seus dados!\n§aTente relogar para fixar seus dados corretamene.");
	}
	
}
