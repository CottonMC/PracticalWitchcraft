package io.github.cottonmc.witchcraft.spell;

import io.github.cottonmc.witchcraft.block.WitchcraftBlocks;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class Spell {
	private ActionPattern action;
	private List<TargeterPattern> targeters;
	private List<ModifierPattern> modifiers;
	private int performances;

	public Spell(ActionPattern action, List<TargeterPattern> targeters, List<ModifierPattern> modifiers) {
		this(action, targeters, modifiers, 0);
	}

	public Spell(ActionPattern action, List<TargeterPattern> targeters, List<ModifierPattern> modifiers, int performances) {
		this.action = action;
		this.targeters = targeters;
		this.modifiers = modifiers;
		this.performances = performances;
	}

	public ActionPattern getAction() {
		return action;
	}

	public List<TargeterPattern> getTargeters() {
		return targeters;
	}

	public List<ModifierPattern> getModifiers() {
		return modifiers;
	}

	public List<Entity> getTargets(World world, BlockPos pos, PlayerEntity activator) {
		List<Entity> entities = new ArrayList<>();
		for (TargeterPattern targeter : targeters) {
			entities.addAll(targeter.getTargets(world, pos, activator, this));
		}
		return entities;
	}

	public int getPerformances() {
		return performances;
	}

	public void perform(World world, BlockPos pos, PlayerEntity activator) {
		int result = action.performAction(world, pos, activator,this, performances);
		if (result > 0) world.getBlockTickScheduler().schedule(pos, WitchcraftBlocks.SPELL_CIRCLE, result);
		performances++;
	}

	public CompoundTag toTag() {
		CompoundTag tag = new CompoundTag();
		tag.putString("Action", SpellPatterns.getActionId(action).toString());
		if (!targeters.isEmpty()) {
			ListTag targeterList = new ListTag();
			for (int i = 0; i < targeters.size(); i++) {
				targeterList.addTag(i, new StringTag(SpellPatterns.getTargeterId(targeters.get(i)).toString()));
			}
			tag.put("Targeters", targeterList);
		}
		if (!modifiers.isEmpty()) {
			ListTag modifierList = new ListTag();
			for (int i = 0; i < modifiers.size(); i++) {
				modifierList.addTag(i, new StringTag(SpellPatterns.getModifierId(modifiers.get(i)).toString()));
			}
			tag.put("Modifiers", modifierList);
		}
		tag.putInt("Performances", performances);
		return tag;
	}

	public static Spell fromTag(CompoundTag tag) {
		ActionPattern action = SpellPatterns.getAction(new Identifier(tag.getString("Action")));
		List<TargeterPattern> targeters = new ArrayList<>();
		List<ModifierPattern> modifiers = new ArrayList<>();
		int performances = 0;
		if (tag.containsKey("Targeters", NbtType.LIST)) {
			ListTag targeterList = tag.getList("Targeters", NbtType.STRING);
			for (int i = 0; i < targeterList.size(); i++) {
				String id = targeterList.getString(i);
				targeters.add(SpellPatterns.getTargeter(new Identifier(id)));
			}
		}
		if (tag.containsKey("Modifiers", NbtType.LIST)) {
			ListTag modifierList = tag.getList("Modifiers", NbtType.STRING);
			for (int i = 0; i < modifierList.size(); i++) {
				String id = modifierList.getString(i);
				modifiers.add(SpellPatterns.getModifier(new Identifier(id)));
			}
		}
		if (tag.containsKey("Performances", NbtType.INT)) performances = tag.getInt("Performances");
		return new Spell(action, targeters, modifiers, performances);
	}
}
