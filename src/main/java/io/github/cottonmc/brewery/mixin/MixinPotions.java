package io.github.cottonmc.brewery.mixin;

import io.github.cottonmc.brewery.effect.BreweryEffects;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Potions.class)
public class MixinPotions {

	@Shadow
	@Mutable
	@Final
	public static Potion MUNDANE = reregister("mundane", new Potion(new StatusEffectInstance(BreweryEffects.IMMUNITY, 3600)));

	private static Potion reregister(String name, Potion potion) {
		return Registry.register(Registry.POTION, name, potion);
	}
}
