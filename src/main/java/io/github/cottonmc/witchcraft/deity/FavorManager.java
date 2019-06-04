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
import net.minecraft.util.Identifier;

import javax.annotation.Nullable;

/**
 * Utility class for managing Deity favor.
 */
public class FavorManager {
	/**
	 * Set a player's devotion to a certain Deity.
	 * @param player The player devoting themselves.
	 * @param deity The Deity the player is devoting themselves to.
	 */
	public static void devote(PlayerEntity player, Deity deity) {
		CompoundTag tag = PlayerData.get(player, Witchcraft.MODID);
		tag.putString("Devotee", Pantheon.DEITIES.getId(deity).toString());
		PlayerData.markDirty(player);
	}

	/**
	 * Get the Deity a player is devoted to.
	 * @param player The player to check.
	 * @return The Deity the player is devoted to, or null if the player has no devotion.
	 */
	@Nullable
	public static Deity getDevotion(PlayerEntity player) {
		CompoundTag tag = PlayerData.get(player, Witchcraft.MODID);
		if (!tag.containsKey("Devotee")) return null;
		Identifier deityId = new Identifier(tag.getString("Devotee"));
		return Pantheon.DEITIES.get(deityId);
	}

	/**
	 * @param player The player to check.
	 * @param deity The deity to check for.
	 * @return Whether a player is devoted to the given Deity.
	 */
	public static boolean isDevotedTo(PlayerEntity player, Deity deity) {
		Deity checkDeity = getDevotion(player);
		if (checkDeity == null) return false;
		else return deity == checkDeity;
	}

	/**
	 * Cause a player to forsake their devotion to their Deity.
	 * Curses the player if they had a devotion.
	 * @param player The player forsaking.
	 */
	public static void forsake(PlayerEntity player) {
		CompoundTag tag = PlayerData.get(player, Witchcraft.MODID);
		if (tag.containsKey("Devotee", NbtType.STRING)) {
			Deity deity = Pantheon.DEITIES.get(new Identifier(tag.getString("Devotee")));
			tag.remove("Devotee");
			curse(player, deity,false);
		}
	}

	public static void shiftFavor(PlayerEntity player, Deity deity, float amount) {
		shiftFavor(player, deity, amount, false);
	}

	/**
	 * Shift a Deity's opinion of a player by a certain amount.
	 * When a Deity's integer value opinion is raised or lowered, the player will be given Luck or Unluck, respectively.
	 * When a Deity's opinion exceeds 10 or -10, the player will have a Curse or Blessing lifted, respectively.
	 * When a Deity's opinion exceeds 20 or -20, the player will be Blessed or Cursed by that deity, respectively.
	 * @param player The player to change the Deity's opinion of.
	 * @param deity The Deity to change for.
	 * @param amount How much positive or negative favor the player has accrued with this Deity.
	 * @param passive If true, this shift will not give any direct indication it happened.
	 */
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
		if (passive) return;
		if (oldFavor != newFavor) {
			player.addChatMessage(new TranslatableComponent("msg.witchcraft.favor." + (amount > 0 ? "gain" : "lose"), deity.getName(player).getFormattedText()), true);
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
		}
		if (favor > 10) {
			player.removePotionEffect(WitchcraftEffects.CURSED);
			WitchcraftNetworking.removeEffect((ServerPlayerEntity) player, WitchcraftEffects.CURSED);
		} else if (favor < 10) {
			player.removePotionEffect(WitchcraftEffects.BLESSED);
			WitchcraftNetworking.removeEffect((ServerPlayerEntity) player, WitchcraftEffects.BLESSED);
		}
		boolean isDevotion = isDevotedTo(player, deity);
		if (favor >= 20) {
			if (amount > 1 || isDevotion) bless(player, deity, true);
			deity.bless(player, favor);
			player.addPotionEffect(new StatusEffectInstance(WitchcraftEffects.BLESSED, 18000, 0, false, false, true));
		} else if (favor <= -20) {
			if (amount < -1 || isDevotion) curse(player, deity, true);
			int multiplier = (int) ((favor * -1) - 20) / 10;
			multiplier = Math.min(multiplier, 5);
			player.addPotionEffect(new StatusEffectInstance(WitchcraftEffects.CURSED, 18000, multiplier, false, false, true));
		}
	}

	/**
	 * Reset a Deity's opinion of a player.
	 * @param player The player to reset the Deity's opinion of.
	 * @param deity The Deity to reset for.
	 */
	public static void resetFavor(PlayerEntity player, Deity deity) {
		CompoundTag tag = getDeityTag(player, deity);
		tag.putFloat("Favor", 0);
		PlayerData.markDirty(player);
	}

	public static void setFavor(PlayerEntity player, Deity deity, float amount) {
		setFavor(player, deity, amount, false);
	}

	/**
	 * Set a Deity's opinion of a player to a given value.
	 * When a Deity's opinion exceeds 10 or -10, the player will have a Curse or Blessing lifted, respectively, every time opinion is shifted by greater than 1.
	 * When a Deity's opinion exceeds 20 or -20, the player will be Blessed or Cursed, respectively, every time opinion is shifted by greater than 1.
	 * @param player The player to set the Deity's opinion of.
	 * @param deity The Deity to set opinion for.
	 * @param amount What value the favor should be set to, positive or negative.
	 * @param passive If true, this setting will not give any direct indication it happened.
	 */
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
			player.removePotionEffect(WitchcraftEffects.BLESSED);
			WitchcraftNetworking.removeEffect((ServerPlayerEntity)player, WitchcraftEffects.BLESSED);
		}
		if (amount >= 20) {
			player.addPotionEffect(new StatusEffectInstance(WitchcraftEffects.BLESSED, 18000, 0, true, false));
			deity.bless(player, amount);
		} else if (amount <= -20) {
			int multiplier = (int)((amount * -1) - 20) / 10;
			multiplier = Math.min(multiplier, 5);
			player.addPotionEffect(new StatusEffectInstance(WitchcraftEffects.CURSED, 18000, multiplier, true, false));
			deity.curse(player, amount);
		}
	}

	/**
	 * @param player The player to check on.
	 * @param deity The Deity to check for.
	 * @return How much favor the Deity has for the given player at the moment.
	 */
	public static float getFavor(PlayerEntity player, Deity deity) {
		CompoundTag tag = getDeityTag(player, deity);
		return tag.getFloat("Favor");
	}

	private static CompoundTag getDeityTag(PlayerEntity player, Deity deity) {
		String id = Pantheon.DEITIES.getId(deity).toString();
		CompoundTag wcTag = PlayerData.get(player, Witchcraft.MODID);
		if (!wcTag.containsKey("Deities", NbtType.COMPOUND)) wcTag.put("Deities", new CompoundTag());
		CompoundTag deities = wcTag.getCompound("Deities");
		if (!deities.containsKey(id, NbtType.COMPOUND)) deities.put(id, new CompoundTag());
		return deities.getCompound(id);
	}

	/**
	 * When favor is sufficiently shifted in the positive direction, the specific player will be Blessed.
	 * @param player The player to bless.
	 * @param deity the Deity blessing the player.
	 * @param deityOnly If true, the player will not get a Blessed status effect, and their Devotion will not Bless them.
	 */
	public static void bless(PlayerEntity player, @Nullable Deity deity, boolean deityOnly) {
		if (deity != null) deity.bless(player, getFavor(player, deity));
		if (deityOnly) return;
		Deity devotion = getDevotion(player);
		if (devotion != null) devotion.bless(player, getFavor(player, devotion));
		player.addPotionEffect(new StatusEffectInstance(WitchcraftEffects.BLESSED, 18000));
	}

	/**
	 * When favor is sufficiently shifted in the negative direction, the specific player will be Cursed.
	 * @param player The player to curse.
	 * @param deity the Deity cursing the player.
	 * @param deityOnly If true, the player will not get a Cursed status effect.
	 */
	public static void curse(PlayerEntity player, @Nullable Deity deity, boolean deityOnly) {
		if (deity != null) deity.curse(player, getFavor(player, deity));
		Deity devotion = getDevotion(player);
		boolean isFromDevotion = false;
		if (devotion != null) {
			if (devotion == deity) {
				isFromDevotion = true;
				if (devotion.getCharacter() == DeityCharacter.JUDGEMENTAL) devotion.curse(player, getFavor(player, devotion));
			}
		}
		if (deityOnly) return;
		if (!player.hasStatusEffect(WitchcraftEffects.CURSED)) player.addPotionEffect(new StatusEffectInstance(WitchcraftEffects.CURSED, 120000));
		else {
			int level = player.getStatusEffect(WitchcraftEffects.CURSED).getAmplifier();
			if (level < 5) {
				if (isFromDevotion && devotion.getCharacter() == DeityCharacter.FORGIVING) level = Math.max(1, level - 1);
				player.addPotionEffect(new StatusEffectInstance(WitchcraftEffects.CURSED, 120000, level + 1));
			}
		}
	}
}
