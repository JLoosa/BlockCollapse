package me.jrl1004.java.blockcollapse.utilities;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class MessageManager {

	private MessageManager() {
	}

	private static String _BlockCollapse = ChatColor.GOLD + "[" + ChatColor.RED + "BlockCollapse" + ChatColor.GOLD + "] " + ChatColor.AQUA;

	public static void messagePrefixed(CommandSender sender, String... messages) {
		messageWithPrefix(sender, _BlockCollapse, messages);
	}

	public static void messageWithPrefix(CommandSender sender, String prefix, String... messages) {
		if (messages.length == 0) return;
		for (String s : messages)
			sender.sendMessage(prefix + s);
	}
}
