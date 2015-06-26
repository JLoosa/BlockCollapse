package me.jrl1004.java.blockcollapse.managers;

import java.io.File;
import java.util.HashMap;
import java.util.Scanner;

import me.jrl1004.java.blockcollapse.BlockCollapse;
import me.jrl1004.java.blockcollapse.maps.Map;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.util.Vector;

public class MapManager {

	private final String	SPLITTER	= ";";

	private File			mapFolder;

	public MapManager() {
		mapFolder = new File(BlockCollapse.get().getDataFolder(), "Maps");
		if (!mapFolder.exists()) mapFolder.mkdirs();
		loadMaps();
	}

	private void loadMaps() {
		File[] mapFiles = mapFolder.listFiles();
		if (mapFiles == null || mapFiles.length == 0) return; // We have no work to do
		for (File map : mapFiles) {
			try {
				loadMap(map);
			} catch (Exception e) {
				BlockCollapse.get().getLogger().warning("Failed to load level: " + map.getName());
			}
		}
	}

	private void loadMap(File mapFile) throws Exception {
		Scanner scanner = new Scanner(mapFile);
		HashMap<String, String> keyMap = new HashMap<String, String>();
		while (scanner.hasNextLine()) {
			String read = scanner.nextLine();
			String[] data = read.split(":");
			keyMap.put(data[0].trim(), data[1].trim());
		}
		// Load core data
		World mapWorld = Bukkit.getWorld(keyMap.get("WORLD"));
		Vector nativeMinimum = vectorFromString(keyMap.get("VECTOR_MINIMUM"));
		Vector nativeMaximum = vectorFromString(keyMap.get("VECTOR_MAXIMUM"));
		// Instantiate world
		Map map = new Map(mapWorld, nativeMinimum, nativeMaximum);
		// Load remaining data
		String name = keyMap.get("NAME");
		map.setName(name);
		Vector teleportLocation = vectorFromString(keyMap.get("VECTOR_TELEPORT"));
		map.setTeleportLocation(teleportLocation);
		scanner.close();
	}

	private Vector vectorFromString(String vector) throws NumberFormatException {
		String[] data = vector.split(SPLITTER);
		double x = Double.parseDouble(data[0]);
		double y = Double.parseDouble(data[1]);
		double z = Double.parseDouble(data[2]);
		return new Vector(x, y, z);
	}
}
