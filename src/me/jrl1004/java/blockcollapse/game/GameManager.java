package me.jrl1004.java.blockcollapse.game;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import me.jrl1004.java.blockcollapse.BlockCollapse;
import me.jrl1004.java.blockcollapse.utilities.ConfigurationUtils;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class GameManager {

	private static GameManager instance;

	private ArrayList<Game> games;

	private GameManager() {
		games = new ArrayList<Game>();
	}

	public static GameManager getGameManager() {
		if (instance == null) instance = new GameManager();
		return instance;
	}

	public List<Game> getGames() {
		return Collections.unmodifiableList(games);
	}

	public Game getPlayerGame(OfflinePlayer player) {
		List<Game> gameList = getGames();
		if (gameList.isEmpty()) return null;
		for (Game game : gameList) {
			Set<Player> gamePlayers = game.getPlayers();
			if (gamePlayers.isEmpty()) continue;
			if (gamePlayers.contains(player)) return game;
		}
		return null;
	}

	public Game getByID(int id) {
		List<Game> gameList = getGames();
		if (gameList.isEmpty()) return null;
		for (Game game : gameList) {
			if (game.getId() == id) return game;
		}
		return null;
	}

	public Game getByName(String string) {
		List<Game> gameList = getGames();
		if (gameList.isEmpty()) return null;
		for (Game game : gameList) {
			if (game.getGameName() != null && game.getGameName().equalsIgnoreCase(string)) return game;
		}
		return null;
	}

	public Game getNewGame() {
		Game game = new Game(games.size());
		games.add(game);
		return game;
	}

	public void startTicks() {
		new BukkitRunnable() {
			public void run() {
				if (getGames().isEmpty()) return;
				for (Game game : getGames()) {
					if (!game.isRunning()) continue;
					game.gameTick();
				}
			}
		}.runTaskTimer(BlockCollapse.getBlockCollapse(), 0, 5);
	}

	public Game getFromString(String string) {
		Game game = null;
		game = getByName(string);
		if (game != null) return game;
		int i = readInt(string);
		if (i == -1) return null;
		return getByID(i);
	}

	private int readInt(String str) {
		str = str.replaceAll("[^0-9]", "");
		int i = -1;
		try {
			i = Integer.parseInt(str);
		} catch (NumberFormatException exc) {
			i = -1;
		}
		return i;
	}

	public void loadSavedGames() {
		File gameFolder = ConfigurationUtils.getSaveFolder();
		File[] gameFiles = gameFolder.listFiles();
		if (gameFiles.length == 0) return;
		for (File file : gameFiles) {
			Game game = new Game(games.size(), file);
			games.add(game);
		}
	}
}
