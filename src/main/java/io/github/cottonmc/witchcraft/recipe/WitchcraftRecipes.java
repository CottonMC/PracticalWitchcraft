package io.github.cottonmc.witchcraft.recipe;

import io.github.cottonmc.cotton.cauldron.Cauldron;
import io.github.cottonmc.cotton.cauldron.CauldronBehavior;
import io.github.cottonmc.libcd.tweaker.Tweaker;
import io.github.cottonmc.witchcraft.Witchcraft;
import io.github.cottonmc.witchcraft.item.WitchcraftItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CampfireBlock;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class WitchcraftRecipes {

	public static final RecipeType<CauldronRecipe> CAULDRON = register("cauldron");
	public static final RecipeSerializer<CauldronRecipe> CAULDRON_SERIALIZER = register("cauldron", new CauldronRecipeSerializer());

	public static void init() {
		CauldronBehavior.registerBehavior(
				(ctx) -> ctx.getStack().getItem() == WitchcraftItems.BROOMSTICK
						&& ctx.getFluid() == Fluids.WATER
						&& ctx.getLevel() >= 1
						&& ctx.getWorld().getRecipeManager().getFirstMatch(WitchcraftRecipes.CAULDRON,
						new CauldronInventoryWrapper(ctx.getPreviousItems(), isFireUnder(ctx.getWorld(), ctx.getPos())), ctx.getWorld()).isPresent(),
				(ctx -> {
					PlayerEntity player = ctx.getPlayer();
					CauldronInventoryWrapper wrapper = new CauldronInventoryWrapper(ctx.getPreviousItems(), isFireUnder(ctx.getWorld(), ctx.getPos()));
					CauldronRecipe recipe = ctx.getWorld().getRecipeManager().getFirstMatch(WitchcraftRecipes.CAULDRON, wrapper, ctx.getWorld()).get();
					ItemStack result = recipe.craft(wrapper);
					if (player != null) {
						player.increaseStat(Stats.USE_CAULDRON, 1);
						if (!player.inventory.insertStack(result)) {
							ItemEntity entity = player.dropItem(result, false);
							if (entity != null) entity.addScoreboardTag("NoCauldronCollect");
							ctx.getWorld().spawnEntity(entity);
						}
					} else {
						ItemEntity entity = new ItemEntity(ctx.getWorld(), ctx.getPos().getX(), ctx.getPos().getY()+1, ctx.getPos().getZ(), result);
						entity.addScoreboardTag("NoCauldronCollect");
						ctx.getWorld().spawnEntity(entity);
					}
					((Cauldron)ctx.getState().getBlock()).drain(ctx.getWorld(), ctx.getPos(), ctx.getState(), Fluids.WATER, 1);
					ctx.getWorld().playSound(null, ctx.getPos(), SoundEvents.BLOCK_NETHER_WART_BREAK, SoundCategory.BLOCKS, 1.0f, 1.0f);
				})
		);
		Tweaker.addAssistant("WitchcraftRecipeTweaker", WitchcraftRecipeTweaker.INSTANCE);
	}

	public static boolean isFireUnder(World world, BlockPos pos) {
		BlockState state = world.getBlockState(pos.down());
		if (state.getBlock() == Blocks.FIRE) return true;
		if (state.getBlock() == Blocks.CAMPFIRE) return state.get(CampfireBlock.LIT);
		return false;
	}

	public static <T extends Recipe<?>> RecipeType<T> register(String id) {
		return Registry.register(Registry.RECIPE_TYPE, new Identifier(Witchcraft.MODID, id), new RecipeType<T>() {
			public String toString() {
				return id;
			}
		});
	}

	public static <S extends RecipeSerializer<T>, T extends Recipe<?>> S register(String name, S serializer) {
		return Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(Witchcraft.MODID, name), serializer);
	}
}
