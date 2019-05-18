package io.github.cottonmc.brewery.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//to remove once player tick event is impl'd in fabric proper
@Mixin(PlayerEntity.class)
public abstract class MixinPlayerTickTemp extends LivingEntity {
	protected MixinPlayerTickTemp(EntityType<? extends LivingEntity> type, World world) {
		super(type, world);
	}

	@Inject(method = "tick", at = @At("TAIL"))
	private void tickImmunity(CallbackInfo ci) {
		for (StatusEffectInstance effect : this.getStatusEffects()) {
			if (effect.getEffectType() == StatusEffects.WITHER) {
				//only clear Wither if it's got less than 5 seconds left, to prevent cheesing wither battles
				if (effect.getDuration() <= 100) {
					this.removePotionEffect(StatusEffects.WITHER);
				}
			} else if (effect.getEffectType().getType() == StatusEffectType.HARMFUL
					&& effect.getEffectType() != StatusEffects.BAD_OMEN) {
				//don't make immunity clear out bad omens, you have to cleanse that specially
				this.removePotionEffect(effect.getEffectType());
			}
		}
	}

}
