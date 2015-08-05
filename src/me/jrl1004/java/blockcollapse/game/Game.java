package me.jrl1004.java.blockcollapse.game;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import me.jrl1004.java.blockcollapse.utilities.BCConfig;
import me.jrl1004.java.blockcollapse.utilities.GameConfig;
import me.jrl1004.java.blockcollapse.utilities.GameException;
import me.jrl1004.java.blockcollapse.utilities.MessageManager;
import me.jrl1004.java.blockcollapse.utilities.NMSTools;
import me.jrl1004.java.blockcollapse.utilities.PlayerData;

import org.apache.commons.lang3.Validate;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.bukkit.selections.Selection;

@SuppressWarnings("deprecation")
public class Game {

	private final int id;
	private HashMap<Player, PlayerData> players;
	private ArrayList<Player> alivePlayers;
	private Selection playArea;
	private Location teleportLocation;
	private String gameName;
	private Stack<BlockState> regenQueue;
	private HashSet<BlockState> markedBlocks;
	private int lobbyTime = 0;
	private int graceTime = 0;
	private int maxPlayers = 0;
	private int blockBreakTicks = 0;
	private GameState state;

	// Constructors

	public Game(int id) {
		this.id = id;
		players = new HashMap<Player, PlayerData>();
		alivePlayers = new ArrayList<Player>();
		playArea = null;
		teleportLocation = null;
		this.regenQueue = new Stack<BlockState>();
		this.markedBlocks = new HashSet<BlockState>();
		lobbyTime = BCConfig.getLobbyTime();
		graceTime = BCConfig.getGracePeriod();
		maxPlayers = BCConfig.getMaxPlayers();
		blockBreakTicks = BCConfig.getTickDelay();
		state = GameState.DISABLED;
		gameName = null;
	}

	public Game(int id, File gameFile) {
		// Load the defaults
		this(id);
		// Load the configuration
		Validate.notNull(gameFile);
		YamlConfiguration config = YamlConfiguration.loadConfiguration(gameFile);
		playArea = GameConfig.getPlayArea(config);
		teleportLocation = GameConfig.getTeleportLocation(config);
		gameName = GameConfig.getGameName(config);
	}

	// Game Methods

	public void startGame() throws GameException {
		if (id == -1) throw new GameException("Game ID must be a non-negative integer");
		if (playArea == null) throw new GameException("Game does not yet have a valid play area");
		if (teleportLocation == null) throw new GameException("Game does not have a valid spawn location");
		if (!playArea.contains(teleportLocation)) throw new GameException("Game spawn point is not within the arena!");
		state = GameState.LOBBY;
	}

	private int $currentTick = 0;

	public void gameTick() {
		if (state == GameState.DISABLED) return;
		if (state == GameState.ENDING) return;
		if (state == GameState.RESETTING) return;
		++$currentTick;
		if (state == GameState.LOBBY) {
			if (getPlayers().size() < 2) $currentTick = 0;
			if ($currentTick >= lobbyTime) {
				$currentTick -= lobbyTime;
				state = GameState.GRACE;
				alivePlayers.clear();
				for (Player p : getPlayers()) {
					alivePlayers.add(p);
					p.teleport(teleportLocation);
				}
			}
			return;
		}
		if (state == GameState.GRACE) {
			if ($currentTick >= graceTime) {
				$currentTick -= graceTime;
				state = GameState.INGAME;
			}
			return;
		}
		if (state == GameState.INGAME) {
			if ($currentTick >= blockBreakTicks) {
				$currentTick -= blockBreakTicks;
				runBlockRemovalTick();
			}
		}
	}

	private void runBlockRemovalTick() {
		if (!markedBlocks.isEmpty()) {
			for (BlockState state : markedBlocks) {
				regenQueue.add(state);
				state.getWorld().playEffect(state.getLocation(), Effect.STEP_SOUND, state.getTypeId());
				state.getBlock().setType(Material.AIR);
			}
			markedBlocks.clear();
		}
		for (Player player : alivePlayers) {
			if (!playArea.contains(player.getLocation())) {
				messagePlayers(player.getName() + " has fallen out of the arena");
				markAsDead(player);
			}
			if (!alivePlayers.contains(player)) continue;
			if (!((Entity) player).isOnGround()) continue;
			Block block = player.getLocation().getBlock().getRelative(BlockFace.DOWN);
			if (block.getType() == Material.AIR) {
				Block[] arr = NMSTools.getSupportingBlocks(player);
				if (arr.length == 0) {
					messagePlayers(player.getName() + " was flying!");
					player.setFlying(false);
					player.setAllowFlight(false);
					continue;
				}
				block = arr[0];
			}
			markedBlocks.add(block.getState());
		}
	}

	public void endGame(Player winner) {
		state = GameState.ENDING;
		for (Player p : players.keySet()) {
			players.get(p).restorePlayer();
			players.get(p).wipeData();
		}
		if (winner != null) messagePlayers("Game Over! " + winner.getName() + " has won!");
		else
			messagePlayers("You game has been force-ended");
		players.clear();
		alivePlayers.clear();
		resetMap();
	}

	public void resetMap() {
		state = GameState.RESETTING;
		while (!regenQueue.isEmpty()) {
			BlockState state = regenQueue.pop();
			state.getBlock().setTypeIdAndData(state.getTypeId(), state.getRawData(), false);
		}
		state = GameState.LOBBY;
	}

	public void setState(GameState state) {
		this.state = state;
	}

	// Player Control

	public void addPlayer(Player player) {
		if (getPlayers().size() >= maxPlayers) {
			MessageManager.messagePrefixed(player, "This game is full");
			return;
		}
		players.put(player, new PlayerData(player));
		players.get(player).setToGameDefaults();
		alivePlayers.add(player);
		messagePlayers(player.getName() + " has joined the game", getIdentifier() + " now has " + getPlayers().size() + " players");
	}

	private void markAsDead(Player player) {
		alivePlayers.remove(player);
		player.setGameMode(GameMode.SPECTATOR);
		if (alivePlayers.size() < 2) endGame(alivePlayers.size() == 1 ? alivePlayers.get(0) : null);
	}

	public void removePlayer(Player player) {
		if (!getPlayers().contains(player)) return;
		messagePlayers(player.getName() + " has left the game");
		PlayerData data = players.get(player);
		players.remove(player);
		markAsDead(player);
		data.restorePlayer();
		data.wipeData();
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

	public GameState getState() {
		return state;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) throws GameException {
		if (GameManager.getGameManager().getByName(gameName) != null) throw new GameException("A game by that name already exists");
		this.gameName = gameName;
	}

	public int getId() {
		return id;
	}

	public void saveGameSettingsToFile() throws GameException, IOException {
		if (gameName == null) throw new GameException("Game must be assigned a name before it can be saved");
		GameConfig.saveGameToFile(this);
	}

	public String getIdentifier() {
		if (getGameName() != null && getGameName().length() > 0) return getGameName();
		return "[UNNAMED]-" + getId();
	}
}
