package io.github.cottonmc.witchcraft.block.entity;

import io.github.cottonmc.witchcraft.block.WitchcraftBlocks;
import io.github.cottonmc.witchcraft.spell.Spell;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.DefaultedList;

public class SpellCircleEntity extends BlockEntity implements BlockEntityClientSerializable {
	private Spell spell;
	PlayerEntity activator;

	public SpellCircleEntity() {
		super(WitchcraftBlocks.SPELL_CIRCLE_BE);
	}

	public void setSpell(Spell spell) {
		this.spell = spell;
	}

	public void beginSpell(PlayerEntity activator) {
		this.activator = activator;
		if (spell != null) spell.perform(world, pos, activator);
		markDirty();
	}

	public void performSpell() {
		if (spell != null) spell.perform(world, pos, activator);
		markDirty();
	}

	@Override
	public void fromTag(CompoundTag tag) {
		super.fromTag(tag);
		if (tag.containsKey("Spell", NbtType.COMPOUND)) this.spell = Spell.fromTag(tag.getCompound("Spell"));
		if (tag.containsKey("ActivatorMost", NbtType.LONG)) this.activator = world.getPlayerByUuid(tag.getUuid("Activator"));
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		super.toTag(tag);
		if (spell != null) tag.put("Spell", spell.toTag());
		if (activator != null) tag.putUuid("Activator", activator.getUuid());
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
