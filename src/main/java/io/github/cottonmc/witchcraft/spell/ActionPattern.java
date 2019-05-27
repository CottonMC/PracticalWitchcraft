package io.github.cottonmc.witchcraft.spell;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ActionPattern {

	int performAction(World world, BlockPos pos, PlayerEntity activator, Spell spell, int performances);

	int getSpellDuration(World world, BlockPos pos, PlayerEntity activator, Spell spell);

	int getSpellCharge(World world, BlockPos pos, PlayerEntity activator, Spell spell);

}
