package io.github.cottonmc.witchcraft.recipe;

import io.github.cottonmc.cotton.cauldron.Cauldron;
import io.github.cottonmc.cotton.cauldron.CauldronBehavior;
import io.github.cottonmc.witchcraft.Witchcraft;
import io.github.cottonmc.witchcraft.item.WitchcraftItems;
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
import net.minecraft.util.registry.Registry;

public class WitchcraftRecipes {

	public static final RecipeType<CauldronRecipe> CAULDRON = register("cauldron");
	public static final RecipeSerializer<CauldronRecipe> CAULDRON_SERIALIZER = register("cauldron", new CauldronRecipeSerializer());

	public static void init() {
		CauldronBehavior.registerBehavior(
				(ctx) -> ctx.getStack().getItem() == WitchcraftItems.BROOMSTICK
						&& ctx.getFluid() == Fluids.WATER
						&& ctx.getLevel() >= 1
						&& ctx.getWorld().getServer().getRecipeManager().getFirstMatch(WitchcraftRecipes.CAULDRON,
						new CauldronInventoryWrapper(ctx.getPreviousItems()), ctx.getWorld()).isPresent(),
				(ctx -> {
					PlayerEntity player = ctx.getPlayer();
					CauldronRecipe recipe = ctx.getWorld().getServer().getRecipeManager().getFirstMatch(WitchcraftRecipes.CAULDRON,
							new CauldronInventoryWrapper(ctx.getPreviousItems()), ctx.getWorld()).get();
					ItemStack result = recipe.output;
					if (player != null) {
						player.increaseStat(Stats.USE_CAULDRON, 1);
						if (!player.inventory.insertStack(result)) {
							player.dropItem(result, false).addScoreboardTag("NoCauldronCollect");
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
