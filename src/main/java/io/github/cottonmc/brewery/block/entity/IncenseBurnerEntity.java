package io.github.cottonmc.brewery.block.entity;

import io.github.cottonmc.brewery.block.BreweryBlocks;
import io.github.cottonmc.brewery.item.BreweryItems;
import io.github.cottonmc.brewery.util.BreweryNetworking;
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

public class IncenseBurnerEntity extends BlockEntity {
	private ItemStack incense = ItemStack.EMPTY;

	private int purges;

	public IncenseBurnerEntity() {
		super(BreweryBlocks.INCENSE_BURNER_BE);
	}

	public void light() {
		purges = 6;
		world.getBlockTickScheduler().schedule(pos, BreweryBlocks.INCENSE_BURNER, 200);
		markDirty();
	}

	public void purge() {
		world.playSound(null, pos, SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.BLOCKS, 1.0f, 1.0f);
		for (Object obj : PlayerStream.around(this.world, this.pos, 5d).toArray()) {
			PlayerEntity player = (PlayerEntity)obj;
			//TODO: make fancy with an API to add incense sticks or something
			if (incense.getItem() == BreweryItems.PURGING_INCENSE) {
				if (player.hasStatusEffect(StatusEffects.BAD_OMEN)) {
					StatusEffectInstance inst = player.getStatusEffect(StatusEffects.BAD_OMEN);
					int level = inst.getAmplifier();
					int time = inst.getDuration();
					player.removePotionEffect(StatusEffects.BAD_OMEN);
					BreweryNetworking.removeEffect((ServerPlayerEntity) player, StatusEffects.BAD_OMEN);
					if (level != 0) {
						StatusEffectInstance newInst = new StatusEffectInstance(StatusEffects.BAD_OMEN, time, level - 1, false, false, true);
						player.addPotionEffect(newInst);
						((ServerPlayerEntity) player).networkHandler.sendPacket(new EntityPotionEffectS2CPacket(player.getEntityId(), newInst));
					}
				}
			}
		}
		purges--;
		if (purges > 0) {
			world.getBlockTickScheduler().schedule(pos, BreweryBlocks.INCENSE_BURNER, 200);
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
}
