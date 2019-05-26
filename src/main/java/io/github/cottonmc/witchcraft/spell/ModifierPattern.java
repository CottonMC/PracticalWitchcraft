package io.github.cottonmc.witchcraft.spell;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class ModifierPattern implements SpellPattern {
	private String name;
	private Identifier texture;
	private int weight;

	public ModifierPattern(String name, Identifier texture, int weight) {
		this.name = name;
		this.texture = texture;
		this.weight = weight;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Identifier getTexture() {
		return texture;
	}

	@Override
	public int getWeight() {
		return weight;
	}

	@Override
	public boolean canAddTo(Spell spell) {
		return !spell.getModifiers().contains(this);
	}

	public abstract int getModifierValue(World world, BlockPos pos, PlayerEntity activator, Spell spell);
}
