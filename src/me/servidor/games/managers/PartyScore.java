package me.servidor.games.managers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class PartyScore {

	private String type, title;
	private Player player;
	private List<String> keys, values;

	public PartyScore(Player player, String title, String type) {
		this.player = player;
		this.title = title;

		this.keys = new ArrayList<>();
		this.values = new ArrayList<>();

		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public List<String> getKeys() {
		return keys;
	}

	public List<String> getValues() {
		return values;
	}

	public void createLine(Object type, Object value) {
		keys.add(String.valueOf(type));
		values.add(String.valueOf(value));
	}

	public void createLine(String text) {
		createLine(text.substring(0, text.length() / 2), text.substring(text.length() / 2));
	}

	public void update() {
		if (player.getScoreboard().getObjective(DisplaySlot.SIDEBAR) == null || !player.getScoreboard().getObjective(DisplaySlot.SIDEBAR).getName().equalsIgnoreCase(type)) {
			Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
			Objective object = board.registerNewObjective(type, type);

			object.setDisplayName(title);
			object.setDisplaySlot(DisplaySlot.SIDEBAR);

			int score = 1;

			for (int id = keys.size() - 1; id >= 0; id--) {
				String key = keys.get(id);

				if (error(key, id))
					return;

				object.getScore(key).setScore(score++);
				board.registerNewTeam(key).addEntry(key);
			}

			player.setScoreboard(board);
		}

		Scoreboard board = player.getScoreboard();

		board.getObjective(DisplaySlot.SIDEBAR).setDisplayName(title);

		for (int id = keys.size() - 1; id >= 0; id--) {
			String key = keys.get(id);

			if (error(key, id) || error(values.get(id), id))
				return;

			board.getTeam(key).setSuffix(values.get(id));
		}
	}

	private boolean error(String text, int line) {
		if (text.length() <= 16)
			return false;

		System.out.println("Faanezinho(error) - Scoreboard with more than 32 characters (Line " + (line + 1) + ").");
		return true;
	}

}
