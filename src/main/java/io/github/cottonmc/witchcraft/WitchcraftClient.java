package io.github.cottonmc.witchcraft;

import io.github.cottonmc.witchcraft.block.entity.StoneCauldronEntity;
import io.github.cottonmc.witchcraft.block.render.StoneCauldronRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.render.BlockEntityRendererRegistry;

public class WitchcraftClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		 BlockEntityRendererRegistry.INSTANCE.register(StoneCauldronEntity.class, new StoneCauldronRenderer());

	}
}
