package io.github.cottonmc.witchcraft.spell;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ModifierPattern {

	/**
	 * @param spell The spell to add this to.
	 * @return Whether this modifier can be added to the spell.
	 */
	default boolean canAddTo(Spell spell) {
		return !spell.getModifiers().contains(this) && spell.getAction().canAcceptModifier(this);
	}

	/**
	 * Boost a spell's abilities.
	 * @param world The world the spell is being cast in.
	 * @param pos The position the spell is at.
	 * @param activator The player activating the spell.
	 * @param spell The spell being activated.
	 * @return The amount to boost this modifier's property by.
	 */
	int getModifierValue(World world, BlockPos pos, PlayerEntity activator, Spell spell);

}
