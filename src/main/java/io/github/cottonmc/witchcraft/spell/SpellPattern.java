package io.github.cottonmc.witchcraft.spell;

import net.minecraft.util.Identifier;

public interface SpellPattern {

	String getName();

	Identifier getTexture();

	int getWeight();

	boolean canAddTo(Spell spell);
}
