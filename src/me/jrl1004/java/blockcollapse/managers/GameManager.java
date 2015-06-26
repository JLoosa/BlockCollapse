package me.jrl1004.java.blockcollapse.managers;

import java.util.ArrayList;

import me.jrl1004.java.blockcollapse.BlockCollapse;
import me.jrl1004.java.blockcollapse.games.Game;
import me.jrl1004.java.blockcollapse.utils.BCConstants;

import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.scheduler.BukkitRunnable;

public class GameManager {
	private ArrayList<Game>	currentGames;

	public GameManager() {
		currentGames = new ArrayList<Game>();
		startTicks();
	}

	public boolean addGame(Game game) {
		if (currentGames.isEmpty()) {
			currentGames.add(game);
			return true;
		}
		for (Game g : currentGames) {
			if (g.getName().equals(game.getName())) return false;
		}
		currentGames.add(game);
		return true;
	}

	private void startTicks() {
		new BukkitRunnable() {
			public void run() {
				if (currentGames.isEmpty()) return;
				for (Game g : currentGames)
					g.onTick();
			}
		}.runTaskTimer(BlockCollapse.get(), 0, 1);
	}

	public void endAllGames() {
		if (currentGames.isEmpty()) return;
		for (Game g : currentGames)
			g.endGame();
	}

	public void messageAllIngame(String... msg) {
		if (msg.length == 0 || currentGames.isEmpty()) return;
		for (Game g : currentGames)
			g.messageAllPlayers(msg);
	}

	public Game getGameFromSign(Sign sign) {
		String[] signText = cleantext(sign.getLines());
		if (signText.length != 4) return null;
		if (!signText[0].equals(cleantext(BCConstants.SIGN_TITLE_COLORED))) return null;
		return getGameByName(signText[2]);
	}

	public Game getGameByName(String name) {
		if (currentGames.isEmpty()) return null;
		for (Game g : currentGames)
			if (g.getName().equalsIgnoreCase(name)) return g;
		return null;
	}

	private String[] cleantext(String... lines) {
		if (lines.length == 0) return new String[0];
		for (int i = 0; i < lines.length; i++)
			lines[i] = ChatColor.stripColor(lines[i]).toLowerCase();
		return lines;
	}
}
