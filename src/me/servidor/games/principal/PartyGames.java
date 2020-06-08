package me.servidor.games.principal;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import me.servidor.games.loader.Loader;
import me.servidor.games.managers.Manager;

public class PartyGames extends JavaPlugin {
	
	private static Manager manager;
	
	@Override
	public void onEnable() {	
		manager = new Manager();
		
		new Loader().load();
		
		Bukkit.getWorlds().get(0).setAutoSave(false);
	
		manager.getPreGame().initTimer();
		manager.getUtils().createBox(new String[] { "(- Servidor - PartyGames -)", "Iniciado com sucesso!" });
	}
	
	public static Manager getManager() {
		return manager;
	}
	
}
