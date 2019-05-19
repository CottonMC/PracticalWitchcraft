package io.github.cottonmc.brewery.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.entity.effect.StatusEffects;

public class ImmunityStatusEffect extends BreweryStatusEffect {

	public ImmunityStatusEffect() {
		super(StatusEffectType.NEUTRAL, 0x385DC6);
	}

	@Override
	public boolean canApplyUpdateEffect(int int_1, int int_2) {
		return true;
	}

	@Override
	public void applyUpdateEffect(LivingEntity entity, int ticks) {
		for (StatusEffectInstance effect : entity.getStatusEffects()) {
			if (effect.getEffectType() == StatusEffects.WITHER) {
				//only clear Wither if it's got less than 5 seconds left, to prevent cheesing wither battles
				if (effect.getDuration() <= 100) {
					entity.removePotionEffect(StatusEffects.WITHER);
				}
			} else if (effect.getEffectType().getType() == StatusEffectType.HARMFUL
					&& effect.getEffectType() != StatusEffects.BAD_OMEN) {
				//don't make immunity clear out bad omens, you have to cleanse that specially
				entity.removePotionEffect(effect.getEffectType());
			}
		}
	}
}
