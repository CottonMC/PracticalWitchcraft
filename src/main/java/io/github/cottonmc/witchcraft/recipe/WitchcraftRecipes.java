package io.github.cottonmc.witchcraft.recipe;

import io.github.cottonmc.cotton.cauldron.CauldronBehavior;
import io.github.cottonmc.witchcraft.Witchcraft;
import io.github.cottonmc.witchcraft.block.WitchcraftBlocks;
import io.github.cottonmc.witchcraft.item.WitchcraftItems;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class WitchcraftRecipes {

	public static final RecipeType<CauldronRecipe> CAULDRON = register("cauldron");

	public static void init() {
		CauldronBehavior.registerBehavior((ctx) -> ctx.getWorld().getBlockState(ctx.getPos()).getBlock() == WitchcraftBlocks.STONE_CAULDRON
				&& ctx.getStack().getItem() == WitchcraftItems.BROOMSTICK
				&& ctx.getWorld().getServer().getRecipeManager().getFirstMatch(WitchcraftRecipes.CAULDRON,
				new CauldronInventoryWrapper(ctx.getPreviousItems()), ctx.getWorld()).isPresent(), (ctx -> {

		}));
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
