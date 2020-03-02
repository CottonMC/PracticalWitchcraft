package io.github.cottonmc.witchcraft.block.render;

import io.github.cottonmc.witchcraft.block.entity.StoneCauldronEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;

public class StoneCauldronRenderer extends BlockEntityRenderer<StoneCauldronEntity> {

	public StoneCauldronRenderer(BlockEntityRenderDispatcher dispatcher) {
		super(dispatcher);
	}

	public void render(StoneCauldronEntity be, double x, double y, double z, float partialTicks, int destroyStage) {
//		final Tessellator tessellator = Tessellator.getInstance();
//		final BufferBuilder buffer = tessellator.getBufferBuilder();
//		buffer.setOffset(x, y, z);
//		GlStateManager.enableBlend();
//		GlStateManager.disableAlphaTest();
//		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
//		renderManager.textureManager.bindTexture(SpriteAtlasTexture.BLOCK_ATLAS_TEX);
//		Fluid fluid = be.fluid.getInvFluid(0).getRawFluid();
//		if (fluid != null && fluid != Fluids.EMPTY) {
//			FluidRenderHandler handler = FluidRenderHandlerRegistry.INSTANCE.get(fluid);
//			Sprite sprite = handler.getFluidSprites(be.getWorld(), be.getPos(), fluid.getDefaultState())[0];
//			buffer.begin(GL11.GL_QUADS, VertexFormats.POSITION_UV_LMAP_COLOR);
//			int amount = be.fluid.getInvFluid(0).getAmount();
//			int color = handler.getFluidColor(be.getWorld(), be.getPos(), fluid.getDefaultState());
//			float red = ((color >> 4) & 255) / 255f;
//			float green = ((color >> 2) & 255) / 255f;
//			float blue = (color & 255) / 255f;
//			if (amount > 0) {
//				double height = ((be.fluid.getInvFluid(0).getAmount_F().as1620() / (float) FluidAmount.BUCKET.as1620()) * 9 / 16f) + 6 / 16f;
//				buffer.vertex(2 / 16f, height, 2 / 16f).texture(sprite.getMinU(), sprite.getMinV()).texture(240, 240).color(red, green, blue, 1f).next();
//				buffer.vertex(2 / 16f, height, 14 / 16f).texture(sprite.getMinU(), sprite.getMaxV()).texture(240, 240).color(red, green, blue, 1f).next();
//				buffer.vertex(14 / 16f, height, 14 / 16f).texture(sprite.getMaxU(), sprite.getMaxV()).texture(240, 240).color(red, green, blue, 1f).next();
//				buffer.vertex(14 / 16f, height, 2 / 16f).texture(sprite.getMaxU(), sprite.getMinV()).texture(240, 240).color(red, green, blue, 1f).next();
//			}
//			GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
//			tessellator.draw();
//			GlStateManager.disableBlend();
//			GlStateManager.enableAlphaTest();
//		}
//		buffer.setOffset(0.0, 0.0, 0.0);
//		super.render(be, x, y, z, partialTicks, destroyStage);
	}

	@Override
	public void render(StoneCauldronEntity be, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {

	}
}
