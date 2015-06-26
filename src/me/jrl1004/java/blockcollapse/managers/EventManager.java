package me.jrl1004.java.blockcollapse.managers;

import me.jrl1004.java.blockcollapse.BlockCollapse;
import me.jrl1004.java.blockcollapse.games.Game;

import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class EventManager implements Listener {

	public EventManager() {
	}

	@EventHandler
	public void onClick(PlayerInteractEvent event) {
		if (!event.hasBlock()) return;
		final Block b = event.getClickedBlock();
		final Player p = event.getPlayer();
		if(!(b.getState() instanceof Sign)) return;
		Sign s = (Sign) b.getState();
		Game game = BlockCollapse.get().getGameManager().getGameFromSign(s);
		if(game == null) return;
		p.sendMessage(game.addPlayer(p));
	}
}
