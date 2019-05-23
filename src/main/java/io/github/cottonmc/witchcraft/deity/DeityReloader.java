package io.github.cottonmc.witchcraft.deity;

import io.github.cottonmc.witchcraft.Witchcraft;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

public class DeityReloader implements SimpleSynchronousResourceReloadListener {
	@Override
	public Identifier getFabricId() {
		return new Identifier(Witchcraft.MODID, "deity_reloader");
	}

	@Override
	public void apply(ResourceManager manager) {
		Pantheon.DEITIES.stream().forEach(Deity::reload);
	}
}
