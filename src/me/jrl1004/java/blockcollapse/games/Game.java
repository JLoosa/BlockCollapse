package me.jrl1004.java.blockcollapse.games;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

import me.jrl1004.java.blockcollapse.maps.Map;
import me.jrl1004.java.blockcollapse.utils.BCConstants;
import me.jrl1004.java.blockcollapse.utils.PlayerData;
import me.jrl1004.java.blockcollapse.utils.PlayerUtils;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class Game {

	private final Map				map;
	private ArrayList<Sign>			gameSigns;
	private GameState				state;
	private ArrayList<Player>		players;
	private ArrayList<Player>		deadPlayers;
	private int						maxPlayers;
	private int						gameTime;
	private Stack<BlockState>		backups;
	private ArrayList<Block>		markedBlocks;
	private ArrayList<PlayerData>	playerBackups;

	public Game(Map map, Sign... joinSigns) {
		this.map = map;
		gameSigns = new ArrayList<Sign>(Arrays.asList(joinSigns));
		state = GameState.Lobby;
		players = new ArrayList<Player>();
		deadPlayers = new ArrayList<Player>();
		maxPlayers = map.getMaxPlayers();
		gameTime = 0;
		backups = new Stack<BlockState>();
		markedBlocks = new ArrayList<Block>();
		playerBackups = new ArrayList<PlayerData>();

		updateSigns();
	}

	// Getters

	public Map getMap() {
		return map;
	}

	public String getName() {
		return map.getName();
	}

	public boolean hasPlayer(Player player) {
		return players.contains(player) || deadPlayers.contains(player);
	}

	public int getAllPlayers() {
		return players.size() + deadPlayers.size();
	}

	// Methods

	public String addPlayer(Player player) {
		if (state != GameState.Lobby) return "Game is only joinable while in lobby";
		if (getAllPlayers() >= maxPlayers) return "Game is full";
		players.add(player);
		playerBackups.add(new PlayerData(player));
		player.teleport(map.getTeleportLocation());
		messageAllPlayers(player.getName() + " has joined the game");
		return "You are now in game: " + map.getName();
	}

	public void messageAllPlayers(String... msg) {
		if (msg.length == 0) return;
		messageLivingPlayers(msg);
		messageDeadPlayers(msg);
	}

	public void messageLivingPlayers(String... msg) {
		if (msg.length == 0) return;
		if (players.size() == 0) return;
		for (Player p : players)
			p.sendMessage(msg);
	}

	public void messageDeadPlayers(String... msg) {
		if (msg.length == 0) return;
		if (deadPlayers.size() == 0) return;
		for (Player p : deadPlayers)
			p.sendMessage(msg);
	}

	public void updateSigns() {
		if (gameSigns.isEmpty()) return;
		for (Sign s : gameSigns) {
			if (!s.getChunk().isLoaded()) continue;
			s.setLine(0, BCConstants.SIGN_TITLE_COLORED);
			s.setLine(1, map.getName());
			s.setLine(2, state.toString());
			s.setLine(3, (players.size() + deadPlayers.size()) + " / " + maxPlayers);
			s.update(true, false);
		}
	}

	public void onTick() { //Method to be run when the game checks it's values
		++gameTime;
		if (state == GameState.Lobby) {
			lobbyTick();
			return;
		}
		if (state == GameState.Grace) {
			graceTick();
			return;
		}
		if (state == GameState.Ingame) {
			gameTick();
			return;
		}
	}

	private void lobbyTick() {
		double minPlayers = maxPlayers / 4;
		if (players.size() >= minPlayers) startGame();
	}

	private void graceTick() {
		if (gameTime >= 20 * 10) {
			state = GameState.Ingame;
			gameTime = 0;
		}
	}

	private void gameTick() {
		clearMarkedBlocks();
		if (gameTime >= 20 * 60 * 5) {
			endGame();
			return;
		}
		markBlocks();
	}

	public void clearMarkedBlocks() {
		if (markedBlocks.isEmpty()) return;
		for (Block block : markedBlocks) {
			backups.push(block.getState());
			block.setType(Material.AIR);
		}
		markedBlocks.clear();
	}

	public void markBlocks() {
		if (players.isEmpty()) return;
		for (Player p : players) {
			if (!((Entity) p).isOnGround()) continue; // Player is jumping
			Block blockBelow = p.getLocation().getBlock().getRelative(BlockFace.DOWN);
			if (blockBelow.getType() != Material.AIR) {
				markedBlocks.add(blockBelow);
			}
			else {
				Block[] support = PlayerUtils.getSupportingBlocks(p);
				if (support.length == 0) continue; //TODO add anti-cheat
				blockBelow = support[0];
			}
		}
	}

	public void startGame() {
		state = GameState.Grace;
		if (!players.isEmpty()) for (Player p : players)
			p.teleport(map.getTeleportLocation());
		messageAllPlayers("Game Starting");
		gameTime = 0;
	}

	@SuppressWarnings("deprecation")
	public void endGame() {
		messageAllPlayers("Game Over!");
		if (!playerBackups.isEmpty()) {
			for (PlayerData d : playerBackups) {
				d.restorePlayer();
				d.wipeData();
			}
		}
		playerBackups.clear();
		players.clear();
		deadPlayers.clear();
		state = GameState.Resetting;
		updateSigns();
		while (!backups.isEmpty()) {
			BlockState bs = backups.pop();
			bs.getBlock().setTypeIdAndData(bs.getTypeId(), bs.getRawData(), true);
		}
		gameTime = 0;
		state = GameState.Lobby;
	}
}
