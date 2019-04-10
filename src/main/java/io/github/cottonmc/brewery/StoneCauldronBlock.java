package io.github.cottonmc.brewery;

import io.github.cottonmc.cotton.cauldron.Cauldron;
import io.github.cottonmc.cotton.cauldron.CauldronBehavior;
import io.github.cottonmc.cotton.cauldron.CauldronContext;
import io.github.prospector.silk.block.SilkBlockWithEntity;
import io.github.prospector.silk.fluid.*;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tag.FabricItemTags;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.VerticalEntityPosition;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.BooleanBiFunction;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import java.util.function.Predicate;

public class StoneCauldronBlock extends SilkBlockWithEntity implements FluidContainerProvider, Cauldron {

	public static final VoxelShape RAY_TRACE_SHAPE = createCuboidShape(2.0D, 4.0D, 2.0D, 14.0D, 16.0D, 14.0D);
	public static final VoxelShape OUTLINE_SHAPE = VoxelShapes.combineAndSimplify(VoxelShapes.fullCube(), VoxelShapes.union(createCuboidShape(0.0D, 0.0D, 4.0D, 16.0D, 3.0D, 12.0D), new VoxelShape[]{createCuboidShape(4.0D, 0.0D, 0.0D, 12.0D, 3.0D, 16.0D), createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 3.0D, 14.0D), RAY_TRACE_SHAPE}), BooleanBiFunction.ONLY_FIRST);

	public StoneCauldronBlock() {
		super(FabricBlockSettings.of(Material.STONE).breakByTool(FabricItemTags.PICKAXES).strength(6.0f, 6.0f).build());
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, VerticalEntityPosition entityPos) {
		return OUTLINE_SHAPE;
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
		FluidInstance fluid = cauldron.fluid.getFluid(0);
		if (stack.isEmpty() && !(cauldron.previousItems.isEmpty()) && player.isSneaking()) {
			int index = getLastFilledSlot(cauldron.previousItems);
			if (index != -1) {
				player.setStackInHand(hand, cauldron.previousItems.get(index));
				cauldron.previousItems.set(index, ItemStack.EMPTY);
			}
			return true;
		}
		if (stack.getItem() instanceof BucketItem) {
			if (stack.getItem() == Items.BUCKET && fluid.getAmount() >= DropletValues.BUCKET) {
				BucketUtil.fillBucketFromFluid(stack, fluid.getFluid());
				drain(world, pos, state, fluid.getFluid(), 3);
				return true;
			}
			else if (fluid.isEmpty()) {
				FluidInstance vol = ((ItemWithFluid)stack.getItem()).getFluid(stack);
				fluid.setFluid(vol.getFluid());
				fluid.setAmount(vol.getAmount());
				player.setStackInHand(hand, new ItemStack(Items.BUCKET));
				return true;
			}
		}
		CauldronContext ctx = new CauldronContext(world, pos, state, fluid.getAmount() / DropletValues.BOTTLE, fluid.getFluid(), cauldron.previousItems, player, hand, player.getStackInHand(hand));
		for (Predicate<CauldronContext> pred : CauldronBehavior.BEHAVIORS.keySet()) {
			if (pred.test(ctx)) {
				CauldronBehavior behavior = CauldronBehavior.BEHAVIORS.get(pred);
				behavior.react(ctx);
				return true;
			}
		}
		return super.activate(state, world, pos, player, hand, hit);
	}

	@Override
	public boolean fill(World world, BlockPos pos, BlockState state, Fluid fluid, int bottles) {
		int amount = DropletValues.BOTTLE * bottles;
		StoneCauldronEntity cauldron = (StoneCauldronEntity)world.getBlockEntity(pos);
		FluidInstance vol = cauldron.fluid.getFluid(0);
		if (vol.getFluid().equals(fluid) && vol.getAmount() + amount <= DropletValues.BLOCK) {
			vol.addAmount(amount);
		}
		return false;
	}

	@Override
	public boolean drain(World world, BlockPos pos, BlockState state, Fluid fluid, int bottles) {
		int amount = DropletValues.BOTTLE * bottles;
		StoneCauldronEntity cauldron = (StoneCauldronEntity)world.getBlockEntity(pos);
		FluidInstance vol = cauldron.fluid.getFluid(0);
		if (vol.getFluid().equals(fluid) && vol.getAmount() > amount) {
			vol.subtractAmount(amount);
		}
		return false;
	}

	@Override
	public boolean canAcceptFluid(World world, BlockPos pos, BlockState state, Fluid fluid) {
		StoneCauldronEntity cauldron = (StoneCauldronEntity)world.getBlockEntity(pos);
		FluidInstance vol = cauldron.fluid.getFluid(0);
		return vol.getFluid().equals(fluid);
	}

	@Override
	public FluidContainer getContainer(BlockState state, IWorld world, BlockPos pos) {
		StoneCauldronEntity be = (StoneCauldronEntity)world.getBlockEntity(pos);
		return be.fluid;
	}

	public static int getLastFilledSlot(DefaultedList<ItemStack> slots) {
		for (int i = 0; i < slots.size(); i++) {
			if (slots.get(i).isEmpty()) return i - 1;
		}
		return slots.size() - 1;
	}
}
