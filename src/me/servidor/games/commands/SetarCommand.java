package me.servidor.games.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.servidor.games.principal.PartyGames;
import me.servidor.games.commands.manager.PartyCommand;
import me.servidor.games.game.GameType;
import me.servidor.games.managers.Manager;
import me.servidor.games.managers.Messages;
import me.servidor.games.utils.Utils;

public class SetarCommand extends PartyCommand {

	private Manager manager;
	private Utils utils;

	public SetarCommand() {
		super("setar");

		manager = PartyGames.getManager();
		utils = manager.getUtils();
	}

	@Override
	public boolean execute(CommandSender sender, String label, String[] args) {

		if (!isPlayer(sender) || !hasPermission(sender, "setar"))
			return true;

		Player player = (Player) sender;

		if (args.length == 3) {
			if (GameType.containsToSetup(args[1]) || !GameType.getGameFromSetup(args[1]).equals(GameType.NONE)) {
				GameType game = GameType.getGameFromSetup(args[1]);

				if (utils.isNumber(args[2])) {
					Integer id = Integer.valueOf(args[2]);

					if (id <= game.getMaxPostions()) {
						utils.registerLocationConfig(player, utils.captalize(args[1]) + ".Spawns." + id);
						player.sendMessage("§aVocê setou o spawn §f" + id + "§a do game §f" + game.getName() + "§a!");
					} else {
						player.sendMessage("§aO id precisa ser no máximo §f" + game.getMaxPostions() + "§a.");
					}
				} else {
					player.sendMessage("§aO id precisa ser um número.");
				}
			} else {
				player.sendMessage("§aO suposto MineGame: §f" + args[1] + " §anão foi encontrado!");
			}
		} else if (args.length == 2) {
			if (args[0].equalsIgnoreCase("spectator")) {
				if (GameType.containsToSetup(args[1]) && !GameType.getGameFromSetup(args[1]).equals(GameType.NONE)) {
					if (!GameType.getGameFromSetup(args[1]).equals(GameType.NONE)) {
						utils.registerLocationConfig(player, utils.captalize(args[1]) + ".Spectator");
						player.sendMessage("§aVocê setou o spawn dos §fespectadores §ado game §f"
								+ GameType.getGameFromSetup(args[1]).getName() + "§a!");
					} else {
						player.sendMessage("§aO suposto MineGame: §f" + args[1] + " §anão foi encontrado!");
					}
				} else {
					player.sendMessage("§aO suposto MineGame: §f" + args[1] + " §anão foi encontrado!");
				}
			} else if (args[0].equalsIgnoreCase("arrival")) {
				if (GameType.containsToSetup(args[1]) && !GameType.getGameFromSetup(args[1]).equals(GameType.NONE)) {
					if (!GameType.getGameFromSetup(args[1]).equals(GameType.LAVA_CHALLENGE)
							&& !GameType.getGameFromSetup(args[1]).equals(GameType.NONE)) {
						utils.registerLocationConfig(player, utils.captalize(args[1]) + ".Arrival");
						player.sendMessage("§aVocê setou a §fChegada §ado game §f"
								+ GameType.getGameFromSetup(args[1]).getName() + "§a!");
					} else {
						player.sendMessage("§aA Lava Challenge não pode ser setada utilizando arrival.");
						player.sendMessage(
								"§aPara setar a §fLava Challenge §autilize o comando /setar (spawn) (lava).");
					}
				} else {
					player.sendMessage("§aO suposto MineGame: §f" + args[1] + " §anão foi encontrado!");
				}
			} else if (args[0].equalsIgnoreCase("spawn")) {
				if (args[1].equalsIgnoreCase("gameselection")) {
					utils.registerLocationConfig(player, "GameSelection.Spawn");
					player.sendMessage("§aVocê setou spawn do §fGameSelection§a!");
				} else {
					player.sendMessage("§aEsse comando é permitido para usar apenas no GameSelection.");
					player.sendMessage("§aPara setar outros games utilize o comando /setar (spawn) (game) (id).");
				}
			} else if (args[0].equalsIgnoreCase("height")) {
				if (args[1].equalsIgnoreCase("parkour")) {
					utils.registerInConfig(utils.captalize(args[1]) + ".height", player.getLocation());
					player.sendMessage("§aVocê setou a §faltura §ado game §f"
							+ GameType.getGameFromSetup(args[1]).getName() + "§a!");
				} else {
					player.sendMessage("§aEsse comando é permitido para usar apenas no game Corrida de Parkour.");
					player.sendMessage("§aPara setar outros games utilize o comando /setar (spawn) (game) (id).");
				}
			} else {
				player.sendMessage(Messages.getWrongArgumentMessage("/setar (Spectator / Arrival / Spawn)", player));
			}
		} else if (args.length == 3) {
			if (args[0].equalsIgnoreCase("checkpoint")) {
				if (utils.isNumber(args[1])) {
					if (args[2].equalsIgnoreCase("parkour")) {
						utils.registerLocationConfig(player, utils.captalize(args[2]) + ".Checkpoint." + args[1]);
						player.sendMessage("§aVocê setou o checkpoint §f" + args[1] + " §ado game §f"
								+ GameType.getGameFromSetup(args[1]).getName() + "§a!");
					} else {
						player.sendMessage(Messages.getWrongArgumentMessage("/setar (checkpoint) (id) (game)", player));
					}
				} else {
					player.sendMessage("§aO checkpoint precisa ser um número!");
				}
			} else {
				player.sendMessage("§aEsse comando é permitido para usar apenas para o game Corrida Parkour.");
				player.sendMessage("§aPara setar outros games utilize o comando /setar (spawn) (game) (id).");
			}
		} else {
			player.sendMessage(
					Messages.getWrongArgumentMessage("/setar (Checkpoint / Spectator / Arrival / Spawn)", player));
		}

		return false;
	}

}
