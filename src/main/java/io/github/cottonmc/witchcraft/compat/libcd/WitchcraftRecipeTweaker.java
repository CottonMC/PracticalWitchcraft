package io.github.cottonmc.witchcraft.compat.libcd;

import io.github.cottonmc.libcd.api.tweaker.recipe.RecipeParser;
import io.github.cottonmc.libcd.api.tweaker.recipe.RecipeTweaker;
import io.github.cottonmc.witchcraft.recipe.CauldronRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Identifier;

public class WitchcraftRecipeTweaker {
	public static final WitchcraftRecipeTweaker INSTANCE = new WitchcraftRecipeTweaker();

	public void addCauldron(Object[] inputs, Object output, boolean needsFire) {
		try {
			ItemStack out = RecipeParser.processItemStack(output);
			Identifier recipeId = RecipeTweaker.INSTANCE.getRecipeId(out);
			DefaultedList<Ingredient> ingredients = DefaultedList.of();
			for (int i = 0; i < Math.min(inputs.length, 9); i++) {
				Object id = inputs[i];
				if (id.equals("")) continue;
				ingredients.add(i, RecipeParser.processIngredient(id));
			}
			RecipeTweaker.INSTANCE.addRecipe(new CauldronRecipe(recipeId, out, ingredients, needsFire));
		} catch (Exception e) {
			System.out.println("Error parsing cauldron recipe - " + e.getMessage());
		}
	}
}