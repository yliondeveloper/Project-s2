package me.servidor.games.game.timer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.servidor.games.game.GameState;
import me.servidor.games.game.GameType;
import me.servidor.games.managers.Manager;
import me.servidor.games.managers.Messages;
import me.servidor.games.utils.Utils.TimeFormatMode;

public class PreGame extends GameTime {

	private Manager manager;

	public PreGame(Manager manager) {
		this.manager = manager;
	}

	@Override
	public void timer() {

		if (getTime() > 0 && (getTime() % 60 == 0 || getTime() % 30 == 0 || (getTime() > 0 && getTime() <= 5))) {
			Messages.sendBroadcast("§aO jogo irá começar em §f"
					+ manager.getUtils().formatTime(getTime(), TimeFormatMode.COMPLETE) + "§a!");
		} else if (getTime() == 0) {

			if (Bukkit.getOnlinePlayers().size() < 2) {
				Messages.sendBroadcast(
						"§aJogadores insuficientes para poder iniciar o jogo! Jogadores mínimos: §f" + 2 + "§a.");
				setTime(180);
				return;
			}

			Messages.sendBroadcast("");
			Messages.sendBroadcast("§aO jogo iniciou, desjamos boa sorte a todos!");
			Messages.sendBroadcast("");
			Messages.sendBroadcast("§f" + manager.getGamerManager().getAliveGamersAmount() + "§a Jogadores jogando.");
			Messages.sendBroadcast("§aEscolhendo MiniGame em §f3 §asegundos...");
			Messages.sendBroadcast("");

			manager.getGameManager().setGameState(GameState.GAME_SELECTION);

			for (Player online : Bukkit.getOnlinePlayers()) {
				manager.getPlayerScoreboard().update(online);
				manager.getUtils().teleportPlayer(online, "GameSelection.Spawn");
			}

		} else if (getTime() == -3) {
			manager.getGameSeletion().chooseMinigame();
			setCancelled(true);
			return;
		}

		if (getTime() > -3)
			updateTimeDown();
	}

	@Override
	public void initTimer() {
		manager.getGameManager().setGameState(GameState.PREGAME);
		manager.getGameManager().setGame(GameType.NONE);
		super.initTimer();
	}

}
