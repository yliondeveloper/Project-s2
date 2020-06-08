package me.servidor.games.loader;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.event.Listener;

import me.servidor.games.principal.PartyGames;
import me.servidor.games.commands.manager.PartyCommand;
import me.servidor.games.game.GameType;
import me.servidor.games.managers.Manager;
import me.servidor.games.utils.ClassGetter;

public class Loader {
	
	private Manager manager;
	
	public Loader() {
		manager = PartyGames.getManager();
	}
	
	private void loadGames() {
		for (GameType games : GameType.values()) {		
			if (!games.equals(GameType.NONE))
				manager.getGameManager().addGame(games);
		}
	}
	

	public void load() {
		
		loadGames();
		
		manager.getPlayerScoreboard().initTimer();
		
		
		for (Class<?> classes : ClassGetter.getClassesForPackage(manager.getPlugin(), "me.servidor.games")) {
			

			
			try {
				if (!classes.getName().contains("$") && Listener.class.isAssignableFrom(classes)) {
					Bukkit.getPluginManager().registerEvents((Listener) classes.newInstance(), manager.getPlugin());
				}
			} catch (Exception exception) {
				manager.getUtils().log("Nao foi possivel carregar o listener " + classes.getSimpleName() + "! Causa: " + exception);
			}
			
			try {
				if (PartyCommand.class.isAssignableFrom(classes) && !classes.getSimpleName().equals("PartyCommand")) {
					PartyCommand command = (PartyCommand) classes.newInstance();
					((CraftServer) Bukkit.getServer()).getCommandMap().register(command.getName(), command);
				}
			} catch (Exception exception) {
				manager.getUtils().log("Nao foi possivel carregar o comando " + classes.getSimpleName() + "! Causa: " + exception);
			}
			
		}
	}
	
}
