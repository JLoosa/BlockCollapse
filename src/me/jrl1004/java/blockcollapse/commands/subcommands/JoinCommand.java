package me.jrl1004.java.blockcollapse.commands.subcommands;

import me.jrl1004.java.blockcollapse.BlockCollapse;
import me.jrl1004.java.blockcollapse.commands.SubCommand;
import me.jrl1004.java.blockcollapse.game.Game;
import me.jrl1004.java.blockcollapse.game.GameManager;
import me.jrl1004.java.blockcollapse.utilities.MessageManager;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JoinCommand extends SubCommand {

	public JoinCommand() {
		super("Join");
		setAliases("j");
		setCmdParams("[ID | Name]");
		setPermNode(BlockCollapse.getPlayerPermissionNode() + "join");
		setUsage(getDefaultUsage());
	}

	@Override
	public void onCommand(CommandSender sender, String[] args) {
		if (!hasPermission(sender)) {
			noPermission(sender);
			return;
		}
		if (!isPlayer(sender)) {
			playerOnly(sender);
			return;
		}
		if (args.length < getCmdParams().length) {
			missingArguments(sender);
			return;
		}
		Player player = (Player) sender;
		if (GameManager.getGameManager().getPlayerGame(player) != null) {
			MessageManager.messagePrefixed(sender, "You are already in a game");
			return;
		}
		Game game = GameManager.getGameManager().getFromString(args[0]);
		if (game == null) {
			MessageManager.messagePrefixed(sender, "Game could not be found");
			return;
		}
		MessageManager.messagePrefixed(sender, "Joining game " + game.getIdentifier());
		game.addPlayer(player);
	}

}
