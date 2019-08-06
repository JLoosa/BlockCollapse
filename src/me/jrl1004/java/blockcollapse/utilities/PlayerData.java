package me.jrl1004.java.blockcollapse.utilities;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PlayerData {
    private Player player;
    private Location location;
    private float fallDistance;
    private int totalExp;
    private int foodLevel;
    private GameMode gameMode;
    private ItemStack[] inventory;
    private ItemStack[] armor;

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
	this.inventory = player.getInventory().getContents();
	this.armor = player.getInventory().getArmorContents();
    }

    public void restorePlayer() {
	player.teleport(location);
	player.setFallDistance(fallDistance);
	player.setTotalExperience(totalExp);
	player.setFoodLevel(foodLevel);
	player.setGameMode(gameMode);
	player.getInventory().setArmorContents(armor);
	player.getInventory().setContents(inventory);
    }

    public void setToGameDefaults() {
	player.getInventory().clear();
	player.setHealth(20);
	player.setFoodLevel(20);
	player.setAllowFlight(false);
	player.setFlying(false);
	player.setGameMode(GameMode.ADVENTURE);
	player.setFireTicks(0);
	player.setTotalExperience(0);
    }

    public void wipeData() {
	player = null;
	location = null;
	inventory = null;
	armor = null;
    }
}