package io.github.cottonmc.witchcraft.block.entity;

import io.github.cottonmc.witchcraft.block.WitchcraftBlocks;
import io.github.cottonmc.witchcraft.spell.Spell;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

public class SpellCircleEntity extends BlockEntity implements BlockEntityClientSerializable {
	private Spell spell;
	private PlayerEntity activator;
	private boolean hasPixie;

	public SpellCircleEntity() {
		super(WitchcraftBlocks.SPELL_CIRCLE_BE);
	}

	public void setSpell(Spell spell) {
		this.spell = spell;
	}

	public void beginSpell(PlayerEntity activator) {
		this.activator = activator;
		if (spell != null) {
			spell.perform(world, pos, activator);
			this.hasPixie = false;
		}
		markDirty();
	}

	public void performSpell() {
		if (spell != null) {
			int performances = spell.getPerformances();
			int breakChance = world.getRandom().nextInt(15);
			if (performances - 15 < breakChance) spell.perform(world, pos, activator);
			else {
				world.playSound(null, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 1f, 1f);
			}
		}
		markDirty();
	}

	public Spell getSpell() {
		return spell;
	}

	public boolean hasPixie() {
		return hasPixie;
	}

	public void addPixie() {
		hasPixie = true;
		markDirty();
	}

	@Override
	public void fromTag(CompoundTag tag) {
		super.fromTag(tag);
		if (tag.containsKey("Spell", NbtType.COMPOUND)) this.spell = Spell.fromTag(tag.getCompound("Spell"));
		if (tag.containsKey("ActivatorMost", NbtType.LONG)) this.activator = world.getPlayerByUuid(tag.getUuid("Activator"));
		hasPixie = tag.getBoolean("HasPixie");
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		super.toTag(tag);
		if (spell != null) tag.put("Spell", spell.toTag());
		if (activator != null) tag.putUuid("Activator", activator.getUuid());
		tag.putBoolean("HasPixie", hasPixie);
		return tag;
	}

	@Override
	public void fromClientTag(CompoundTag tag) {
		this.fromTag(tag);
	}

	@Override
	public CompoundTag toClientTag(CompoundTag tag) {
		return this.toTag(tag);
	}
}
