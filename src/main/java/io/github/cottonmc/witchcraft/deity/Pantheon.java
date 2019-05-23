package io.github.cottonmc.witchcraft.deity;

import io.github.cottonmc.witchcraft.Witchcraft;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.DefaultedRegistry;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;

public class Pantheon {
	public static final Registry<Deity> DEITIES;

	static {
		MutableRegistry<Deity> temp_init = new DefaultedRegistry<>("witchcraft:no_deity");
		Registry.register(Registry.REGISTRIES, new Identifier(Witchcraft.MODID, "deities"), temp_init);
		DEITIES = temp_init;
	}

	public static Deity NATURE = register("nature", new Deity(new HashMap<>(), new HashMap<>()));

	public static void init() {
	}

	private static Deity register(String name, Deity deity) {
		return Registry.register(DEITIES, new Identifier(Witchcraft.MODID, name), deity);
	}
}
