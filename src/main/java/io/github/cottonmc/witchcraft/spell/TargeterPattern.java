package io.github.cottonmc.witchcraft.spell;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public abstract class TargeterPattern implements SpellPattern {
	private String name;
	private Identifier texture;
	private int weight;

	public TargeterPattern(String name, Identifier texture, int weight) {
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
		return !spell.getTargeters().contains(this);
	}

	public abstract List<Entity> getTargets(World world, BlockPos pos, PlayerEntity activator, Spell spell);
}
