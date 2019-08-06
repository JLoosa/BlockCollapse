package me.jrl1004.java.blockcollapse.utilities;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class PlayerTools {
    private static final float PLAYER_WIDTH, PLAYER_WIDTH_HALF;
    static {
	PLAYER_WIDTH = 0.6f;
	PLAYER_WIDTH_HALF = PLAYER_WIDTH / 2f;
    }

    private PlayerTools() {
    }

    public static Block[] getSupportingBlocks(Player player) {
	Block[] blocks = new Block[4];
	blocks[0] = player.getLocation().add(PLAYER_WIDTH_HALF, -1, PLAYER_WIDTH_HALF).getBlock();
	blocks[1] = player.getLocation().add(-PLAYER_WIDTH_HALF, -1, PLAYER_WIDTH_HALF).getBlock();
	blocks[2] = player.getLocation().add(PLAYER_WIDTH_HALF, -1, -PLAYER_WIDTH_HALF).getBlock();
	blocks[3] = player.getLocation().add(-PLAYER_WIDTH_HALF, -1, -PLAYER_WIDTH_HALF).getBlock();
	return removeNonsolidBlocks(blocks);
    }

    private static Block[] removeNonsolidBlocks(Block[] in) {
	int count = 0;
	for (Block b : in)
	    if (b.getType().isSolid())
		count++;
	Block[] out = new Block[count];
	count = 0;
	for (Block b : in)
	    if (b.getType().isSolid())
		out[count++] = b;
	return out;
    }
}
