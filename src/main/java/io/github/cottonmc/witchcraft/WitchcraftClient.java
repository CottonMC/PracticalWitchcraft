package io.github.cottonmc.witchcraft;

import io.github.cottonmc.witchcraft.block.WitchcraftBlocks;
import io.github.cottonmc.witchcraft.block.render.StoneCauldronRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;

public class WitchcraftClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		 BlockEntityRendererRegistry.INSTANCE.register(WitchcraftBlocks.STONE_CAULDRON_BE, StoneCauldronRenderer::new);
		 BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), WitchcraftBlocks.BORAGE,
				 WitchcraftBlocks.PINK_BORAGE,
				 WitchcraftBlocks.POTTED_BORAGE,
				 WitchcraftBlocks.POTTED_PINK_BORAGE,
				 WitchcraftBlocks.STONE_CAULDRON,
				 WitchcraftBlocks.INCENSE_BURNER);
	}
}
