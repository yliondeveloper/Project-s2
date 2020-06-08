package me.servidor.games.commands;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.servidor.games.principal.PartyGames;
import me.servidor.games.commands.manager.PartyCommand;
import me.servidor.games.game.GameType;
import me.servidor.games.gamer.Gamer;
import me.servidor.games.managers.Manager;
import me.servidor.games.managers.Messages;
import me.servidor.games.utils.Utils;
import me.servidor.games.utils.Utils.TimeFormatMode;

public class GameCommand extends PartyCommand {

	private Manager manager;
	private Utils utils;

	public GameCommand() {
		super("game");

		manager = PartyGames.getManager();
		utils = manager.getUtils();
	}

	@Override
	public boolean execute(CommandSender sender, String label, String[] args) {

		if (!isPlayer(sender))
			return true;

		Player player = (Player) sender;

		if (!player.hasPermission("star.party.time") && !player.hasPermission("star.party.vote")
				&& !player.hasPermission("star.party.set")) {
			player.sendMessage(Messages.getWithoutPermissionMessage(player));
			return true;
		}

		if (args.length == 1) {
			if (player.hasPermission("star.party.game.start")) {
				if (!manager.getPreGame().isCancelled()) {
					if (Bukkit.getOnlinePlayers().size() >= 2) {
						player.sendMessage(Messages.getPrefix() + "§aVocê iniciou o jogo!");
						manager.getPreGame().setTime(0);
						return true;
					} else {
						Messages.sendBroadcast(
								"§aJogadores insuficientes para poder iniciar o jogo! Jogadores mínimos: §f" + 2
										+ "§a.");
					}
				} else {
					player.sendMessage("§aEsse jogo já iniciou, portanto você não pode utilzar o comando: §f/start");
				}
			} else {
				player.sendMessage(Messages.getWrongArgumentMessage("/game (start)", player));
			}
		} else if (args.length == 2) {
			if (args[0].equalsIgnoreCase("time")) {
				if (player.hasPermission("star.party.game.time")) {
					if (utils.isNumber(args[1])) {
						if (!manager.getPreGame().isCancelled()) {
							Integer time = Integer.valueOf(args[1]);
							if (time > 0) {
								if (time <= 10200) {
									player.sendMessage("§aVocê alterou o tempo de inicio do jogo para §f"
											+ utils.formatTime(time, TimeFormatMode.COMPLETE) + "§a!");
									Messages.sendBroadcastWithBypass(
											Messages.getPrefix() + "O tempo de inicio do jogo foi alterado para §a"
													+ utils.formatTime(time, TimeFormatMode.COMPLETE) + "§7!",
											player);
									manager.getPreGame().setTime(time);
									return true;
								} else {
									player.sendMessage("§aO tempo máximo suportado é 10.200 (170 minutos).");
								}
							} else {
								player.sendMessage("§aAtenção o tempo precisa ser maior que 0!");
							}
						} else {
							player.sendMessage(Messages.getPrefix()
									+ "§aO jogo já iniciou, portanto você não pode alterar o tempo.");
						}
					} else {
						player.sendMessage("§aAtenção o tempo precisa ser um número!");
					}
				} else {
					player.sendMessage(Messages.getWithoutPermissionMessage(player));
				}
			} else if (args[0].equalsIgnoreCase("vote")) {
				if (player.hasPermission("star.party.vote")) {
					if (GameType.containsToSetup(args[1])) {
						Gamer gamer = manager.getGamerManager().getGamer(player);
						if (!gamer.hasVoted()) {
							if (new Random().nextInt(100) <= 25)
								manager.getGameManager().setNextGame(GameType.getGameFromSetup(args[1]));

							gamer.setVoted(true);
							player.sendMessage("§aVocê votou §f" + GameType.getGameFromSetup(args[1]).getName()
									+ " §apara ser o próximo minigame, ou sejá esse modo de jogo tem §f25% §ade chance de ser escolhido.");
						} else {
							player.sendMessage("§aOps, você já votou em um MiniGame nesta rodada!");
						}
					} else {
						player.sendMessage(
								Messages.getWrongArgumentMessage("/game vote (kangaroo, grappler, lava)", player));
					}
				} else {
					player.sendMessage("§aVocê precisa ser um vip para poder ter acesso a esse comando!");
				}
			} else if (args[0].equalsIgnoreCase("set")) {
				if (player.hasPermission("star.party.set")) {
					if (GameType.containsToSetup(args[1])) {
						manager.getGameManager().setNextGame(GameType.getGameFromSetup(args[1]));
						player.sendMessage(Messages.getPrefix() + "§aVocê setou §f"
								+ GameType.getGameFromSetup(args[1]).getName() + " §apara ser o próximo minigame!");
					} else {
						player.sendMessage(
								Messages.getWrongArgumentMessage("/game set (kangaroo, grappler, lava)", player));
					}
				} else {
					player.sendMessage(Messages.getWithoutPermissionMessage(player));
				}
			} else {
				player.sendMessage(Messages.getWrongArgumentMessage(
						"/game (" + (player.hasPermission("star.party.start") ? "start" : "") + ", "
								+ (player.hasPermission("star.party.time") ? "time" : "") + ", "
								+ (player.hasPermission("star.party.vote") ? "vote" : "") + ", "
								+ (player.hasPermission("star.party.set") ? "set" : "") + ")",
						player));
			}
		} else {
			player.sendMessage(Messages
					.getWrongArgumentMessage("/game (" + (player.hasPermission("star.party.start") ? "start" : "")
							+ ", " + (player.hasPermission("star.party.time") ? "time" : "") + ", "
							+ (player.hasPermission("star.party.vote") ? "vote" : "") + ", "
							+ (player.hasPermission("star.party.set") ? "set" : "") + ")", player));
		}

		return false;
	}

}
