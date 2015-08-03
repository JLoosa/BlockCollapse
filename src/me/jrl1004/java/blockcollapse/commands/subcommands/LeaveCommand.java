package me.jrl1004.java.blockcollapse.commands.subcommands;

import me.jrl1004.java.blockcollapse.commands.SubCommand;
import me.jrl1004.java.blockcollapse.game.Game;
import me.jrl1004.java.blockcollapse.game.GameManager;
import me.jrl1004.java.blockcollapse.utilities.MessageManager;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LeaveCommand extends SubCommand {

	public LeaveCommand() {
		super("Leave");
		setAliases("l");
		setPermNode("");
		setUsage(getDefaultUsage());
	}
	
	@Override
	public void onCommand(CommandSender sender, String[] args) {
		if (!isPlayer(sender)) {
			playerOnly(sender);
			return;
		}
		Player player = (Player) sender;
		Game game =GameManager.getGameManager().getPlayerGame(player);
		if(game != null) {
			game.removePlayer(player);
			MessageManager.messagePrefixed(player, "You have left the game");
			return;
		}
		else {
			MessageManager.messagePrefixed(player, "You are not currently in a game");
			return;
		}
	}

}
