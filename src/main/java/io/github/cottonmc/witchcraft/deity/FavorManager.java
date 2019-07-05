package io.github.cottonmc.witchcraft.deity;

import com.raphydaphy.crochet.data.PlayerData;
import io.github.cottonmc.witchcraft.Witchcraft;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.Text;
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
	 * Release a player from their devotion to their Deity.
	 * @param player The player forsaking.
	 */
	public static void forsake(PlayerEntity player) {
		CompoundTag tag = PlayerData.get(player, Witchcraft.MODID);
		if (tag.containsKey("Devotee", NbtType.STRING)) {
			Deity deity = Pantheon.DEITIES.get(new Identifier(tag.getString("Devotee")));
			tag.remove("Devotee");
		}
	}

	public static void shiftFavor(PlayerEntity player, Deity deity, float amount) {
		shiftFavor(player, deity, amount, false);
	}

	/**
	 * Shift a Deity's opinion of a player by a certain amount.
	 * @param player The player to change the Deity's opinion of.
	 * @param deity The Deity to change for.
	 * @param amount How much positive or negative favor the player has accrued with this Deity.
	 * @param passive If true, the deity's methods for a favor shift will not be run.
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
		boolean rollover = oldFavor != newFavor;
		Text message = deity.getFavorMessage(player, favor, amount, rollover);
		if (message != null) player.addChatMessage(message, true);
		deity.affectPlayer(player, favor, amount, rollover);
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
	 * @param player The player to set the Deity's opinion of.
	 * @param deity The Deity to set opinion for.
	 * @param amount What value the favor should be set to, positive or negative.
	 * @param passive If true, the Deity's methods for a favor shift will not be called.
	 */
	public static void setFavor(PlayerEntity player, Deity deity, float amount, boolean passive) {
		CompoundTag tag = getDeityTag(player, deity);
		float favor = amount;
		if (amount > 100) favor = 100;
		if (amount < -100) favor = -100;
		tag.putFloat("Favor", favor);
		PlayerData.markDirty(player);
		if (passive) return;
		boolean rollover = Math.abs(amount) >= 1;
		Text message = deity.getFavorMessage(player, favor, amount, rollover);
		if (message != null) player.addChatMessage(message, true);
		deity.affectPlayer(player, favor, amount, rollover);
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
}
