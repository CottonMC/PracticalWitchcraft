package io.github.cottonmc.witchcraft.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.entity.effect.StatusEffects;

public class ImmunityStatusEffect extends WitchcraftStatusEffect {

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
				//only clear Wither if it's got less than 3 seconds left, to prevent cheesing wither battles
				if (effect.getDuration() <= 60) {
					entity.removeStatusEffect(StatusEffects.WITHER);
				}
			} else if (effect.getEffectType().getType() == StatusEffectType.HARMFUL
					&& effect.getEffectType() != WitchcraftEffects.FIZZLE) {
				//don't make immunity clear out fizzle, you have to cleanse that specially
				entity.removeStatusEffect(effect.getEffectType());
			}
		}
	}
}
