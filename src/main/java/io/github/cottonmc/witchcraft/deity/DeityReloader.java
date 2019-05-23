package io.github.cottonmc.witchcraft.deity;

import io.github.cottonmc.witchcraft.Witchcraft;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.resource.ResourceManager;
import net.minecraft.stat.Stat;
import net.minecraft.stat.Stats;
import net.minecraft.tag.ItemTags;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class DeityReloader implements SimpleSynchronousResourceReloadListener {
	@Override
	public Identifier getFabricId() {
		return new Identifier(Witchcraft.MODID, "deity_reloader");
	}

	@Override
	public void apply(ResourceManager manager) {
		Deity nature = Pantheon.DEITIES.get(new Identifier(Witchcraft.MODID, "nature"));
		nature.prepareReload();
		Map<Stat, Float> favors = new HashMap<>();
		Map<Stat, Float> disfavors = new HashMap<>();
		for (Item item : ItemTags.SAPLINGS.values()) {
			Stat stat = Stats.USED.getOrCreateStat(item);
			favors.put(stat, 0.01f);
		}
		EntityType[] rare_passives = new EntityType[]{EntityType.TURTLE, EntityType.POLAR_BEAR, EntityType.FOX, EntityType.MOOSHROOM};
		for (EntityType type : rare_passives) {
			Stat stat = Stats.KILLED.getOrCreateStat(type);
			disfavors.put(stat, 1f);
		}
		nature.addFavors(favors);
		nature.addDisfavors(disfavors);
	}
}
