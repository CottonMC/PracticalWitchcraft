package io.github.cottonmc.witchcraft.spell;

import java.util.HashSet;
import java.util.Set;

public class SpellPatterns {
	private static Set<ActionPattern> ACTIONS = new HashSet<>();
	private static Set<TargeterPattern> TARGETERS = new HashSet<>();
	private static Set<ModifierPattern> MODIFIERS = new HashSet<>();

	public static ActionPattern registerAction(ActionPattern pattern) {
		ACTIONS.add(pattern);
		return pattern;
	}

	public static TargeterPattern registerTargeter(TargeterPattern pattern) {
		TARGETERS.add(pattern);
		return pattern;
	}

	public static ModifierPattern registerModifier(ModifierPattern pattern) {
		MODIFIERS.add(pattern);
		return pattern;
	}

	public static Set<ActionPattern> getActions() {
		return new HashSet<>(ACTIONS);
	}

	public static Set<TargeterPattern> getTargeters() {
		return new HashSet<>(TARGETERS);
	}

	public static Set<ModifierPattern> getModifiers() {
		return new HashSet<>(MODIFIERS);
	}
}
