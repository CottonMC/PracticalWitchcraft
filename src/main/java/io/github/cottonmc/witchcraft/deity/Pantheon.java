package io.github.cottonmc.witchcraft.deity;

import io.github.cottonmc.cotton.registry.CommonTags;
import io.github.cottonmc.witchcraft.Witchcraft;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.stat.Stat;
import net.minecraft.stat.Stats;
import net.minecraft.tag.ItemTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.DefaultedRegistry;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Pantheon {
	public static final Registry<Deity> DEITIES;

	static {
		MutableRegistry<Deity> temp_init = new DefaultedRegistry<>("witchcraft:nameless");
		Registry.register(Registry.REGISTRIES, new Identifier(Witchcraft.MODID, "deities"), temp_init);
		DEITIES = temp_init;
	}

	public static Deity NATURE = register("nature", new Deity(deity -> {
		Map<Stat, Float> favors = new HashMap<>();
		Map<Stat, Float> disfavors = new HashMap<>();
		List<Item> plants = new ArrayList<>(ItemTags.SAPLINGS.values());
		plants.addAll(CommonTags.PLANTABLES.values());
		for (Item item : plants) {
			Stat stat = Stats.USED.getOrCreateStat(item);
			favors.put(stat, 0.01f);
		}

		EntityType[] rare_passives = new EntityType[]{EntityType.TURTLE, EntityType.POLAR_BEAR, EntityType.FOX, EntityType.PANDA};
		for (EntityType type : rare_passives) {
			Stat stat = Stats.KILLED.getOrCreateStat(type);
			disfavors.put(stat, 1f);
		}
		deity.addFavors(favors);
		deity.addDisfavors(disfavors);
	}));

	public static void init() {
	}

	private static Deity register(String name, Deity deity) {
		return Registry.register(DEITIES, new Identifier(Witchcraft.MODID, name), deity);
	}
}
