package io.github.cottonmc.witchcraft.deity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.stat.Stat;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

/**
 * A deity of the world, which a player can pray to.
 */
public class Deity {
	private Map<Stat, Float> favors = new HashMap<>();
	private Map<Stat, Float> disfavors = new HashMap<>();
	private DeityReload reloader;

	public Deity(DeityReload reloader) {
		this.reloader = reloader;
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

	public void reload() {
		favors.clear();
		disfavors.clear();
		reloader.reload(this);
	}

	public void addFavors(Map<Stat, Float> favors) {
		this.favors.putAll(favors);
	}

	public void addDisfavors(Map<Stat, Float> disfavors) {
		this.disfavors.putAll(disfavors);
	}

	public void bless(PlayerEntity player) {

	}

	public void curse(PlayerEntity player) {

	}
}
