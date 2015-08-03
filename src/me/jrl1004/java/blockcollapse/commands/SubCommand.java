package me.jrl1004.java.blockcollapse.commands;

import me.jrl1004.java.blockcollapse.utilities.MessageManager;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class SubCommand {

	private String name;
	private String[] aliases;
	private String usage;
	private String permNode;
	private String[] cmdParams;

	public SubCommand(String name) {
		this.name = name;
		this.aliases = new String[0];
		this.cmdParams = new String[0];
		this.usage = getDefaultUsage();
		this.permNode = "";
	}

	public String getDefaultUsage() {
		return "/BlockCollapse " + name + " " + getCmdParamsAsString();
	}

	public String getCmdParamsAsString() {
		String ret = "";
		if (cmdParams.length == 0) return ret;
		for (String s : cmdParams)
			ret += (" " + s);
		return ret.trim();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String[] getAliases() {
		return aliases;
	}

	public void setAliases(String... aliases) {
		this.aliases = aliases;
	}

	public String getUsage() {
		return usage;
	}

	public void setUsage(String usage) {
		this.usage = usage;
	}

	public String getPermNode() {
		return permNode;
	}

	public void setPermNode(String permNode) {
		this.permNode = permNode;
	}

	public String[] getCmdParams() {
		return cmdParams;
	}

	public void setCmdParams(String... cmdParams) {
		this.cmdParams = cmdParams;
	}

	public boolean hasPermission(CommandSender sender) {
		return sender.isOp() || sender.hasPermission(getPermNode());
	}

	public void sendUsage(CommandSender sender) {
		MessageManager.messagePrefixed(sender, "Usage: " + getUsage());
	}

	public void noPermission(CommandSender sender) {
		MessageManager.messagePrefixed(sender, "You do not have permission to use this command");
		MessageManager.messagePrefixed(sender, getPermNode());
	}

	public void missingArguments(CommandSender sender) {
		MessageManager.messagePrefixed(sender, "Not enough arguments");
		sendUsage(sender);
	}

	public boolean isPlayer(CommandSender sender) {
		return sender instanceof Player;
	}

	public void playerOnly(CommandSender sender) {
		MessageManager.messagePrefixed(sender, "This is a player-only command");
	}

	public String[] removeFirst(String... a) {
		if (a.length < 2) return new String[0];
		String[] ret = new String[a.length - 1];
		for (int i = 0; i < ret.length; i++)
			ret[i] = a[i + 1];
		return ret;
	}

	public abstract void onCommand(CommandSender sender, String[] args);

	public boolean isCommand(String string) {
		if (string.equalsIgnoreCase(name)) return true;
		if (aliases.length == 0) return false;
		for (String s : aliases)
			if (s.equalsIgnoreCase(string)) return true;
		return false;
	}

}
