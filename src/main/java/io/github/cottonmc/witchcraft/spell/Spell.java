package io.github.cottonmc.witchcraft.spell;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class Spell {
	private List<ActionPattern> actions;
	private List<TargeterPattern> targeters;
	private List<ModifierPattern> modifiers;

	public Spell(List<ActionPattern> actions, List<TargeterPattern> targeters, List<ModifierPattern> modifiers) {
		this.actions = actions;
		this.targeters = targeters;
		this.modifiers = modifiers;
	}

	public List<ActionPattern> getActions() {
		return actions;
	}

	public List<TargeterPattern> getTargeters() {
		return targeters;
	}

	public List<ModifierPattern> getModifiers() {
		return modifiers;
	}

	public int getCumulativeWeight() {
		int weight = 0;
		for (ActionPattern action : actions) {
			weight += action.getWeight();
		}
		for (TargeterPattern targeter : targeters) {
			weight += targeter.getWeight();
		}
		for (ModifierPattern modifier : modifiers) {
			weight += modifier.getWeight();
		}
		return weight;
	}

	public List<Entity> getTargets(World world, BlockPos pos, PlayerEntity activator) {
		List<Entity> entities = new ArrayList<>();
		for (TargeterPattern targeter : targeters) {
			entities.addAll(targeter.getTargets(world, pos, activator, this));
		}
		return entities;
	}

	public void perform(World world, BlockPos pos, PlayerEntity activator) {
		for (ActionPattern action : actions) {
			action.performAction(world, pos, activator,this);
		}
	}
}
