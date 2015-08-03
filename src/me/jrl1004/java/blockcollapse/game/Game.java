package me.jrl1004.java.blockcollapse.game;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import me.jrl1004.java.blockcollapse.utilities.ConfigurationUtils;
import me.jrl1004.java.blockcollapse.utilities.GameException;
import me.jrl1004.java.blockcollapse.utilities.MessageManager;
import me.jrl1004.java.blockcollapse.utilities.NMSTools;
import me.jrl1004.java.blockcollapse.utilities.PlayerData;

import org.apache.commons.lang3.Validate;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.bukkit.selections.Selection;

public class Game {

	private final int id;
	private HashMap<Player, PlayerData> players;
	private Selection playArea;
	private Location teleportLocation;
	private boolean running = false;
	private String gameName;
	private Stack<BlockState> regenQueue;
	private HashSet<BlockState> markedBlocks;

	// Constructors

	public Game(int id) {
		this.id = id;
		players = new HashMap<Player, PlayerData>();
		playArea = null;
		teleportLocation = null;
		this.regenQueue = new Stack<BlockState>();
		this.markedBlocks = new HashSet<BlockState>();
		gameName = null;
	}

	public Game(int id, File gameFile) {
		Validate.notNull(gameFile);
		this.id = id;
		players = new HashMap<Player, PlayerData>();
		regenQueue = new Stack<BlockState>();
		markedBlocks = new HashSet<BlockState>();
		// Load the configuration
		YamlConfiguration config = YamlConfiguration.loadConfiguration(gameFile);
		playArea = ConfigurationUtils.getPlayArea(config);
		teleportLocation = ConfigurationUtils.getTeleportLocation(config);
		gameName = ConfigurationUtils.getGameName(config);
	}

	// Game Methods

	public void startGame() throws GameException {
		if (id == -1) throw new GameException("Game ID must be a non-negative integer");
		if (playArea == null) throw new GameException("Game does not yet have a valid play area");
		if (teleportLocation == null) throw new GameException("Game does not have a valid spawn location");
		this.running = true;
	}

	@SuppressWarnings("deprecation")
	public void endGame() {
		running = false;
		for (Player p : players.keySet()) {
			players.get(p).restorePlayer();
			players.get(p).wipeData();
		}
		players.clear();
		while (!regenQueue.isEmpty()) {
			BlockState state = regenQueue.pop();
			state.getBlock().setTypeIdAndData(state.getTypeId(), state.getRawData(), false);
		}
	}

	public void gameTick() {
		if (!running) return;
		if (!markedBlocks.isEmpty()) {
			for (BlockState state : markedBlocks) {
				regenQueue.add(state);
				state.getBlock().setType(Material.AIR);
			}
			markedBlocks.clear();
		}
		for (Player player : players.keySet()) {
			if (!((Entity) player).isOnGround()) continue;
			Block block = player.getLocation().getBlock().getRelative(BlockFace.DOWN);
			if (block.getType() == Material.AIR) {
				Block[] arr = NMSTools.getSupportingBlocks(player);
				if (arr.length == 0) {
					//TODO add cheating
					continue;
				}
				block = arr[0];
			}
			markedBlocks.add(block.getState());
		}
	}

	public void addPlayer(Player player) {
		players.put(player, new PlayerData(player));
		messagePlayers(player.getName() + " has joined the game");
	}

	private void messagePlayers(String... message) {
		if (getPlayers().isEmpty()) return;
		for (Player p : getPlayers()) {
			MessageManager.messagePrefixed(p, message);
		}
	}

	// Getters and Setters

	public Set<Player> getPlayers() {
		return Collections.unmodifiableSet(players.keySet());
	}

	public Selection getPlayArea() {
		return playArea;
	}

	public void setPlayArea(Selection playArea) {
		this.playArea = playArea;
	}

	public Location getTeleportLocation() {
		return teleportLocation;
	}

	public void setTeleportLocation(Location teleportLocation) {
		this.teleportLocation = teleportLocation;
	}

	public boolean isRunning() {
		return running;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public int getId() {
		return id;
	}

	public void removePlayer(Player player) {
		if (!getPlayers().contains(player)) return;
		messagePlayers(player.getName() + " has left the game");
		PlayerData data = players.get(player);
		players.remove(player);
		data.restorePlayer();
		data.wipeData();
		if (getPlayers().size() < 2) endGame();
	}

	public void saveGameSettingsToFile() throws GameException, IOException {
		if (gameName == null) throw new GameException("Game must be assigned a name before it can be saved");
		ConfigurationUtils.saveGameToFile(this);
	}

	public String getIdentifier() {
		if (getGameName() != null && getGameName().length() > 0) return "\"" + getGameName() + "\"";
		return getId() + "";
	}
}
