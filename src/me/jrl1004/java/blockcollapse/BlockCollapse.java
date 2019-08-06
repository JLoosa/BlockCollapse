package me.jrl1004.java.blockcollapse;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;

import me.jrl1004.java.blockcollapse.commands.CommandManager;
import me.jrl1004.java.blockcollapse.game.GameManager;
import me.jrl1004.java.blockcollapse.utilities.BCConfig;

public class BlockCollapse extends JavaPlugin {
    @Override
    public void onDisable() {
	HandlerList.unregisterAll(this);
	GameManager.getGameManager().unloadGames();
	saveConfig();
	super.onDisable();
    }

    @Override
    public void onEnable() {
	if (getConfig() == null)
	    saveResource("config.yml", true);
	if (getConfig().getBoolean("use-config")) {
	    if (getConfig().getBoolean("reset-config")) {
		saveResource("config.yml", true);
		getConfig().set("reset-config", false);
	    }
	    BCConfig.create(getConfig());
	}
	getCommand("BlockCollapse").setExecutor(new CommandManager());
	GameManager.getGameManager().loadSavedGames();
	GameManager.getGameManager().startTicks();
	super.onEnable();
    }

    public static BlockCollapse getBlockCollapse() {
	Plugin plugin = Bukkit.getPluginManager().getPlugin("BlockCollapse");
	if (plugin instanceof BlockCollapse && plugin.isEnabled())
	    return (BlockCollapse) plugin;
	return null;
    }

    public static WorldEditPlugin getWorldEdit() {
	Plugin plugin = Bukkit.getPluginManager().getPlugin("WorldEdit");
	if (plugin instanceof WorldEditPlugin && plugin.isEnabled())
	    return (WorldEditPlugin) plugin;
	return null;
    }

    public static String getAdminPermissionNode() {
	return getDefaultPermissionNode() + "admin.";
    }

    public static String getPlayerPermissionNode() {
	return getDefaultPermissionNode() + "player.";
    }

    public static String getDefaultPermissionNode() {
	return "blockcollapse.";
    }
}
