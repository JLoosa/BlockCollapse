package me.jrl1004.java.blockcollapse.commands.subcommands;

import me.jrl1004.java.blockcollapse.BlockCollapse;
import me.jrl1004.java.blockcollapse.commands.SubCommand;
import me.jrl1004.java.blockcollapse.game.Game;
import me.jrl1004.java.blockcollapse.game.GameManager;
import me.jrl1004.java.blockcollapse.utilities.MessageManager;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.bukkit.selections.Selection;

public class SetCommand extends SubCommand {
    private final String[] params = { "region", "spawn" };

    public SetCommand() {
	super("Set");
	setCmdParams("[Game]", "[Param]", "(Value)");
	setAliases("s");
	setPermNode(BlockCollapse.getAdminPermissionNode() + "set");
	setUsage(getDefaultUsage());
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
	if (!hasPermission(sender)) {
	    noPermission(sender);
	    return;
	}
	if (args.length < getCmdParams().length - 1) {
	    missingArguments(sender);
	    return;
	}
	Game game = GameManager.getGameManager().getFromString(args[0]);
	if (game == null) {
	    MessageManager.messagePrefixed(sender, "Game not found");
	    return;
	}
	switch (args[1].toLowerCase()) {
	case "region":
	    setRegion(sender, game);
	    break;
	case "spawn":
	    setSpawn(sender, game);
	    break;
	default:
	    MessageManager.messagePrefixed(sender, "Valid params are: ");
	    MessageManager.messageWithPrefix(sender, ChatColor.AQUA + " -> ", params);
	    break;
	}
    }

    private void setSpawn(CommandSender sender, Game game) {
	if (!isPlayer(sender)) {
	    playerOnly(sender);
	    return;
	}
	game.setTeleportLocation(((Player) sender).getLocation());
	MessageManager.messagePrefixed(sender, "Game " + game.getIdentifier() + " has had is spawn set to your location");
    }

    private void setRegion(CommandSender sender, Game game) {
	if (!isPlayer(sender)) {
	    playerOnly(sender);
	    return;
	}
	Selection sel = BlockCollapse.getWorldEdit().getSelection((Player) sender);
	if (sel == null) {
	    MessageManager.messagePrefixed(sender, "You must have a valid WorldEdit selection");
	    return;
	}
	game.setPlayArea(sel);
	MessageManager.messagePrefixed(sender, "Game " + game.getIdentifier() + " has had it's play area set to your WorldEdit selection");
    }
}
