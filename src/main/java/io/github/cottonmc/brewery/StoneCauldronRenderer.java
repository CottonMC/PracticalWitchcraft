package io.github.cottonmc.brewery;

import com.mojang.blaze3d.platform.GlStateManager;
import io.github.prospector.silk.fluid.DropletValues;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import org.lwjgl.opengl.GL11;

public class StoneCauldronRenderer extends BlockEntityRenderer<StoneCauldronEntity> {

	@Override
	public void render(StoneCauldronEntity be, double x, double y, double z, float partialTicks, int destroyStage) {
		final Tessellator tessellator = Tessellator.getInstance();
		final BufferBuilder buffer = tessellator.getBufferBuilder();
		buffer.setOffset(x, y, z);
		GlStateManager.enableBlend();
		GlStateManager.disableAlphaTest();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		renderManager.textureManager.bindTexture(SpriteAtlasTexture.BLOCK_ATLAS_TEX);
		//TODO: change once the fabric fluid renderer PR is merged
		Sprite sprite = MinecraftClient.getInstance().getSpriteAtlas().getSprite("minecraft:block/water_still");
		buffer.begin(GL11.GL_QUADS, VertexFormats.POSITION_UV_LMAP_COLOR);
		int amount = be.fluid.getFluid(0).getAmount();
		if (amount > 0) {
			double height = ((be.fluid.getFluid(0).getAmount() / DropletValues.BLOCK) * 14 / 16f) + 2 / 16f;
			buffer.vertex(0, height, 0).texture(sprite.getMinU(), sprite.getMinV()).texture(240, 240).color(1f, 1f, 1f, 1f).next();
			buffer.vertex(0, height, 1).texture(sprite.getMinU(), sprite.getMaxV()).texture(240, 240).color(1f, 1f, 1f, 1f).next();
			buffer.vertex(1, height, 1).texture(sprite.getMaxU(), sprite.getMaxV()).texture(240, 240).color(1f, 1f, 1f, 1f).next();
			buffer.vertex(1, height, 0).texture(sprite.getMaxU(), sprite.getMinV()).texture(240, 240).color(1f, 1f, 1f, 1f).next();
		}
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		buffer.setOffset(0.0, 0.0, 0.0);
		tessellator.draw();
		GlStateManager.disableBlend();
		GlStateManager.enableAlphaTest();
		super.render(be, x, y, z, partialTicks, destroyStage);
	}
}
