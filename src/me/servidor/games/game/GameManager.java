package me.servidor.games.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import me.servidor.games.gamer.Gamer;
import me.servidor.games.managers.Manager;
import me.servidor.games.managers.Messages;
import me.servidor.games.utils.SortMapByValue;

public class GameManager {

	private Manager manager;
	private ArrayList<GameType> games = new ArrayList<GameType>();

	private GameType currentGame, nextGame;
	private GameState gameState;

	public GameManager(Manager manager) {
		this.manager = manager;
	}

	public ArrayList<GameType> getGames() {
		return games;
	}

	public void addGame(GameType game) {
		games.add(game);
	}

	public void removeGame(GameType game) {
		games.remove(game);
	}

	public void setGame(GameType game) {
		this.currentGame = game;
	}

	public GameType getCurrentGame() {
		return currentGame;
	}

	public GameState getGameState() {
		return gameState;
	}

	public GameType chooseGame() {
		return nextGame == null ? games.get(new Random().nextInt(games.size())) : nextGame;
	}

	public void setNextGame(GameType game) {
		this.nextGame = game;
	}

	public void setGameState(GameState gameState) {
		this.gameState = gameState;
	}

	public String getPlacementColor(int placement) {
		if (placement == 0)
			return "§a";
		if (placement == 1)
			return "§6";

		return "§e";
	}

	public int getPlacementScore(int placement) {
		if (placement == 0)
			return 4;
		if (placement == 1)
			return 3;
		if (placement == 2)
			return 2;

		return 1;
	}

	public ArrayList<Gamer> getGamersPlacements(Location location) {
		HashMap<Gamer, Double> distance = new HashMap<Gamer, Double>();

		for (Gamer gamers : manager.getGamerManager().getAliveGamers())
			distance.put(gamers, gamers.getPlayer().getLocation().distance(location));

		return new ArrayList<Gamer>(new SortMapByValue().sortByComparator(distance, true).keySet());
	}

	public void sendPlacementsMessage(Location location) {
		Messages.sendBroadcast("");
		Messages.sendBroadcast(Messages.getPrefix() + "§e§lCOLOCAÇÕES:");
		Messages.sendBroadcast(Messages.getPrefix() + "§a1º " + getGamersPlacements(location).get(0).getName());

		if (getGamersPlacements(location).size() > 1)
			Messages.sendBroadcast(Messages.getPrefix() + "§62º " + getGamersPlacements(location).get(1).getName());

		if (getGamersPlacements(location).size() > 2)
			Messages.sendBroadcast(Messages.getPrefix() + "§c3º " + getGamersPlacements(location).get(2).getName());

		Messages.sendBroadcast("");
	}

	public void sendWinnersMessage(List<Gamer> winners) {
		Messages.sendBroadcast("");
		Messages.sendBroadcast(Messages.getPrefix() + "§aLista dos vencedores:");
		Messages.sendBroadcast("");

		for (int id = 0; id < winners.size(); id++) {
			Gamer gamer = winners.get(id);
			Integer score = getPlacementScore(id);

			gamer.addScore(score);
			Messages.sendBroadcast(Messages.getPrefix() + getPlacementColor(id) + (id + 1) + "º " + gamer.getName()
					+ " §a+ §f" + score);
		}

		Messages.sendBroadcast("");

		new BukkitRunnable() {
			public void run() {
				manager.getGameSeletion().nextGame();
			}
		}.runTaskLater(manager.getPlugin(), 40L);
	}

}
