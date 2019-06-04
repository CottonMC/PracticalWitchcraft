package io.github.cottonmc.witchcraft.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;

public class GreenThumbStatusEffect extends StatusEffect {
	public GreenThumbStatusEffect() {
		super(StatusEffectType.BENEFICIAL, 0x0a9976);
	}

	@Override
	public void applyUpdateEffect(LivingEntity entity, int amplifier) {
		super.applyUpdateEffect(entity, amplifier);
	}
}
