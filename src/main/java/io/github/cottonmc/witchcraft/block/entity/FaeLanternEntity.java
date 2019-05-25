package io.github.cottonmc.witchcraft.block.entity;

import io.github.cottonmc.witchcraft.block.FaeLanternBlock;
import io.github.cottonmc.witchcraft.block.WitchcraftBlocks;
import net.minecraft.block.entity.BlockEntity;

public class FaeLanternEntity extends BlockEntity {
	public FaeLanternEntity() {
		super(WitchcraftBlocks.FAE_LANTERN_BE);
	}

	public int getPixies() {
		return world.getBlockState(pos).get(FaeLanternBlock.PIXIES);
	}
}
