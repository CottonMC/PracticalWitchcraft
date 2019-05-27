package io.github.cottonmc.witchcraft.spell;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public interface TargeterPattern {
	default boolean canAddTo(Spell spell) {
		return !spell.getTargeters().contains(this);
	}

	 List<Entity> getTargets(World world, BlockPos pos, PlayerEntity activator, Spell spell);
}
