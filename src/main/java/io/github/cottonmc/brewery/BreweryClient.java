package io.github.cottonmc.brewery;

import io.github.cottonmc.brewery.block.entity.StoneCauldronEntity;
import io.github.cottonmc.brewery.block.render.StoneCauldronRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.render.BlockEntityRendererRegistry;

public class BreweryClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		 BlockEntityRendererRegistry.INSTANCE.register(StoneCauldronEntity.class, new StoneCauldronRenderer());

	}
}
