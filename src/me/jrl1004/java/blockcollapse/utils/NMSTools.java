package me.jrl1004.java.blockcollapse.utils;

import net.minecraft.server.v1_8_R2.AxisAlignedBB;
import net.minecraft.server.v1_8_R2.Block;
import net.minecraft.server.v1_8_R2.BlockPosition;
import net.minecraft.server.v1_8_R2.Entity;
import net.minecraft.server.v1_8_R2.EntityPlayer;
import net.minecraft.server.v1_8_R2.World;

import org.bukkit.craftbukkit.v1_8_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class NMSTools {
	private NMSTools() {
	}

	public static Entity getEntityHandle(org.bukkit.entity.Entity entity) {
		if (entity instanceof CraftEntity) ((CraftEntity) entity).getHandle();
		return null;
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
