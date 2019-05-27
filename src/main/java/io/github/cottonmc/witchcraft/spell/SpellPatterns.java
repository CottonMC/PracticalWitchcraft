package io.github.cottonmc.witchcraft.spell;

import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class SpellPatterns {
	private static Map<Identifier, ActionPattern> ACTIONS = new HashMap<>();
	private static Map<Identifier, TargeterPattern> TARGETERS = new HashMap<>();
	private static Map<Identifier, ModifierPattern> MODIFIERS = new HashMap<>();

	public static ActionPattern registerAction(Identifier id, ActionPattern pattern) {
		ACTIONS.put(id, pattern);
		return pattern;
	}

	public static TargeterPattern registerTargeter(Identifier id, TargeterPattern pattern) {
		TARGETERS.put(id, pattern);
		return pattern;
	}

	public static ModifierPattern registerModifier(Identifier id, ModifierPattern pattern) {
		MODIFIERS.put(id, pattern);
		return pattern;
	}

	public static ActionPattern getAction(Identifier id) {
		return ACTIONS.get(id);
	}

	public static Identifier getActionId(ActionPattern pattern) {
		for (Identifier id : ACTIONS.keySet()) {
			if (ACTIONS.get(id) == pattern) return id;
		}
		return null;
	}

	public static TargeterPattern getTargeter(Identifier id) {
		return TARGETERS.get(id);
	}

	public static Identifier getTargeterId(TargeterPattern pattern) {
		for (Identifier id : TARGETERS.keySet()) {
			if (TARGETERS.get(id) == pattern) return id;
		}
		return null;
	}

	public static ModifierPattern getModifier(Identifier id) {
		return MODIFIERS.get(id);
	}

	public static Identifier getModifierId(ModifierPattern pattern) {
		for (Identifier id : MODIFIERS.keySet()) {
			if (MODIFIERS.get(id) == pattern) return id;
		}
		return null;
	}

}
