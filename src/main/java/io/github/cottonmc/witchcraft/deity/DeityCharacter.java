package io.github.cottonmc.witchcraft.deity;

/**
 * Deities can have different personalities.
 * These personalities can affect how they treat their devotees.
 */
//TODO: should this be extensible?
public enum DeityCharacter {
	/**
	 * A forgiving Deity will be less harsh with curses they place on their devotees.
	 */
	FORGIVING,
	/**
	 * An apathetic Deity will not treat their devotees differently from anyone else.
	 */
	APATHETIC,
	/**
	 * A judgemental Deity will curse their devotees whenever the devotee obtains a curse from another Deity.
	 */
	JUDGEMENTAL
}
