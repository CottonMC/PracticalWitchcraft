package io.github.cottonmc.witchcraft.block;

import alexiil.mc.lib.attributes.AttributeList;
import alexiil.mc.lib.attributes.AttributeProvider;
import alexiil.mc.lib.attributes.Simulation;
import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import alexiil.mc.lib.attributes.fluid.volume.NormalFluidVolume;
import io.github.cottonmc.witchcraft.util.BucketUtil;
import io.github.cottonmc.witchcraft.block.entity.StoneCauldronEntity;
import io.github.cottonmc.cotton.cauldron.Cauldron;
import io.github.cottonmc.cotton.cauldron.CauldronBehavior;
import io.github.cottonmc.cotton.cauldron.CauldronContext;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tools.FabricToolTags;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EntityContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.BooleanBiFunction;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.function.Predicate;

public class StoneCauldronBlock extends BlockWithEntity implements AttributeProvider, Cauldron {

	public static final VoxelShape RAY_TRACE_SHAPE = createCuboidShape(2.0D, 4.0D, 2.0D, 14.0D, 16.0D, 14.0D);
	public static final VoxelShape OUTLINE_SHAPE = VoxelShapes.combineAndSimplify(VoxelShapes.fullCube(), VoxelShapes.union(createCuboidShape(0.0D, 0.0D, 4.0D, 16.0D, 3.0D, 12.0D), new VoxelShape[]{createCuboidShape(4.0D, 0.0D, 0.0D, 12.0D, 3.0D, 16.0D), createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 3.0D, 14.0D), RAY_TRACE_SHAPE}), BooleanBiFunction.ONLY_FIRST);

	public StoneCauldronBlock() {
		super(FabricBlockSettings.of(Material.STONE).breakByTool(FabricToolTags.PICKAXES).strength(6.0f, 6.0f).build());
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, EntityContext entityPos) {
		return OUTLINE_SHAPE;
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Override
	public VoxelShape getRayTraceShape(BlockState state, BlockView view, BlockPos pos) {
		return RAY_TRACE_SHAPE;
	}

	@Override
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public BlockEntity createBlockEntity(BlockView view) {
		return new StoneCauldronEntity();
	}

	@Override
	public boolean activate(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		ItemStack stack = player.getStackInHand(hand);
		StoneCauldronEntity cauldron = (StoneCauldronEntity)world.getBlockEntity(pos);
		FluidVolume fluid = cauldron.fluid.getInvFluid(0);
		if (stack.isEmpty() && !(cauldron.previousItems.isEmpty()) && player.isSneaking()) {
			int index = getLastFilledSlot(cauldron.previousItems);
			if (index != -1) {
				player.setStackInHand(hand, cauldron.previousItems.get(index));
				cauldron.previousItems.set(index, ItemStack.EMPTY);
			}
			return true;
		}
		if (stack.getItem() instanceof BucketItem) {
			if (stack.getItem() == Items.BUCKET && fluid.getAmount() >= FluidVolume.BUCKET) {
				if (!player.isCreative()) player.setStackInHand(hand, BucketUtil.fillBucketFromFluid(stack, fluid.getRawFluid()));
				drain(world, pos, state, fluid.getRawFluid(), 3);
				world.playSound(player, pos, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1.0f, 1.0f);
				return true;
			}
			//TODO: remove restriction to water bucket only once renderer is fixed
			else if (fluid.isEmpty() && stack.getItem() == Items.WATER_BUCKET) {
				if (!player.isCreative()) player.setStackInHand(hand, new ItemStack(Items.BUCKET));
				cauldron.fluid.setInvFluid(0, BucketUtil.getBucketFluid(stack), Simulation.ACTION);
				world.playSound(player, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0f, 1.0f);
				return true;
			}
		}
		CauldronContext ctx = new CauldronContext(world, pos, state, fluid.getAmount() / FluidVolume.BOTTLE, fluid.getRawFluid(), cauldron.previousItems, player, hand, player.getStackInHand(hand));
		for (Predicate<CauldronContext> pred : CauldronBehavior.BEHAVIORS.keySet()) {
			if (pred.test(ctx)) {
				CauldronBehavior behavior = CauldronBehavior.BEHAVIORS.get(pred);
				behavior.react(ctx);
				cauldron.craft();
				return true;
			}
		}
		return super.activate(state, world, pos, player, hand, hit);
	}

	@Override
	public boolean fill(World world, BlockPos pos, BlockState state, Fluid fluid, int bottles) {
		int amount = FluidVolume.BOTTLE * bottles;
		StoneCauldronEntity cauldron = (StoneCauldronEntity)world.getBlockEntity(pos);
		FluidVolume vol = cauldron.fluid.getInvFluid(0);
		if (vol.getRawFluid().equals(fluid) && vol.getAmount() + amount <= FluidVolume.BUCKET) {
			vol.merge(NormalFluidVolume.create(vol.getFluidKey(), amount), Simulation.ACTION);
			cauldron.markDirty();
			return true;
		}
		return false;
	}

	@Override
	public boolean drain(World world, BlockPos pos, BlockState state, Fluid fluid, int bottles) {
		int amount = FluidVolume.BOTTLE * bottles;
		StoneCauldronEntity cauldron = (StoneCauldronEntity)world.getBlockEntity(pos);
		FluidVolume vol = cauldron.fluid.getInvFluid(0);
			vol.split(amount);
			cauldron.markDirty();
			return true;
	}

	@Override
	public boolean canAcceptFluid(World world, BlockPos pos, BlockState state, Fluid fluid) {
		StoneCauldronEntity cauldron = (StoneCauldronEntity)world.getBlockEntity(pos);
		FluidVolume vol = cauldron.fluid.getInvFluid(0);
		if (vol.getRawFluid() == null) return false;
		return vol.getRawFluid().equals(fluid);
	}

	public static int getLastFilledSlot(DefaultedList<ItemStack> slots) {
		for (int i = 0; i < slots.size(); i++) {
			if (slots.get(i).isEmpty()) return i - 1;
		}
		return slots.size() - 1;
	}

	@Override
	public void addAllAttributes(World world, BlockPos pos, BlockState state, AttributeList<?> to) {
		BlockEntity be = world.getBlockEntity(pos);
		if (be instanceof StoneCauldronEntity) {
			StoneCauldronEntity cauldron = (StoneCauldronEntity) be;
			to.offer(cauldron.fluid, OUTLINE_SHAPE);
			to.offer(cauldron.fluid.getInsertable(), OUTLINE_SHAPE);
			to.offer(cauldron.fluid.getExtractable(), OUTLINE_SHAPE);
			// cauldron.addAttributes(to);
		}
	}
}
