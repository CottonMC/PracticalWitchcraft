package io.github.cottonmc.brewery.effect;

import io.github.cottonmc.brewery.Brewery;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class BreweryEffects {
	public static StatusEffect IMMUNITY = register("immunity", new BreweryStatusEffect(StatusEffectType.NEUTRAL, 0x385DC6));

	public static void init() {
	}

	public static StatusEffect register(String name, StatusEffect effect) {
		Registry.register(Registry.STATUS_EFFECT, new Identifier(Brewery.MODID, name), effect);
		return effect;
	}
}
