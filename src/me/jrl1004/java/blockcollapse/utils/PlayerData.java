package me.jrl1004.java.blockcollapse.utils;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PlayerData {

	private Player		player;
	private Location	location;
	private float		fallDistance;
	private int			totalExp;
	private int			foodLevel;
	private GameMode	gameMode;

	public PlayerData(Player player) {
		this.player = player;
		writeVars();
	}

	private void writeVars() {
		this.location = player.getLocation();
		this.fallDistance = player.getFallDistance();
		this.totalExp = player.getTotalExperience();
		this.foodLevel = player.getFoodLevel();
		this.gameMode = player.getGameMode();
	}

	public void restorePlayer() {
		player.teleport(location);
		player.setFallDistance(fallDistance);
		player.setTotalExperience(totalExp);
		player.setFoodLevel(foodLevel);
		player.setGameMode(gameMode);
	}

	public void wipeData() {
		player = null;
		location = null;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public float getFallDistance() {
		return fallDistance;
	}

	public void setFallDistance(float fallDistance) {
		this.fallDistance = fallDistance;
	}

	public int getTotalExp() {
		return totalExp;
	}

	public void setTotalExp(int totalExp) {
		this.totalExp = totalExp;
	}

	public int getFoodLevel() {
		return foodLevel;
	}

	public void setFoodLevel(int foodLevel) {
		this.foodLevel = foodLevel;
	}

	public GameMode getGameMode() {
		return gameMode;
	}

	public void setGameMode(GameMode gameMode) {
		this.gameMode = gameMode;
	}

	public Player getPlayer() {
		return player;
	}
}
