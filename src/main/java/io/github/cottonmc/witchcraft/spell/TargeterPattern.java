package io.github.cottonmc.witchcraft.spell;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public interface TargeterPattern {
	/**
	 * @param spell The spell to add this to.
	 * @return Whether this targeter can be added to the spell.
	 */
	default boolean canAddTo(Spell spell) {
		return !spell.getTargeters().contains(this) && spell.getAction().canAcceptTargeter(this);
	}

	/**
	 * Add entity targets for the spell to affect.
	 * @param world The world to check in.
	 * @param pos The position the spell is at.
	 * @param activator The player activating the spell.
	 * @param spell The spell to be activated.
	 * @return Entities that the spell will apply to.
	 */
	 List<Entity> getTargets(World world, BlockPos pos, PlayerEntity activator, Spell spell);
}
