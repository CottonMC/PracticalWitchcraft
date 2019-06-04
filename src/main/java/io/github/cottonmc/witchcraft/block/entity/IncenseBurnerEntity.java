package io.github.cottonmc.witchcraft.block.entity;

import io.github.cottonmc.witchcraft.block.WitchcraftBlocks;
import io.github.cottonmc.witchcraft.item.IncenseStickItem;
import io.github.cottonmc.witchcraft.item.WitchcraftItems;
import io.github.cottonmc.witchcraft.util.WitchcraftNetworking;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.fabricmc.fabric.api.server.PlayerStream;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.network.packet.EntityPotionEffectS2CPacket;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

public class IncenseBurnerEntity extends BlockEntity implements BlockEntityClientSerializable {
	private ItemStack incense = ItemStack.EMPTY;

	private int purges;

	public IncenseBurnerEntity() {
		super(WitchcraftBlocks.INCENSE_BURNER_BE);
	}

	public void light() {
		purges = 6;
		world.getBlockTickScheduler().schedule(pos, WitchcraftBlocks.INCENSE_BURNER, 100);
		markDirty();
	}

	public void purge() {
		world.playSound(null, pos, SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.BLOCKS, 1.0f, 1.0f);
		for (Object obj : PlayerStream.around(this.world, this.pos, 5d).toArray()) {
			PlayerEntity player = (PlayerEntity)obj;
			if (incense.getItem() instanceof IncenseStickItem) {
				IncenseStickItem stick = (IncenseStickItem) incense.getItem();
				stick.getAction().purge(player);
			}
		}
		purges--;
		if (purges > 0) {
			world.getBlockTickScheduler().schedule(pos, WitchcraftBlocks.INCENSE_BURNER, 100);
		} else {
			incense.subtractAmount(1);
		}
		markDirty();
	}

	public boolean hasIncense() {
		return !incense.isEmpty();
	}

	public ItemStack removeIncense() {
		ItemStack ret = incense.copy();
		incense.subtractAmount(1);
		markDirty();
		return ret;
	}

	public void setIncense(ItemStack stack) {
		incense = stack.copy();
		incense.setAmount(1);
		markDirty();
	}

	@Override
	public void fromTag(CompoundTag tag) {
		super.fromTag(tag);
		purges = tag.getInt("Purges");
		incense = ItemStack.fromTag(tag.getCompound("Incense"));
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		tag.putInt("Purges", purges);
		tag.put("Incense", incense.toTag(new CompoundTag()));
		return super.toTag(tag);
	}

	@Override
	public void fromClientTag(CompoundTag tag) {
		this.fromTag(tag);
	}

	@Override
	public CompoundTag toClientTag(CompoundTag tag) {
		return this.toTag(tag);
	}

	@Override
	public void markDirty() {
		super.markDirty();
		if (!this.world.isClient) {
			for (Object obj : PlayerStream.watching(this).toArray()) {
				ServerPlayerEntity player = (ServerPlayerEntity) obj;
				player.networkHandler.sendPacket(this.toUpdatePacket());
			}
		}
	}
}
