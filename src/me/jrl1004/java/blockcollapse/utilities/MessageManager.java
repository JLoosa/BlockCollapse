package me.jrl1004.java.blockcollapse.utilities;

import org.bukkit.command.CommandSender;

public class MessageManager {

	private MessageManager() {
	}

	private static String getDefaultPrefix() {
		return BCConfig.getChatPrefix();
	}

	public static void messagePrefixed(CommandSender sender, String... messages) {
		messageWithPrefix(sender, getDefaultPrefix(), messages);
	}

	public static void messageWithPrefix(CommandSender sender, String prefix, String... messages) {
		if (messages.length == 0) return;
		for (String s : messages)
			sender.sendMessage(prefix + s);
	}
}
