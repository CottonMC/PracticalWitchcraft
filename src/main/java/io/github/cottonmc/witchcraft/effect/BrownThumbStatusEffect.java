package io.github.cottonmc.witchcraft.effect;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.PlantBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

public class BrownThumbStatusEffect extends StatusEffect {
	public BrownThumbStatusEffect() {
		super(StatusEffectType.HARMFUL, 0x5d4a75);
	}

	@Override
	public void applyUpdateEffect(LivingEntity entity, int amplifier) {
		if (entity instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity)entity;
			PlayerInventory inv = player.inventory;
			for (int i = 0; i < inv.getInvSize(); i++) {
				ItemStack stack = inv.getInvStack(i);
				int amount = stack.getAmount();
				if (stack.getItem() instanceof BlockItem) {
					Block block = ((BlockItem)stack.getItem()).getBlock();
					if (block instanceof PlantBlock && block != Blocks.DEAD_BUSH) {
						if (amount > 16) {
							ItemStack bushes = new ItemStack(Items.DEAD_BUSH, 16);
							stack.subtractAmount(16);
							if (!inv.insertStack(bushes)) {
								player.dropStack(bushes);
							}
						} else {
							ItemStack bushes = new ItemStack(Items.DEAD_BUSH, amount);
							inv.setInvStack(i, bushes);
						}
						player.playSound(SoundEvents.BLOCK_GRASS_BREAK, SoundCategory.PLAYERS, 1f, 1f);
						break;
					}
				}
			}
		}
	}

	@Override
	public boolean canApplyUpdateEffect(int duration, int amplifier) {
		if (amplifier == 0) return duration % 40 == 0;
		return super.canApplyUpdateEffect(duration, amplifier);
	}
}
