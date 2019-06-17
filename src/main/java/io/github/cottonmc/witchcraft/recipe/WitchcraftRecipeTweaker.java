package io.github.cottonmc.witchcraft.recipe;

import io.github.cottonmc.libcd.tweaker.RecipeParser;
import io.github.cottonmc.libcd.tweaker.RecipeTweaker;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Identifier;

public class WitchcraftRecipeTweaker {
	public static void addCauldron(String[] inputs, ItemStack output, boolean needsFire) {
		Identifier recipeId = RecipeTweaker.getRecipeId(output);
		try {
			DefaultedList<Ingredient> ingredients = DefaultedList.create();
			for (int i = 0; i < Math.min(inputs.length, 9); i++) {
				String id = inputs[i];
				if (id.equals("")) continue;
				ingredients.add(i, RecipeParser.processIngredient(id));
			}
			RecipeTweaker.addRecipe(new CauldronRecipe(recipeId, output, ingredients, needsFire));
		} catch (Exception e) {
			System.out.println("Error parsing cauldron recipe - " + e.getMessage());
		}
	}
}