package io.github.cottonmc.witchcraft.util;

import net.minecraft.entity.player.PlayerEntity;

public interface IncenseAction {
	/**
	 * Perform an action on a nearby player. Happens six times over one minute.
	 * @param player The player to apply to.
	 */
	void purge(PlayerEntity player);
}
