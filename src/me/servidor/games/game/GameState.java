package me.servidor.games.game;

public enum GameState {

	PREGAME("Pr�-Game", "pre_game"), GAME_SELECTION("Sele��o do Minigame", "game_selection"), GAME("Jogo", "game"),
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
