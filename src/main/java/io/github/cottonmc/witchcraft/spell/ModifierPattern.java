package io.github.cottonmc.witchcraft.spell;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ModifierPattern {

	default boolean canAddTo(Spell spell) {
		return !spell.getModifiers().contains(this);
	}

	int getModifierValue(World world, BlockPos pos, PlayerEntity activator, Spell spell);

}
