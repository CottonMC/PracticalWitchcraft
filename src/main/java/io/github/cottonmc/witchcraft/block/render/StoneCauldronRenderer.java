package io.github.cottonmc.witchcraft.block.render;

import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import com.mojang.blaze3d.platform.GlStateManager;
import io.github.cottonmc.witchcraft.block.entity.StoneCauldronEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.tag.FluidTags;
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
		Sprite sprite = MinecraftClient.getInstance().getSpriteAtlas().getSprite(be.fluid.getInvFluid(0).getSprite());
		buffer.begin(GL11.GL_QUADS, VertexFormats.POSITION_UV_LMAP_COLOR);
		int amount = be.fluid.getInvFluid(0).getAmount();
		int color = (FluidTags.WATER.contains(be.fluid.getInvFluid(0).getRawFluid()))? getWorld().getBiome(be.getPos()).getWaterColor() : 0x111111;
		float red = ((color >> 4) & 255) / 255f;
		float green = ((color >> 2) & 255) / 255f;
		float blue = (color & 255) / 255f;
		if (amount > 0) {
			double height = ((be.fluid.getInvFluid(0).getAmount() / (float)FluidVolume.BUCKET) * 14 / 16f) + 1/16f;
			buffer.vertex(2/16f, height, 2/16f).texture(sprite.getMinU(), sprite.getMinV()).texture(240, 240).color(red, green, blue, 1f).next();
			buffer.vertex(2/16f, height, 14/16f).texture(sprite.getMinU(), sprite.getMaxV()).texture(240, 240).color(red, green, blue, 1f).next();
			buffer.vertex(14/16f, height, 14/16f).texture(sprite.getMaxU(), sprite.getMaxV()).texture(240, 240).color(red, green, blue, 1f).next();
			buffer.vertex(14/16f, height, 2/16f).texture(sprite.getMaxU(), sprite.getMinV()).texture(240, 240).color(red, green, blue, 1f).next();
		}
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		buffer.setOffset(0.0, 0.0, 0.0);
		tessellator.draw();
		GlStateManager.disableBlend();
		GlStateManager.enableAlphaTest();
		super.render(be, x, y, z, partialTicks, destroyStage);
	}
}
