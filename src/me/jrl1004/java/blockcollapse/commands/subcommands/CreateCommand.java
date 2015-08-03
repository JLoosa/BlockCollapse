package me.jrl1004.java.blockcollapse.commands.subcommands;

import me.jrl1004.java.blockcollapse.BlockCollapse;
import me.jrl1004.java.blockcollapse.commands.SubCommand;
import me.jrl1004.java.blockcollapse.game.Game;
import me.jrl1004.java.blockcollapse.game.GameManager;

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
		Game game = GameManager.getGameManager().getNewGame();
		game.setGameName(args[0]);
		sender.sendMessage("Game created with ID " + game.getId());
	}
}
