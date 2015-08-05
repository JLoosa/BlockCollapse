package me.jrl1004.java.blockcollapse.utilities;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

public class BCConfig {

	private BCConfig() {
	}

	private static String chatPrefix = "[BlockCollapse]";

	public static String getChatPrefix() {
		return chatPrefix;
	}

	private static int tickDelay = 3;

	public static int getTickDelay() {
		return tickDelay;
	}

	private static int maxPlayers = 8;

	public static int getMaxPlayers() {
		return maxPlayers;
	}

	private static int lobbyTime = 30 * 20;

	public static int getLobbyTime() {
		return lobbyTime;
	}

	private static int gracePeriod = 5 * 20;

	public static int getGracePeriod() {
		return gracePeriod;
	}

	public static void loadData(FileConfiguration fileConfiguration) {
		if (fileConfiguration == null) return;
		Object prefix = fileConfiguration.get("chat-prefix");
		if (prefix != null) chatPrefix = ChatColor.translateAlternateColorCodes('&', (String) prefix);
		Object ticks = fileConfiguration.get("block-break-speed");
		if (ticks != null) tickDelay = (int) ticks;
		Object players = fileConfiguration.get("max-players-per-game");
		if (players != null) maxPlayers = (int) players;
		Object lobby = fileConfiguration.get("lobby-time");
		if (lobby != null) lobbyTime = (int) (((double) lobby) * 20);
		Object grace = fileConfiguration.get("grace-period");
		if (grace != null) gracePeriod = (int) (((double) grace) * 20);
	}

}
