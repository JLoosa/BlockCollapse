package me.jrl1004.java.blockcollapse.utilities;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

public class BCConfig {
    private static BCConfig instance;

    public static BCConfig create(FileConfiguration fileConfiguration) {
	return new BCConfig(fileConfiguration);
    }

    public static BCConfig get() {
	return instance;
    }

    public final String chatPrefix;
    public final int tickDelay;
    public final int maxPlayers;
    public final int minPlayers;
    public final int lobbyTime;
    public final int gracePeriod;

    private BCConfig(FileConfiguration fileConfiguration) {
	if (fileConfiguration == null)
	    throw new IllegalArgumentException("Config file may not be null!");
	String tempChatPrefix = fileConfiguration.getString("chat-prefix", "&a[BlockCollapse] &r");
	chatPrefix = ChatColor.translateAlternateColorCodes('&', tempChatPrefix);
	tickDelay = fileConfiguration.getInt("block-break-speed", 3);
	maxPlayers = fileConfiguration.getInt("max-players-per-game", 8);
	minPlayers = fileConfiguration.getInt("min-players-per-game", 2);
	lobbyTime = fileConfiguration.getInt("lobby-time", 30) * 20;
	gracePeriod = fileConfiguration.getInt("grace-period", 5) * 20;
	instance = this;
    }
}
