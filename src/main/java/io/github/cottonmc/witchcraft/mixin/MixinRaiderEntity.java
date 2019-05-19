package io.github.cottonmc.witchcraft.mixin;

import io.github.cottonmc.witchcraft.karma.KarmaManager;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.PatrolEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//to remove once player tick event is impl'd in fabric proper
@Mixin(RaiderEntity.class)
public abstract class MixinRaiderEntity extends PatrolEntity {

	protected MixinRaiderEntity(EntityType<? extends PatrolEntity> type, World world) {
		super(type, world);
	}

	@Inject(method = "onDeath", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;addPotionEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;)Z"))
	private void affectKarma(DamageSource source, CallbackInfo ci) {
		if (source.getAttacker() instanceof PlayerEntity) {
			KarmaManager.shiftKarma((PlayerEntity)source.getAttacker(), -5, true);
		}
	}

}
