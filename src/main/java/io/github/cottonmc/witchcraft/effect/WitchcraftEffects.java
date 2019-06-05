package io.github.cottonmc.witchcraft.effect;

import io.github.cottonmc.witchcraft.Witchcraft;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class WitchcraftEffects {
	//needs to be declared now so the mixin to the mundane potion can use it
	//this will cause a missing translation to be logged! Everything's fine, don't worry!
	public static StatusEffect IMMUNITY = register("immunity", new ImmunityStatusEffect());

	public static StatusEffect GREEN_THUMB;
	public static StatusEffect DECAY_TOUCH;
	public static StatusEffect FLARE;
	public static StatusEffect FIZZLE;

	public static void init() {
		GREEN_THUMB = register("green_thumb", new GreenThumbStatusEffect());
		DECAY_TOUCH = register("decay_touch", new DecayTouchStatusEffect());
		FLARE = register("flare", new WitchcraftStatusEffect(StatusEffectType.BENEFICIAL, 0x3dc3e1));
		FIZZLE = register("fizzle", new WitchcraftStatusEffect(StatusEffectType.HARMFUL, 0x990a78));
	}

	public static StatusEffect register(String name, StatusEffect effect) {
		Registry.register(Registry.STATUS_EFFECT, new Identifier(Witchcraft.MODID, name), effect);
		return effect;
	}
}
