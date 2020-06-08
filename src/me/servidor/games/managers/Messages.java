package me.servidor.games.managers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Messages {

	/* Pricipal Methods */

	public static String getPrefix() {
		return "§b§lPARTY §7» ";
	}

	public static String getPrefixOut() {
		return "§b§lPARTY §7» ";
	}

	public static String getPrefixTitle() {
		return "§b§lPARTY";
	}

	public static String getWebSite() {
		return "mc-starpvp.tk";
	}

	/* Error Methods ( General ) */

	public static String getWithoutPermissionMessage(Player player) {
		return "\n" + getPrefix() + "§aDesculpe §f" + player.getName() + "§a, não foi possível realizar este comando!\n"
				+ getPrefix() + "§aCausa: Sem permissão de acesso ao comando.\n ";
	}

	public static String getWrongArgumentMessage(String correctUsage, Player player) {
		return "\n" + getPrefix() + "§aDesculpe §f" + player.getName() + "§a, não foi possível realizar este comando!\n"
				+ getPrefix() + "§aCausa: Argumentação incorreta.\n" + getPrefix() + "§aUso correto: §f" + correctUsage
				+ "\n ";
	}

	public static String getPlayerOfflineMessage(String offlineName, Player player) {
		return "\n" + getPrefix() + "§aDesculpe §f" + player.getName() + "§a, não foi possível encontrar o jogador §f"
				+ offlineName + "§a!\n" + getPrefix() + "§aCausa: Jogador desconectado.\n ";
	}

	public static String getCommandOnlyForPlayersMessage(String command) {
		return "\n\n§aDesculpe, não foi possivel realizar o comando §f" + command
				+ "§a!\n§aCausa: Comando apenas para jogadores.\n ";
	}

	/* Broadcast Methods */

	public static void sendBroadcast(String message) {
		sendBroadcastWithBypass(message, null);
	}

	public static void sendResults() {
		Messages.sendBroadcast(
				Messages.getPrefix() + "§aMostrando §7(§fResultados§7) §ados vencedores em 3 segundos...");
	}

	public static void sendBroadcastWithBypass(String message, Player player) {
		for (Player players : Bukkit.getOnlinePlayers())
			if (player != null && players != player)
				players.sendMessage(message);
			else if (player == null)
				players.sendMessage(message);
	}


	public static List<String> formatDescription(String text, int maxLength) {
		List<String> texts = new ArrayList<>();
		String[] split = text.split(" ");
		text = "";

		for (int i = 0; i < split.length; ++i) {
			if (ChatColor.stripColor(text).length() > maxLength || ChatColor.stripColor(text).endsWith(".")
					|| ChatColor.stripColor(text).endsWith("!")) {
				texts.add("§7" + text);

				if (text.endsWith(".") || text.endsWith("!"))
					texts.add("");

				text = "";
			}
			String toAdd = split[i];

			if (toAdd.contains("\n")) {
				toAdd = toAdd.substring(0, toAdd.indexOf("\n"));
				split[i] = split[i].substring(toAdd.length() + 1);
				texts.add("§7" + text + (text.length() == 0 ? "" : " ") + toAdd);
				text = "";
				--i;
			} else {
				text = text + (text.length() == 0 ? "" : " ") + toAdd;
			}
		}
		texts.add("§7" + text);
		return texts;
	}

}
