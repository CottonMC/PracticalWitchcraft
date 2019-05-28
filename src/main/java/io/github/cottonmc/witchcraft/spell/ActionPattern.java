package io.github.cottonmc.witchcraft.spell;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ActionPattern {

	/**
	 * Perform a spell action.
	 * @param world The world to perform in.
	 * @param pos The position to perform at.
	 * @param activator The player who activated the spell.
	 * @param spell The spell being performed.
	 * @param performances How many times this spell has been performed before this.
	 * @return How many ticks until the next performance, or 0 to finish performing.
	 */
	int performAction(World world, BlockPos pos, PlayerEntity activator, Spell spell, int performances);

	/**
	 * @param pattern The targeter to be added.
	 * @return Whether the targeter can apply to this action.
	 */
	boolean canAcceptTargeter(TargeterPattern pattern);

	/**
	 * @param pattern The modifier to be added.
	 * @return Whether the modifier can apply to this action.
	 */
	boolean canAcceptModifier(ModifierPattern pattern);

}
