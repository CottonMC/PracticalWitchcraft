package io.github.cottonmc.witchcraft.component;

import io.github.cottonmc.witchcraft.deity.Deity;
import nerdhub.cardinal.components.api.util.sync.EntitySyncedComponent;

public interface DeityComponent extends EntitySyncedComponent {
	/**
	 * Get the Deity this player is devoted to.
	 * @return The Deity the player is devoted to, or null if the player has no devotion.
	 */
	Deity getDevotion();

	/**
	 * Set this player's devotion to a certain Deity.
	 * @param deity The Deity the player is devoting themselves to.
	 */
	void setDevotion(Deity deity);

	/**
	 * @param deity The deity to check for.
	 * @return Whether this player is devoted to the given Deity.
	 */
	default boolean isDevotedTo(Deity deity) {
		return getDevotion() == deity;
	}

	/**
	 * Release this player from their devotion to their Deity.
	 */
	void forsakeDeity();

	default void shiftFavor(Deity deity, float amount) {
		shiftFavor(deity, amount, false);
	}

	/**
	 * Shift a Deity's opinion of this player by a certain amount.
	 * @param deity The Deity to change for.
	 * @param amount How much positive or negative favor the player has accrued with this Deity.
	 * @param passive If true, the deity's methods for a favor shift will not be run.
	 */
	void shiftFavor(Deity deity, float amount, boolean passive);

	/**
	 * Reset a Deity's opinion of this player.
	 * @param deity The Deity to reset for.
	 */
	void resetFavor(Deity deity);

	default void setFavor(Deity deity, float amount) {
		setFavor(deity, amount, false);
	}

	/**
	 * Set a Deity's opinion of this player to a given value.
	 * @param deity The Deity to set opinion for.
	 * @param amount What value the favor should be set to, positive or negative.
	 * @param passive If true, the Deity's methods for a favor shift will not be called.
	 */
	void setFavor(Deity deity, float amount, boolean passive);

	/**
	 * @param deity The Deity to check for.
	 * @return How much favor the Deity has for the given player at the moment.
	 */
	float getFavor(Deity deity);
}
