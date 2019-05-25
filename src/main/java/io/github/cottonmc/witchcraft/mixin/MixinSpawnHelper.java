package io.github.cottonmc.witchcraft.mixin;

import io.github.cottonmc.witchcraft.block.FaeLanternBlock;
import io.github.cottonmc.witchcraft.block.WitchcraftBlocks;
import io.github.cottonmc.witchcraft.block.entity.FaeLanternEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityCategory;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.ViewableWorld;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

@Mixin(SpawnHelper.class)
public abstract class MixinSpawnHelper {
	@Inject(method = "canSpawn", at = @At("HEAD"), cancellable = true)
	private static void faeSpawnCancel(SpawnRestriction.Location location, ViewableWorld world, BlockPos pos, EntityType<?> type, CallbackInfoReturnable cir) {
		if (type == null) return;
		if (type.getCategory() == EntityCategory.MONSTER) {
			Chunk spawnIn = world.getChunk(pos);
			Set<BlockPos> positions = spawnIn.getBlockEntityPositions();
			for (BlockPos bePos : positions) {
				BlockState state = world.getBlockState(bePos);
				if (state.getBlock() == WitchcraftBlocks.FAE_LANTERN) {
					if (state.get(FaeLanternBlock.PIXIES) > 0) {
						cir.setReturnValue(false);
						return;
					}
				}
			}
		}
	}
}
