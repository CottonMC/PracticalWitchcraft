package io.github.cottonmc.witchcraft.compat.libcd;

import io.github.cottonmc.libcd.api.LibCDInitializer;
import io.github.cottonmc.libcd.api.condition.ConditionManager;
import io.github.cottonmc.libcd.api.tweaker.TweakerManager;

public class LibCDInit implements LibCDInitializer {
	@Override
	public void initTweakers(TweakerManager manager) {
		manager.addAssistant("witchcraft.WitchcraftRecipeTweaker", WitchcraftRecipeTweaker.INSTANCE);
	}

	@Override
	public void initConditions(ConditionManager manager) {

	}
}
