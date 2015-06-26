package me.jrl1004.java.blockcollapse;

import me.jrl1004.java.blockcollapse.managers.CommandManager;
import me.jrl1004.java.blockcollapse.managers.EventManager;
import me.jrl1004.java.blockcollapse.managers.GameManager;
import me.jrl1004.java.blockcollapse.managers.MapManager;
import me.jrl1004.java.blockcollapse.managers.ScoreManager;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class BlockCollapse extends JavaPlugin {

	private MapManager		mapManager;
	private GameManager		gameManager;
	private ScoreManager	scoreManager;
	private CommandManager	commandManager;
	private EventManager	eventManager;

	@Override
	public void onDisable() {
		HandlerList.unregisterAll(this);
		super.onDisable();
	}

	@Override
	public void onEnable() {
		saveDefaultConfig();

		//mapManager = new MapManager();
		gameManager = new GameManager();
		scoreManager = new ScoreManager();
		commandManager = new CommandManager();
		eventManager = new EventManager();

		getCommand("BlockCollapse").setExecutor(commandManager);

		Bukkit.getPluginManager().registerEvents(eventManager, this);

		super.onEnable();
	}

	public static BlockCollapse get() {
		Plugin plugin = Bukkit.getPluginManager().getPlugin("BlockCollapse");
		if (plugin instanceof BlockCollapse)
			return (BlockCollapse) plugin;
		return null;
	}

	public MapManager getMapManager() {
		return mapManager;
	}

	public GameManager getGameManager() {
		return gameManager;
	}

	public ScoreManager getScoreManager() {
		return scoreManager;
	}

	public CommandManager getCommandManager() {
		return commandManager;
	}

	public EventManager getEventManager() {
		return eventManager;
	}
}
