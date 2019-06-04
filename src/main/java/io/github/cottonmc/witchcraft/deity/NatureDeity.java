package io.github.cottonmc.witchcraft.deity;

import io.github.cottonmc.cotton.registry.CommonTags;
import net.minecraft.ChatFormat;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.stat.Stat;
import net.minecraft.stat.Stats;
import net.minecraft.tag.ItemTags;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NatureDeity extends Deity {
	public NatureDeity() {
		super(ChatFormat.GREEN);
	}

	@Override
	public void reload() {
		super.reload();
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
		this.addFavors(favors);
		this.addDisfavors(disfavors);
	}

	@Override
	public String getNameSubkey(PlayerEntity player) {
		return null;
	}

	@Override
	public void bless(PlayerEntity player, float totalFavor) {

	}

	@Override
	public void curse(PlayerEntity player, float totalFavor) {

	}
}
