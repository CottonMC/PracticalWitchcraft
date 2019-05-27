package io.github.cottonmc.witchcraft.block;

import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.world.BlockView;

public class SpellCircleBlock extends Block implements BlockEntityProvider {

	public SpellCircleBlock() {
		super(FabricBlockSettings.of(Material.CARPET).noCollision().build());
	}

	@Override
	public BlockEntity createBlockEntity(BlockView view) {
		return null;
	}
}
