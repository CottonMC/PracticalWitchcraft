package io.github.cottonmc.brewery;

import io.github.cottonmc.brewery.cauldron.StoneCauldronEntity;
import io.github.cottonmc.brewery.cauldron.StoneCauldronRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.render.BlockEntityRendererRegistry;

public class BreweryClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		 BlockEntityRendererRegistry.INSTANCE.register(StoneCauldronEntity.class, new StoneCauldronRenderer());

	}
}
