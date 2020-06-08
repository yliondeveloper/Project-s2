package me.servidor.games.managers;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.servidor.games.game.GameManager;
import me.servidor.games.game.GameState;
import me.servidor.games.game.timer.GameTime;
import me.servidor.games.gamer.Gamer;
import me.servidor.games.utils.SortMapByValue;
import me.servidor.games.utils.Utils.TimeFormatMode;
import me.servidor.games.managers.PartyScore;

public class PlayerScoreboard extends GameTime {
	
	private Manager manager;
	private GameManager gameManager;
	
	public PlayerScoreboard(Manager manager) {
		this.manager = manager;
		this.gameManager = manager.getGameManager();
	}
	
	public void update(Player player) {
		
		PartyScore score = null;
		GameState state = gameManager.getGameState();

		if (state.equals(GameState.PREGAME)) {	
			
			score = new PartyScore(player, "§b§lPARTY", state.getId());
			
			score.createLine("§a§1");
			score.createLine("  §fIniciando em", ": §a" + manager.getUtils().formatTime(manager.getPreGame().getTime(), TimeFormatMode.INCOMPLETE));
			score.createLine("  §fJogadores: §a" + manager.getGamerManager().getAliveGamersAmount() + "/" + Bukkit.getOnlinePlayers().size());
			score.createLine("§c§3");
			score.createLine("  §fRank: §7(§f-§7) §7Unranked");
			score.createLine("§d§4");
			score.createLine("  §ewww.mc-starpvp.com.br");
			
		} else if (state.equals(GameState.GAME_SELECTION)) {	
			
			score = new PartyScore(player, "§b§lPARTY", state.getId());
			
			score.createLine("§a§1");
			score.createLine("  §aEscolhendo Game...");
			score.createLine("§b§2");
			score.createLine("  §6Pontuação:");
			score.createLine("§c§3");
			
			score.createLine("  §a1º§f - " + getGamer(0).getName() + " §a" + getGamer(0).getScore());
			
			if (getGamersScorePositions().size() > 1)
				score.createLine("  §62º§f - " + getGamer(1).getName() + " §a" + getGamer(1).getScore());
			
			if (getGamersScorePositions().size() > 2)
				score.createLine("  §c3º§f - " + getGamer(2).getName() + " §a" + getGamer(2).getScore());
			
			score.createLine("§d§4");
			score.createLine("  §fJogadores: §a" + manager.getGamerManager().getAliveGamersAmount());
			score.createLine("  §fEspectadores: §a" + manager.getGamerManager().getAliveGamersAmount() + "/" + Bukkit.getOnlinePlayers().size());
			score.createLine("§e§5");
			score.createLine("  §fRank: §7(§f-§7) §7Unranked");
			score.createLine("§f§6");
			score.createLine("  §ewww.mc-starpvp.com.br");
			
		} else if (state.equals(GameState.GAME)) {				
			score = new PartyScore(player, "§b§lPARTY", state.getId());
			
			score.createLine("§a§1");
			score.createLine("  §6Pontuação:");
			score.createLine("§b§2");
			
			score.createLine("  §a1º§f - " + getGamer(0).getName() + " §a" + getGamer(0).getScore());
			
			if (getGamersScorePositions().size() > 1)
				score.createLine("  §62º§f - " + getGamer(1).getName() + " §a" + getGamer(1).getScore());
			
			if (getGamersScorePositions().size() > 2)
				score.createLine("  §c3º§f - " + getGamer(2).getName() + " §a" + getGamer(2).getScore());
			
			score.createLine("§c§3");
			score.createLine("  §fJogadores: §a" + manager.getGamerManager().getAliveGamersAmount() + "/" + Bukkit.getOnlinePlayers().size());
			score.createLine("  §fEspectadores: §a" + (Bukkit.getOnlinePlayers().size()-manager.getGamerManager().getAliveGamersAmount()) + "/" + Bukkit.getOnlinePlayers().size());
			score.createLine("§d§4");
			score.createLine("  §fRank: §7(§f-§7) §7Unranked");
			score.createLine("§e§5");
			score.createLine("  §ewww.mc-starpvp.com.br");
		}
		
		score.update();	
	}
	
	private Gamer getGamer(int id) {
		return getGamersScorePositions().get(id);
	}
	
	public ArrayList<Gamer> getGamersScorePositions() {	
		HashMap<Gamer, Double> scores = new HashMap<Gamer, Double>();
		
		for (Gamer gamers : manager.getGamerManager().getGamers())
			scores.put(gamers, (double) gamers.getScore());

		return new ArrayList<Gamer>(new SortMapByValue().sortByComparator(scores, false).keySet());	
	}

	@Override
	public void timer() {
		for (Player online : Bukkit.getOnlinePlayers())
			update(online);
	}
	
}
