package me.servidor.games.game;

public enum GameState {

	PREGAME("Pré-Game", "pre_game"), GAME_SELECTION("Seleção do Minigame", "game_selection"), GAME("Jogo", "game"),
	END("Final", "end");

	private String name, identification;

	GameState(String name, String identification) {
		this.name = name;
		this.identification = identification;
	}

	public String getName() {
		return name;
	}

	public String getId() {
		return identification;
	}

}
