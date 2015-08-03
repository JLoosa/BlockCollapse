package me.jrl1004.java.blockcollapse;

import me.jrl1004.java.blockcollapse.commands.CommandManager;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;

public class BlockCollapse extends JavaPlugin {

	@Override
	public void onDisable() {
		HandlerList.unregisterAll(this);

		super.onDisable();
	}

	@Override
	public void onEnable() {
		saveDefaultConfig();
		getCommand("BlockCollapse").setExecutor(new CommandManager());
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
		return "BlockCollapse.";
	}
}
