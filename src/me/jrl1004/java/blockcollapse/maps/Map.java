package me.jrl1004.java.blockcollapse.maps;

import me.jrl1004.java.blockcollapse.utils.BCConstants;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

public class Map {

	private String	name;				// the name of the map
	private World	mapWorld;			// The world that the map exists in
	private Vector	nativeMinimum;		// The lowest point of the map in terms of coordinates
	private Vector	nativeMaximum;		// The highest point of the map in terms of coordinates
	private Vector	teleportLocation;	// Where players teleport in at
	private Integer	maxPlayers;		// How Many players this map supports

	public Map(World mapWorld, Vector nativeMinimum, Vector nativeMaximum) {
		this.name = "";
		this.mapWorld = mapWorld;
		this.nativeMinimum = nativeMinimum;
		this.nativeMaximum = nativeMaximum;
		this.teleportLocation = nativeMinimum;
		this.maxPlayers = BCConstants.MAP_PLAYER_DEFAULT;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getMaxPlayers() {
		return maxPlayers;
	}

	public void setMaxPlayers(Integer maxPlayers) {
		this.maxPlayers = maxPlayers;
	}

	public Location getTeleportLocation() {
		return teleportLocation.toLocation(mapWorld);
	}

	public void setTeleportLocation(Vector teleportLocation) {
		this.teleportLocation = teleportLocation;
	}

	public boolean isInMap(Vector location) {
		// Check the lowest values first
		if (location.getX() < nativeMinimum.getX()) return false;
		if (location.getY() < nativeMinimum.getY()) return false;
		if (location.getZ() < nativeMinimum.getZ()) return false;
		// Then check the upper bounds
		if (location.getX() > nativeMaximum.getX()) return false;
		if (location.getY() > nativeMaximum.getY()) return false;
		if (location.getZ() > nativeMaximum.getZ()) return false;
		// It is not below the lowest values or above the maximum values
		return true;
	}
}
