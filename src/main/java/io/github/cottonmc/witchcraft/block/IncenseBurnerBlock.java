package io.github.cottonmc.witchcraft.block;

import io.github.cottonmc.witchcraft.block.entity.IncenseBurnerEntity;
import io.github.cottonmc.witchcraft.item.IncenseStickItem;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.Random;

public class IncenseBurnerBlock extends Block implements BlockEntityProvider {
	public IncenseBurnerBlock() {
		super(FabricBlockSettings.of(Material.WOOD).breakByHand(true).build());
	}

	@Override
	public BlockEntity createBlockEntity(BlockView view) {
		return new IncenseBurnerEntity();
	}

	@Override
	public boolean activate(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		ItemStack handStack = player.getStackInHand(hand);
		BlockEntity be = world.getBlockEntity(pos);
		if (world.isClient || !(be instanceof IncenseBurnerEntity)) return true;
		IncenseBurnerEntity burner = (IncenseBurnerEntity) be;
		//don't do anything if incense is already burning!
		if (world.getBlockTickScheduler().isScheduled(pos, WitchcraftBlocks.INCENSE_BURNER)
				|| world.getBlockTickScheduler().isTicking(pos, WitchcraftBlocks.INCENSE_BURNER)) return true;
		if (burner.hasIncense()) {
			if (handStack.getItem() == Items.FLINT_AND_STEEL) {
				burner.light();
				world.playSound(null, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0f, 1.0f);
				return true;
			} else if (handStack.isEmpty()) {
				player.setStackInHand(hand, burner.removeIncense());
				return true;
			}
			//eventually change to IncenseItem or whatever
		} else if (handStack.getItem() instanceof IncenseStickItem) {
			burner.setIncense(handStack);
			handStack.decrement(1);
			world.playSound(null, pos, SoundEvents.ENTITY_ITEM_FRAME_ADD_ITEM, SoundCategory.BLOCKS, 1.0f, 1.0f);
			return true;
		}
		if (handStack.getItem() == Items.FLINT_AND_STEEL) return true;
		return false;
	}

	@Override
	public void onScheduledTick(BlockState state, World world, BlockPos pos, Random rand) {
		IncenseBurnerEntity be = (IncenseBurnerEntity)world.getBlockEntity(pos);
		if (be != null) {
			be.purge();
		}
	}
}
