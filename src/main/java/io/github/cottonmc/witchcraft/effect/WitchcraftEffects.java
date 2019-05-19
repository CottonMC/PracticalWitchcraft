package io.github.cottonmc.witchcraft.effect;

import io.github.cottonmc.witchcraft.Witchcraft;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class WitchcraftEffects {
	public static StatusEffect IMMUNITY = register("immunity", new ImmunityStatusEffect());

	public static void init() {
	}

	public static StatusEffect register(String name, StatusEffect effect) {
		Registry.register(Registry.STATUS_EFFECT, new Identifier(Witchcraft.MODID, name), effect);
		return effect;
	}
}
