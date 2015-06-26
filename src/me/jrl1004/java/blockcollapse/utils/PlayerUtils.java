package me.jrl1004.java.blockcollapse.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.server.v1_8_R2.AxisAlignedBB;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class PlayerUtils {

	private PlayerUtils() {
	}

	public static Block[] getSupportingBlocks(Player player) {
		final AxisAlignedBB playerBB = NMSTools.getEntityBoundingBox(NMSTools.getPlayerHandle(player)).grow(0, 0.15, 0); // So I don't accidentally reassign it
		Map<Block, AxisAlignedBB> blockBoxes = new HashMap<Block, AxisAlignedBB>();
		ArrayList<Block> supportingBlocks = new ArrayList<Block>();
		// Starting at the lowest corner and iterating up
		final Location cornerLoc = player.getLocation().add(-1, -1, -1);
		for (int x = 0; x < 3; x++) {
			for (int z = 0; z < 3; z++) {
				Block block = cornerLoc.clone().add(x, 0, z).getBlock();
				if (block.getType() != Material.AIR) // You can't stand on air... normally
				blockBoxes.put(block, NMSTools.getBlockBoundingBox(block));
			}
		}
		if (blockBoxes.isEmpty()) return new Block[0]; // This player is literally a wizard
		for (Block aabb : blockBoxes.keySet()) {
			if (playerBB.b(blockBoxes.get(aabb))) supportingBlocks.add(aabb);
		}
		return supportingBlocks.toArray(new Block[supportingBlocks.size()]);
	}
}
