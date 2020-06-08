package me.servidor.games.commands.manager;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.servidor.games.managers.Messages;

public abstract class PartyCommand extends Command {

	public boolean enabled;

	public PartyCommand(String name) {
		super(name);
		enabled = true;
	}

	public PartyCommand(String name, List<String> aliases, String description) {
		super(name, description, "", aliases);
		enabled = true;
	}

	protected boolean hasPermission(CommandSender sender, String permission) {
		boolean hasPermission = sender.hasPermission("star.party." + permission);
		if (!hasPermission) {
			sender.sendMessage(Messages.getWithoutPermissionMessage((Player) sender));
		}
		return hasPermission;
	}

	protected boolean isPlayer(CommandSender sender) {
		boolean isPlayer = sender instanceof Player;
		if (!isPlayer)
			sender.sendMessage(Messages.getCommandOnlyForPlayersMessage(getName()));
		return isPlayer;
	}

}
