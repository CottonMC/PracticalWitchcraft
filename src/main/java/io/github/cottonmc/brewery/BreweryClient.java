package io.github.cottonmc.brewery;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.render.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.render.EntityRendererRegistry;

public class BreweryClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		 BlockEntityRendererRegistry.INSTANCE.register(StoneCauldronEntity.class, new StoneCauldronRenderer());

	}
}
