package io.github.cottonmc.witchcraft.mixin;

import io.github.cottonmc.witchcraft.effect.WitchcraftEffects;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.util.Identifier;
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
	public static Potion MUNDANE = reregister("mundane", new Potion(new StatusEffectInstance(WitchcraftEffects.IMMUNITY, 3600)));

	private static Potion reregister(String name, Potion potion) {
		int value = Registry.POTION.getRawId(Registry.POTION.get(new Identifier(name)));
		return Registry.register(Registry.POTION, value, name, potion);
	}
}
