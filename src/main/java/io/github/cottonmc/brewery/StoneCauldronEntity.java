package io.github.cottonmc.brewery;

import io.github.prospector.silk.fluid.DropletValues;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Tickable;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

import java.util.List;
import java.util.stream.Collectors;

public class StoneCauldronEntity extends BlockEntity implements Tickable {
	static VoxelShape ABOVE_SHAPE = Block.createCuboidShape(0.0D, 16.0D, 0.0D, 16.0D, 32.0D, 16.0D);

	public SimpleFluidContainer fluid = new SimpleFluidContainer(1, DropletValues.BLOCK);
	public DefaultedList<ItemStack> previousItems = DefaultedList.create(8, ItemStack.EMPTY);

	public StoneCauldronEntity() {
		super(Brewery.STONE_CAULDRON_BE);
		fluid.listen(this::markDirty);
	}

	public void craft() {
		previousItems.clear();
		markDirty();
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		tag.put("PreviousItems", Inventories.toTag(new CompoundTag(), previousItems));
		tag.put("Fluid", fluid.toTag());
		return tag;
	}

	@Override
	public void fromTag(CompoundTag tag) {
		Inventories.fromTag(tag.getCompound("PreviousItems"), previousItems);
		fluid.fromTag(tag.getTag("Fluid"));
	}

	@Override
	public void markDirty() {
		super.markDirty();

	}

	@Override
	public void tick() {
		if (world.isClient || fluid.getFluid(0).isEmpty()) return;
			List<ItemEntity> itemsAbove = getInputItemEntities();
			if (!itemsAbove.isEmpty()) {
				boolean soundPlayed = false;
				for (ItemEntity item : itemsAbove) {
					ItemStack stack = item.getStack();
					int index = StoneCauldronBlock.getLastFilledSlot(previousItems);
					if (index != 7) {
						if (!soundPlayed) {
							world.playSound(null, pos, SoundEvents.ENTITY_GENERIC_SPLASH, SoundCategory.BLOCK, 1.0f, 1.0f);
							soundPlayed = true;
						}
						previousItems.set(index + 1, stack);
						item.remove();
					}
				}
				markDirty();
			}
	}

	public List<ItemEntity> getInputItemEntities() {
		VoxelShape inputShape = VoxelShapes.union(VoxelShapes.fullCube(), ABOVE_SHAPE);
		return inputShape.getBoundingBoxes().stream().flatMap((bb) -> world.getEntities(ItemEntity.class, bb.offset(pos.getX() - 0.5D, pos.getY() - 0.5D, pos.getZ() - 0.5D), EntityPredicates.VALID_ENTITY).stream()).collect(Collectors.toList());
	}
}
