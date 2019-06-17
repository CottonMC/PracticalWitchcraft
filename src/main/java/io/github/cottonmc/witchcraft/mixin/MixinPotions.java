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
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Potions.class)
public class MixinPotions {

	@ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/potion/Potions;register(Ljava/lang/String;Lnet/minecraft/potion/Potion;)Lnet/minecraft/potion/Potion;", ordinal = 2))
	private static Potion changeMundanePotion(Potion original) {
		return new Potion(new StatusEffectInstance(WitchcraftEffects.IMMUNITY, 3600));
	}
}
