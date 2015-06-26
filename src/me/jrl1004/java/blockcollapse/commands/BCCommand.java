package me.jrl1004.java.blockcollapse.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class BCCommand {

	public final String		name;
	public final String[]	aliases;
	public final String		usage;

	public BCCommand() {
		this.name = getClass().getSimpleName();
		this.aliases = new String[0];
		this.usage = "";
	}

	public BCCommand(String name) {
		this.name = name;
		this.aliases = new String[0];
		this.usage = "";
	}

	public BCCommand(String name, String usage) {
		this.name = name;
		this.aliases = new String[0];
		this.usage = usage;
	}

	public BCCommand(String name, String usage, String... aliases) {
		this.name = name;
		this.aliases = aliases;
		this.usage = usage;
	}

	public BCCommand(String name, String... aliases) {
		this.name = name;
		this.aliases = aliases;
		this.usage = "";
	}

	public abstract void onCommand(CommandSender sender, String... args);

	public abstract void onPlayerCommand(Player player, String... args);

	public boolean isCommand(String key) {
		if (key.equalsIgnoreCase(name)) return true;
		if (aliases.length > 0) for (String a : aliases)
			if (key.equalsIgnoreCase(a)) return true;
		return false;
	}
}
