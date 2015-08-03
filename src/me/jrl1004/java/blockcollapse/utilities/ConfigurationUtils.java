package me.jrl1004.java.blockcollapse.utilities;

import java.io.File;
import java.io.IOException;

import me.jrl1004.java.blockcollapse.BlockCollapse;
import me.jrl1004.java.blockcollapse.game.Game;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;

import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import com.sk89q.worldedit.bukkit.selections.Selection;

public class ConfigurationUtils {

	private final static String key_WORLD = "World";
	private final static String key_PA_MIN = "Locations.PlayAreaMin";
	private final static String key_PA_MAX = "Locations.PlayAreaMax";
	private final static String key_NAME = "Name";
	private final static String key_SPAWN = "Locations.SpawnPoint";

	private final static String key_SPLIT = ":";

	private ConfigurationUtils() {
	}

	public static Selection getPlayArea(YamlConfiguration config) {
		World world = Bukkit.getWorld(config.getString(key_WORLD));
		Location min = Str2Loc(config.getString(key_PA_MIN));
		Location max = Str2Loc(config.getString(key_PA_MAX));
		Selection sel = new CuboidSelection(world, min, max);
		return sel;
	}

	private static Location Str2Loc(String str) {
		String[] arr = str.split(key_SPLIT);
		World world = Bukkit.getWorld(arr[0]);
		Double x = Double.parseDouble(arr[1]);
		Double y = Double.parseDouble(arr[2]);
		Double z = Double.parseDouble(arr[3]);
		return new Location(world, x, y, z);
	}

	private static String Loc2Str(Location loc, boolean useWorld) {
		String world = loc.getWorld().getName();
		String x = (loc.getX() + "");
		x = x.substring(0, x.indexOf(".") + 2);
		String y = (loc.getX() + "");
		y = y.substring(0, y.indexOf(".") + 2);
		String z = (loc.getX() + "");
		z = z.substring(0, z.indexOf(".") + 2);
		return world + key_SPLIT + x + key_SPLIT + y + key_SPLIT + z;
	}

	private static String Loc2Str(Location loc) {
		return Loc2Str(loc, true);
	}

	public static Location getTeleportLocation(YamlConfiguration config) {
		Location tp = Str2Loc(config.getString(key_SPAWN));
		return tp;
	}

	public static String getGameName(YamlConfiguration config) {
		String name = config.getString(key_NAME);
		return name;
	}

	public static void saveGameToFile(Game game) throws IOException {
		File gameFile = new File(getSaveFolder(), game.getGameName());
		if (!gameFile.exists()) gameFile.createNewFile();
		YamlConfiguration config = YamlConfiguration.loadConfiguration(gameFile);
		config.set(key_NAME, game.getGameName());
		config.set(key_WORLD, game.getTeleportLocation().getWorld());
		config.set(key_SPAWN, Loc2Str(game.getTeleportLocation()));
		config.set(key_PA_MAX, Loc2Str(game.getPlayArea().getMaximumPoint(), true));
		config.set(key_PA_MIN, Loc2Str(game.getPlayArea().getMinimumPoint(), true));
		config.save(gameFile);
	}
	
	public static File getSaveFolder() {
		File saveFolder = new File(BlockCollapse.getBlockCollapse().getDataFolder(), "Games");
		if (!saveFolder.exists()) saveFolder.mkdirs();
		return saveFolder;
	}
}
