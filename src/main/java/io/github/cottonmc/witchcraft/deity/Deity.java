package io.github.cottonmc.witchcraft.deity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.stat.Stat;
import net.minecraft.util.Identifier;

import java.util.Map;

public class Deity {
	private Map<Stat, Float> favors;
	private Map<Stat, Float> disfavors;

	public Deity(Map<Stat, Float> favors, Map<Stat, Float> disfavors) {
		this.favors = favors;
		this.disfavors = disfavors;
	}

	public void update(PlayerEntity player, Stat action, int amount) {
		if (favors.containsKey(action)) FavorManager.shiftFavor(player, this, favors.get(action) * amount);
		else if (disfavors.containsKey(action)) FavorManager.shiftFavor(player, this, disfavors.get(action) * amount * -1);
	}

	public String getName() {
		Identifier id = Pantheon.DEITIES.getId(this);
		if (id == null) return "■■■■■";
		return new TranslatableComponent("deity." + id.getNamespace() + "." + id.getPath()).getText();
	}

	public void prepareReload() {
		favors.clear();
		disfavors.clear();
	}

	public void addFavors(Map<Stat, Float> favors) {
		this.favors.putAll(favors);
	}

	public void addDisfavors(Map<Stat, Float> disfavors) {
		this.disfavors.putAll(disfavors);
	}
}
