package me.jrl1004.java.blockcollapse.commands.subcommands;

import java.io.IOException;

import me.jrl1004.java.blockcollapse.BlockCollapse;
import me.jrl1004.java.blockcollapse.commands.SubCommand;
import me.jrl1004.java.blockcollapse.game.Game;
import me.jrl1004.java.blockcollapse.game.GameManager;
import me.jrl1004.java.blockcollapse.utilities.GameException;
import me.jrl1004.java.blockcollapse.utilities.MessageManager;

import org.bukkit.command.CommandSender;

public class SaveCommand extends SubCommand {

	public SaveCommand() {
		super("Save");
		setCmdParams("[ID | Name]");
		setPermNode(BlockCollapse.getAdminPermissionNode() + "save");
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
		Game game = GameManager.getGameManager().getFromString(args[0]);
		if (game == null) {
			MessageManager.messagePrefixed(sender, "Could not find game");
			return;
		}
		try {
			game.saveGameSettingsToFile();
			MessageManager.messagePrefixed(sender, game.getIdentifier() + " was successfully saved to file");
		} catch (GameException e) {
			MessageManager.messagePrefixed(sender, "Failed to save to file: [GameException] " + e.getMessage());
		} catch (IOException e) {
			MessageManager.messagePrefixed(sender, "Failed to save to file: [IOException] " + e.getMessage());
			e.printStackTrace();
		}
	}

}
