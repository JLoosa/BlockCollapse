package me.jrl1004.java.blockcollapse.commands.subcommands;

import me.jrl1004.java.blockcollapse.BlockCollapse;
import me.jrl1004.java.blockcollapse.commands.SubCommand;
import me.jrl1004.java.blockcollapse.game.Game;
import me.jrl1004.java.blockcollapse.game.GameManager;
import me.jrl1004.java.blockcollapse.utilities.GameException;
import me.jrl1004.java.blockcollapse.utilities.MessageManager;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GameCommand extends SubCommand {
    private final String[] params = { "start", "stop", "reset" };

    public GameCommand() {
	super("Game");
	setAliases("g");
	setCmdParams("[Param]");
	setPermNode(BlockCollapse.getAdminPermissionNode() + "game");
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
	if (!isPlayer(sender)) {
	    playerOnly(sender);
	    return;
	}
	Player player = (Player) sender;
	Game game = GameManager.getGameManager().getPlayerGame(player);
	if (game == null) {
	    MessageManager.messagePrefixed(sender, "This command only works when in a game");
	    return;
	}
	switch (args[0].toLowerCase()) {
	case "start":
	    startGame(player, game);
	    break;
	case "stop":
	    stopGame(player, game);
	    break;
	case "reset":
	    resetGame(player, game);
	    break;
	default:
	    MessageManager.messagePrefixed(sender, "Param not found. Valid params are: ");
	    MessageManager.messageWithPrefix(sender, ChatColor.AQUA + " -> ", params);
	}
    }

    private void resetGame(Player player, Game game) {
	game.resetMap();
	MessageManager.messagePrefixed(player, "Game map force-reset");
    }

    private void stopGame(Player player, Game game) {
	game.endGame(null);
	MessageManager.messagePrefixed(player, "Game force-ended");
    }

    private void startGame(Player player, Game game) {
	try {
	    game.startGame(true);
	    MessageManager.messagePrefixed(player, "Game started");
	} catch (GameException e) {
	    MessageManager.messagePrefixed(player, "Failed to start game: [GameException] " + e.getMessage());
	}
    }
}
