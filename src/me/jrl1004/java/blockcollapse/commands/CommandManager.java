package me.jrl1004.java.blockcollapse.commands;

import java.util.Arrays;
import java.util.List;

import me.jrl1004.java.blockcollapse.commands.subcommands.CreateCommand;
import me.jrl1004.java.blockcollapse.commands.subcommands.JoinCommand;
import me.jrl1004.java.blockcollapse.commands.subcommands.LeaveCommand;
import me.jrl1004.java.blockcollapse.commands.subcommands.SaveCommand;
import me.jrl1004.java.blockcollapse.commands.subcommands.SetCommand;
import me.jrl1004.java.blockcollapse.utilities.MessageManager;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandManager implements CommandExecutor {

	private List<SubCommand> commands = Arrays.asList(new CreateCommand(), new JoinCommand(), new LeaveCommand(), new SetCommand(), new SaveCommand());

	@Override
	public boolean onCommand(CommandSender _sender, Command _command, String _label, String[] args) {
		if (!_command.getName().equalsIgnoreCase("blockcollapse")) return false;
		if (args.length == 0) {
			MessageManager.messagePrefixed(_sender, "/BlockCollapse <Command> (Params)");
			MessageManager.messagePrefixed(_sender, "Current commands are: ");
			for (SubCommand sc : commands)
				MessageManager.messageWithPrefix(_sender, ChatColor.AQUA + " -> ", sc.getName());
			return true;
		}
		for (SubCommand sc : commands) {
			if (sc.isCommand(args[0])) {
				sc.onCommand(_sender, sc.removeFirst(args));
				return true;
			}
		}
		MessageManager.messagePrefixed(_sender, "Command not found");
		return true;
	}
}
