package me.servidor.games.managers;

import me.servidor.games.principal.PartyGames;
import me.servidor.games.game.GameManager;
import me.servidor.games.game.GameSelection;
import me.servidor.games.game.games.LavaChallenge;
import me.servidor.games.game.timer.PreGame;
import me.servidor.games.gamer.GamerManager;
import me.servidor.games.gamer.GamerUtils;
import me.servidor.games.utils.Utils;

public class Manager {

	private PartyGames plugin;
	private GamerManager gamerManager;
	private GameManager gameManager;
	private GamerUtils gamerUtils;
	private GameSelection gameSelection;
	private PreGame preGame;
	private Utils utils;
	private PlayerScoreboard playerScoreboard;
	
	private LavaChallenge lavaChallenge;
	
	public Manager() {
		this.plugin = PartyGames.getPlugin(PartyGames.class);
		this.utils = new Utils(this);
		this.gameManager = new GameManager(this);
		this.gamerManager = new GamerManager();
		this.gamerUtils = new GamerUtils();
		this.gameSelection = new GameSelection(this);
		this.preGame = new PreGame(this);
		this.playerScoreboard = new PlayerScoreboard(this);
	}
	
	public PartyGames getPlugin() {
		return plugin;
	}
	
	public GameManager getGameManager() {
		return gameManager;
	}
	
	public GamerManager getGamerManager() {
		return gamerManager;
	}
	
	public GamerUtils getGamerUtils() {
		return gamerUtils;
	}
	
	public GameSelection getGameSeletion() {
		return gameSelection;
	}
	
	public PreGame getPreGame() {
		return preGame;
	}
	
	public Utils getUtils() {
		return utils;
	}
	
	public PlayerScoreboard getPlayerScoreboard() {
		return playerScoreboard;
	}
	
	public LavaChallenge getLavaChallenge() {
		return lavaChallenge;
	}
	
	public void initLavaChallenge() {
		this.lavaChallenge = new LavaChallenge();
	}
	
}
