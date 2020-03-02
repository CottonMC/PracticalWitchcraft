package io.github.cottonmc.witchcraft.deity;

import io.github.cottonmc.witchcraft.Witchcraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.stat.Stat;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * A deity of the world, which a player can pray to.
 */
public abstract class Deity {
	private Map<Stat<?>, Float> favors = new HashMap<>();
	private DeityCharacter character;
	private Formatting nameFormat;

	/**
	 * Deities are special beings, so They can optionally be given a ChatFormat to make their name appear special.
	 * @param character The personality of the Deity.
	 * @param nameFormat The ChatFormat to format the Deity's name with.
	 */
	public Deity(DeityCharacter character, Formatting nameFormat) {
		this.character = character;
		this.nameFormat = nameFormat;
	}

	/**
	 * Construct a Deity without a specially-formatted name.
	 */
	public Deity(DeityCharacter character) {
		this(character,null);
	}

	/**
	 * Do not call this yourself; it is called by the stat handler mixin to automatically update when stats are updated.
	 * @param player The player getting a stat updated.
	 * @param action The stat being updated.
	 * @param amount How many points the stat is being updated by.
	 */
	public void update(PlayerEntity player, Stat<?> action, int amount) {
		if (favors.containsKey(action)) Witchcraft.DEITY_COMPONENT.get(player).shiftFavor(this, favors.get(action) * amount);
	}

	/**
	 * Used to get a Deity's formatted/translated name.
	 * Call this only; do not override.
	 * @param player The player to check getNameSubkey for, or null if not in a player-focused context.
	 * @return a TranslatableText of the Deity's name.
	 */
	public Text getName(@Nullable PlayerEntity player) {
		Identifier id = Pantheon.DEITIES.getId(this);
		if (id == null) return new LiteralText("■■■■■");
		String key = "deity." + id.getNamespace() + "." + id.getPath();
		if (player != null) {
			String subkey = getNameSubkey(player);
			if (subkey != null && !subkey.equals("")) key += "." + subkey;
		}
		TranslatableText component = new TranslatableText(key);
		if (nameFormat != null) component.formatted(nameFormat);
		return component;
	}

	/**
	 * Names are a powerful thing. Deities may be picky about what names They go by when interacting with a given player.
	 * Deity name subkeys are not applied when there is no associated player.
	 * @param player The player the Deity is interacting with.
	 * @return The sub-key to be appended to the Deity's standard key of "deity.namespace.path". Return "" or null for no added subkey.
	 */
	public abstract String getNameSubkey(PlayerEntity player);

	/**
	 * @return the personality of this Deity.
	 */
	public DeityCharacter getCharacter() {
		return character;
	}

	/**
	 * Since Deity favors are based on stats, it's helpful to have access to data-driven constructs like tags.
	 * This is called on every data resource reload to reconstruct the Deity's favor/disfavor lists.
	 * This *must* be overridden for a deity to function, and the override *must* call to super at the beginning.
	 */
	public void reload() {
		favors.clear();
		favors.putAll(getFavors());
	}

	public abstract Map<Stat<?>, Float> getFavors();

	/**
	 * Send a status message to a player in reaction to a favor change from this Deity.
	 * @param player The player to apply to.
	 * @param currentFavor What the player's new favor total with this Deity is.
	 * @param changeAmount How much the player's favor total just changed.
	 * @param intRollover Whether the player's favor with this Deity just changed its integer value.
	 * @return The message to send, or null for none.
	 */
	@Nullable
	public abstract Text getFavorMessage(PlayerEntity player, float currentFavor, float changeAmount, boolean intRollover);

	/**
	 * Apply any effects to a player in reaction to a favor change from this Deity.
	 * @param player The player to apply to.
	 * @param currentFavor What the player's new favor total with this Deity is.
	 * @param changeAmount How much the player's favor total just changed.
	 * @param intRollover Whether the player's favor with this Deity just changed its integer value.
	 */
	public abstract void affectPlayer(PlayerEntity player, float currentFavor, float changeAmount, boolean intRollover);
}
