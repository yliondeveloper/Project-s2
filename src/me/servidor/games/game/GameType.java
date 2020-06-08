package me.servidor.games.game;

public enum GameType {
	
	NONE("None", "unnamed", "without description", 10, false),
	KANGAROO_RACE("Corrida de Kangaroo", "§aUtilize seu kangaroo para chegar até o final do percurso, o primeiro que chegar será o vencedor!", "kangaroo", 16, true),
	GRAPPLER_RACE("Corrida de Grappler", "§aUtilize seu grappler para chegar até o final do percurso, o primeiro que chegar será o vencedor! Tenha cuidado com o dano de queda!", "grappler", 16, true),
	PARKOUR_RACE("Corrida Parkour", "§aSeja o primeiro a chegar até o final do percurso para se tornar o vencedor! Atenção, não caia demais, você tem apenas 10 tentativas!", "parkour", 16, false), 
	LAVA_CHALLENGE("Lava Challenge", "§aAguente o máximo possível no alto dano da lava.", "lava", 3, false);
	
	private String name, description, toSetup;
	private Integer maxPositions;
	private Boolean paralyzed;
	
	private GameType(String name, String description, String toSetup, int maxPostions, boolean paralyzed) {
		this.name = name;
		this.description = description;
		this.toSetup = toSetup;
		this.maxPositions = maxPostions;
		this.paralyzed = paralyzed;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDescrption() {
		return description;
	}
	
	public int getMaxPostions() {
		return maxPositions;
	}
	
	public boolean isParalyzed() {
		return paralyzed;
	}
	
	public static boolean containsToSetup(String game) {	
		boolean contains = false;
		
		for (GameType games : values()) {
			if (games.toSetup.equals(game.toLowerCase())) {
				contains = true;
			}
		}
		return contains;
	}
	
	public static GameType getGameFromSetup(String game) {
		for (GameType games : values()) {
			if (games.toSetup.equals(game.toLowerCase())) {
				return games;
			}
		}
		return values()[0];
	}
	
}
