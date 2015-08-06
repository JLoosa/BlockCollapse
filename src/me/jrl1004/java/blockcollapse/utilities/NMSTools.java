package me.jrl1004.java.blockcollapse.utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.server.v1_8_R3.AxisAlignedBB;
import net.minecraft.server.v1_8_R3.Block;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.World;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class NMSTools {

	private NMSTools() {
	}

	public static Entity getEntityHandle(org.bukkit.entity.Entity entity) {
		if (entity instanceof CraftEntity) ((CraftEntity) entity).getHandle();
		return null;
	}

	public static org.bukkit.block.Block[] getSupportingBlocks(Player player) {
		final AxisAlignedBB playerBB = NMSTools.getEntityBoundingBox(NMSTools.getPlayerHandle(player)).grow(0, 0.15, 0); // So I don't accidentally reassign it
		Map<org.bukkit.block.Block, AxisAlignedBB> blockBoxes = new HashMap<org.bukkit.block.Block, AxisAlignedBB>();
		ArrayList<org.bukkit.block.Block> supportingBlocks = new ArrayList<org.bukkit.block.Block>();
		// Starting at the lowest corner and iterating up
		final Location cornerLoc = player.getLocation().add(-1, -1, -1);
		for (int x = 0; x < 3; x++) {
			for (int z = 0; z < 3; z++) {
				org.bukkit.block.Block block = cornerLoc.clone().add(x, 0, z).getBlock();
				if (block.getType() != Material.AIR) // You can't stand on air... normally
				blockBoxes.put(block, NMSTools.getBlockBoundingBox(block));
			}
		}
		if (blockBoxes.isEmpty()) return new org.bukkit.block.Block[0]; // This player is literally a wizard
		for (org.bukkit.block.Block aabb : blockBoxes.keySet()) {
			if (playerBB.b(blockBoxes.get(aabb))) supportingBlocks.add(aabb);
		}
		return supportingBlocks.toArray(new org.bukkit.block.Block[supportingBlocks.size()]);
	}

	public static EntityPlayer getPlayerHandle(Player player) {
		if (player instanceof CraftPlayer) return ((CraftPlayer) player).getHandle();
		return null;
	}

	public static Block getNMSBlock(int id) {
		Block block = Block.getById(id);
		return block;
	}

	public static World getNMSWorld(org.bukkit.World world) {
		if (world instanceof CraftWorld) return ((CraftWorld) world).getHandle();
		return null;
	}

	public static AxisAlignedBB getEntityBoundingBox(Entity entity) {
		return entity.getBoundingBox();
	}

	public static AxisAlignedBB getEntityBoundingBox(org.bukkit.entity.Entity entity) {
		Entity nmsEntity = getEntityHandle(entity);
		return getEntityBoundingBox(nmsEntity);
	}

	public static AxisAlignedBB getBlockBoundingBox(org.bukkit.block.Block block) {
		@SuppressWarnings("deprecation")
		Block nmsBlock = getNMSBlock(block.getTypeId());
		BlockPosition blockPosition = new BlockPosition(block.getX(), block.getY(), block.getZ());
		AxisAlignedBB boundingBox = nmsBlock.a(getNMSWorld(block.getWorld()), blockPosition, nmsBlock.getBlockData());
		return boundingBox;
	}
}