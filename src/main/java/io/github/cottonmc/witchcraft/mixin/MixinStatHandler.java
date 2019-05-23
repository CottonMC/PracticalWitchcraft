package io.github.cottonmc.witchcraft.mixin;

import io.github.cottonmc.witchcraft.deity.Pantheon;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.stat.Stat;
import net.minecraft.stat.StatHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StatHandler.class)
public abstract class MixinStatHandler {

	@Inject(method = "increaseStat", at = @At("HEAD"))
	private void affectKarma(PlayerEntity player, Stat stat, int amount, CallbackInfo ci) {
		Pantheon.DEITIES.stream().forEach(deity -> deity.update(player, stat, amount));
	}

}
