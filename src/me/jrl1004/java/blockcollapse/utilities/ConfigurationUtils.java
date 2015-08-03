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

	private ConfigurationUtils() {
	}

	public static Selection getPlayArea(YamlConfiguration config) {
		World world = Bukkit.getWorld(config.getString("World"));
		Location min = Str2Loc(config.getString("Locations.PlayAreaMin"));
		Location max = Str2Loc(config.getString("Locations.PlayAreaMax"));
		Selection sel = new CuboidSelection(world, min, max);
		return sel;
	}

	private static Location Str2Loc(String str) {
		String[] arr = str.split(":");
		World world = Bukkit.getWorld(arr[0]);
		Double x = Double.parseDouble(arr[1]);
		Double y = Double.parseDouble(arr[2]);
		Double z = Double.parseDouble(arr[3]);
		return new Location(world, x, y, z);
	}

	public static Location getTeleportLocation(YamlConfiguration config) {
		Location tp = Str2Loc(config.getString("Locations.SpawnPoint"));
		return tp;
	}

	public static String getGameName(YamlConfiguration config) {
		String name = config.getString("Name");
		return name;
	}

	public static void saveGameToFile(Game game) throws IOException {
		File gameFile = new File(BlockCollapse.getBlockCollapse().getDataFolder(), "Games");
		if (!gameFile.exists()) gameFile.mkdirs();
		gameFile = new File(gameFile, game.getGameName());
		if (!gameFile.exists()) gameFile.createNewFile();
	}

}
