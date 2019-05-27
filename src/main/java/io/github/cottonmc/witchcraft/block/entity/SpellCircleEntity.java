package io.github.cottonmc.witchcraft.block.entity;

import io.github.cottonmc.witchcraft.block.WitchcraftBlocks;
import io.github.cottonmc.witchcraft.spell.Spell;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;

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
		this.spell.perform(world, pos, activator);
		markDirty();
	}

	public void performSpell() {
		this.spell.perform(world, pos, activator);
		markDirty();
	}

	@Override
	public void fromTag(CompoundTag tag) {
		super.fromTag(tag);
		this.spell = Spell.fromTag(tag.getCompound("Spell"));
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		super.toTag(tag);
		if (spell != null) tag.put("Spell", spell.toTag());
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
