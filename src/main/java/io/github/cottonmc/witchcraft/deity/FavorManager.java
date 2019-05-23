package io.github.cottonmc.witchcraft.deity;

import com.raphydaphy.crochet.data.PlayerData;
import io.github.cottonmc.witchcraft.Witchcraft;
import io.github.cottonmc.witchcraft.effect.WitchcraftEffects;
import io.github.cottonmc.witchcraft.util.WitchcraftNetworking;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.network.ServerPlayerEntity;

public class FavorManager {
	public static void shiftFavor(PlayerEntity player, Deity deity, float amount) {
		shiftFavor(player, deity, amount, false);
	}

	public static void shiftFavor(PlayerEntity player, Deity deity, float amount, boolean passive) {
		if (amount == 0) return;
		CompoundTag tag = getDeityTag(player, deity);
		float favor;
		int oldFavor;
		int newFavor;
		if (!tag.containsKey("Favor", NbtType.FLOAT)) {
			oldFavor = 0;
			newFavor = (int)amount;
			favor = amount;
			tag.putFloat("Favor", amount);
		} else {
			favor = tag.getFloat("Favor");
			oldFavor = (int)favor;
			favor += amount;
			if (favor > 100) favor = 100;
			if (favor < -100) favor = -100;
			newFavor = (int)favor;
		}
		tag.putFloat("Favor", favor);
		PlayerData.markDirty(player);
		if (oldFavor != newFavor) player.addChatMessage(new TranslatableComponent("msg.witchcraft.favor." + (amount > 0? "gain" : "lose"), deity.getName()), true);
		else {
			if (passive) return;
			if (amount > 0) {
				player.removePotionEffect(StatusEffects.UNLUCK);
				WitchcraftNetworking.removeEffect((ServerPlayerEntity) player, StatusEffects.UNLUCK);
				int multiplier = (int) amount / 5;
				int duration = 1200 * (int) (amount % 5);
				player.addPotionEffect(new StatusEffectInstance(StatusEffects.LUCK, duration, multiplier, false, false, true));
			} else {
				player.removePotionEffect(StatusEffects.LUCK);
				WitchcraftNetworking.removeEffect((ServerPlayerEntity) player, StatusEffects.LUCK);
				int multiplier = (int) ((amount * -1) / 5);
				int duration = 1200 * (int) ((amount * -1) % 5);
				player.addPotionEffect(new StatusEffectInstance(StatusEffects.UNLUCK, duration, multiplier, false, false, true));
			}
			if (favor > 10) {
				player.removePotionEffect(WitchcraftEffects.CURSED);
				WitchcraftNetworking.removeEffect((ServerPlayerEntity) player, WitchcraftEffects.CURSED);
			} else if (favor < 10) {
				player.removePotionEffect(StatusEffects.HERO_OF_THE_VILLAGE);
				WitchcraftNetworking.removeEffect((ServerPlayerEntity) player, StatusEffects.HERO_OF_THE_VILLAGE);
			}
			if (favor >= 20) {
				player.addPotionEffect(new StatusEffectInstance(StatusEffects.HERO_OF_THE_VILLAGE, 18000, 0, false, false, true));
			} else if (favor <= -20) {
				int multiplier = (int) ((favor * -1) - 20) / 5;
				player.addPotionEffect(new StatusEffectInstance(WitchcraftEffects.CURSED, 18000, multiplier, false, false, true));
			}
		}
	}

	public static void resetFavor(PlayerEntity player, Deity deity) {
		CompoundTag tag = getDeityTag(player, deity);
		tag.putFloat("Favor", 0);
		PlayerData.markDirty(player);
	}

	public static void setFavor(PlayerEntity player, Deity deity, float amount) {
		setFavor(player, deity, amount, false);
	}

	public static void setFavor(PlayerEntity player, Deity deity, float amount, boolean passive) {
		CompoundTag tag = getDeityTag(player, deity);
		float favor = amount;
		if (amount > 100) favor = 100;
		if (amount < -100) favor = -100;
		tag.putFloat("Favor", favor);
		PlayerData.markDirty(player);
		if (passive) return;
		if (amount > 10) {
			player.removePotionEffect(WitchcraftEffects.CURSED);
			WitchcraftNetworking.removeEffect((ServerPlayerEntity)player, WitchcraftEffects.CURSED);
		} else if (amount < 10) {
			player.removePotionEffect(StatusEffects.HERO_OF_THE_VILLAGE);
			WitchcraftNetworking.removeEffect((ServerPlayerEntity)player, StatusEffects.HERO_OF_THE_VILLAGE);
		}
		if (amount >= 20) {
			player.addPotionEffect(new StatusEffectInstance(StatusEffects.HERO_OF_THE_VILLAGE, 18000, 0, true, false));
		} else if (amount <= -20) {
			int multiplier = (int)((amount * -1) - 20) / 5;
			player.addPotionEffect(new StatusEffectInstance(WitchcraftEffects.CURSED, 18000, multiplier, true, false));
		}
	}

	private static CompoundTag getDeityTag(PlayerEntity player, Deity deity) {
		String id = Pantheon.DEITIES.getId(deity).toString();
		CompoundTag wcTag = PlayerData.get(player, Witchcraft.MODID);
		if (!wcTag.containsKey("Deities", NbtType.COMPOUND)) wcTag.put("Deities", new CompoundTag());
		CompoundTag deities = wcTag.getCompound("Deities");
		if (!deities.containsKey(id, NbtType.COMPOUND)) deities.put(id, new CompoundTag());
		return deities.getCompound(id);
	}

	public static void curse(PlayerEntity player) {
		if (!player.hasStatusEffect(WitchcraftEffects.CURSED)) player.addPotionEffect(new StatusEffectInstance(WitchcraftEffects.CURSED, 120000));
		else {
			int level = player.getStatusEffect(WitchcraftEffects.CURSED).getAmplifier();
			if (level < 5) {
				player.addPotionEffect(new StatusEffectInstance(WitchcraftEffects.CURSED, 120000, level + 1));
			}
		}
	}
}
