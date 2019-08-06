package me.jrl1004.java.blockcollapse.commands.subcommands;

import me.jrl1004.java.blockcollapse.BlockCollapse;
import me.jrl1004.java.blockcollapse.commands.SubCommand;
import me.jrl1004.java.blockcollapse.game.Game;
import me.jrl1004.java.blockcollapse.game.GameManager;
import me.jrl1004.java.blockcollapse.utilities.GameException;
import me.jrl1004.java.blockcollapse.utilities.MessageManager;

import org.bukkit.command.CommandSender;

public class CreateCommand extends SubCommand {
    public CreateCommand() {
	super("Create");
	setAliases("c");
	setPermNode(BlockCollapse.getAdminPermissionNode() + "create");
	setCmdParams("[Name]");
	setUsage(getDefaultUsage());
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
	if (!hasPermission(sender)) {
	    noPermission(sender);
	    return;
	}
	if (args.length < getCmdParams().length) {
	    missingArguments(sender);
	    return;
	}
	Game game = GameManager.getGameManager().getByName(args[0]);
	boolean useName = true;
	if (game != null) {
	    MessageManager.messagePrefixed(sender, "A game with that name already exists. Creating unnamed game.");
	    useName = false;
	}
	game = GameManager.getGameManager().getNewGame();
	if (useName)
	    try {
		game.setGameName(args[0]);
	    } catch (GameException e) {
	    }
	sender.sendMessage("Game created: " + game.getIdentifier() + "(ID: " + game.getId() + ")");
    }
}
