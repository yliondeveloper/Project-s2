package me.servidor.games.game;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.servidor.games.game.games.GrapplerRace;
import me.servidor.games.game.games.KangarooRace;
import me.servidor.games.game.timer.GameTime;
import me.servidor.games.gamer.Gamer;
import me.servidor.games.gamer.GamerManager;
import me.servidor.games.managers.Manager;
import me.servidor.games.managers.Messages;

public class GameSelection {

	private Manager manager;
	private GameManager gameManager;
	private GamerManager gamerManager;
	private GameTime gameTime;

	public GameSelection(Manager manager) {
		this.gameManager = manager.getGameManager();
		this.gamerManager = manager.getGamerManager();
		this.manager = manager;
	}

	public GameTime getGameTime() {
		return gameTime;
	}

	public void nextGame() {
		if (gameManager.getGames().size() > 0) {

			for (Player players : Bukkit.getOnlinePlayers()) {
				manager.getUtils().teleportPlayer(players, "star.party.spawn");
			}

			Messages.sendBroadcast(Messages.getPrefix() + "§aEscolhendo o próximo jogo em §f8 §asegundos...");

			gameManager.setGameState(GameState.GAME_SELECTION);

			new GameTime() {
				@Override
				public void timer() {
					if (getTime() == 0) {
						chooseMinigame();
						setCancelled(true);
						return;
					}
					updateTimeDown();
				}
			}.initTimer(8);
		}
	}

	public void chooseMinigame() {
		GameType game = gameManager.chooseGame();

		gameManager.setGameState(GameState.GAME);
		gameManager.setGame(game);
		gameManager.setNextGame(null);
		gameManager.removeGame(game);

		String title = "§7¡¡¡¡¡¡¡¡¡¡¡¡ §e§l§n" + game.getName().toUpperCase() + "§7 ¡¡¡¡¡¡¡¡¡¡¡¡";

		Messages.sendBroadcast("");
		Messages.sendBroadcast(title);
		Messages.sendBroadcast("");
		Messages.sendBroadcast(" §aComo Jogar:");
		Messages.sendBroadcast("");

		for (String text : Messages.formatDescription(game.getDescrption(), 40))
			Messages.sendBroadcast("§7   " + text);

		String end = "";
		for (int i = 0; i < game.getName().length(); i++)
			end += "¡";

		Messages.sendBroadcast("");
		Messages.sendBroadcast("§7¡¡¡¡¡¡¡¡¡¡¡¡" + end + "¡¡¡¡¡¡¡¡¡¡¡¡");

		if (Messages.formatDescription(game.getDescrption(), 40).size() <= 2)
			Messages.sendBroadcast("");

		for (Gamer gamers : gamerManager.getGamers()) {
			gamers.setVoted(false);
			gamers.setAlive(true);
		}

		if (game.equals(GameType.KANGAROO_RACE)) {
			new KangarooRace().start();
		} else if (game.equals(GameType.GRAPPLER_RACE)) {
			new GrapplerRace().start();
		} else if (game.equals(GameType.LAVA_CHALLENGE)) {
			manager.initLavaChallenge();
			manager.getLavaChallenge().start();
		}

		gameTime = new GameTime() {
			String message = "§c§l3...";

			public void timer() {

				if (getTime() <= 3) {
					if (getTime() == 2) {
						message = "§6§l2...";
					} else if (getTime() == 1) {
						message = "§e§l1...";
					} else if (getTime() == 0) {
						message = "§a§lBOA SORTE!";
					}

					Messages.sendBroadcast(Messages.getPrefix() + message);
				}

				if (getTime() == 0)
					setCancelled(true);

				updateTimeDown();
			}
		};
		gameTime.initTimer(7);
	}

}
