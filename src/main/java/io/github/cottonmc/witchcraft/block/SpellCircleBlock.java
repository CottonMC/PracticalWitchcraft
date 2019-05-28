package io.github.cottonmc.witchcraft.block;

import io.github.cottonmc.witchcraft.block.entity.SpellCircleEntity;
import io.github.cottonmc.witchcraft.item.WitchcraftItems;
import io.github.cottonmc.witchcraft.spell.Spell;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.Random;

public class SpellCircleBlock extends Block implements BlockEntityProvider {

	public SpellCircleBlock() {
		super(FabricBlockSettings.of(Material.CARPET).noCollision().build());
	}

	@Override
	public boolean activate(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (!world.isClient && (world.getBlockEntity(pos) instanceof SpellCircleEntity)) {
			if (world.getBlockTickScheduler().isScheduled(pos, WitchcraftBlocks.SPELL_CIRCLE)
					|| world.getBlockTickScheduler().isTicking(pos, WitchcraftBlocks.SPELL_CIRCLE)) return true;
			ItemStack stack = player.getStackInHand(hand);
			SpellCircleEntity be = (SpellCircleEntity) world.getBlockEntity(pos);
			if (be.hasPixie() && stack.getItem() == WitchcraftItems.BROOMSTICK) be.beginSpell(player);
			else if (stack.getItem() == WitchcraftItems.BOTTLED_PIXIE) {
				ItemStack bottle = new ItemStack(Items.GLASS_BOTTLE);
				if (!player.isCreative()) {
					if (stack.getAmount() == 1) player.setStackInHand(hand, bottle);
					else if (!player.inventory.insertStack(bottle)) {
						ItemEntity entity = player.dropItem(bottle, false);
						world.spawnEntity(entity);
					}
				}
				be.addPixie();
			}
		}
		return true;
	}

	@Override
	public void onScheduledTick(BlockState state, World world, BlockPos pos, Random rad) {
		if (world.getBlockEntity(pos) instanceof SpellCircleEntity) {
			((SpellCircleEntity)world.getBlockEntity(pos)).performSpell();
		}
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		BlockEntity be = world.getBlockEntity(pos);
		if (be instanceof SpellCircleEntity && stack.getOrCreateTag().containsKey("Spell", NbtType.COMPOUND)) {
			SpellCircleEntity circle = (SpellCircleEntity)be;
			Spell spell = Spell.fromTag(stack.getTag().getCompound("Spell"));
			circle.setSpell(spell);
		}
	}

	@Override
	public BlockEntity createBlockEntity(BlockView view) {
		return new SpellCircleEntity();
	}
}
