package io.github.cottonmc.witchcraft.recipe;

import io.github.cottonmc.libcd.tweaker.RecipeParser;
import io.github.cottonmc.libcd.tweaker.RecipeTweaker;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Identifier;

public class WitchcraftRecipeTweaker {
	public static final WitchcraftRecipeTweaker INSTANCE = new WitchcraftRecipeTweaker();

	public void addCauldron(Object[] inputs, ItemStack output, boolean needsFire) {
		Identifier recipeId = RecipeTweaker.INSTANCE.getRecipeId(output);
		try {
			DefaultedList<Ingredient> ingredients = DefaultedList.create();
			for (int i = 0; i < Math.min(inputs.length, 9); i++) {
				Object id = inputs[i];
				if (id.equals("")) continue;
				ingredients.add(i, RecipeParser.processIngredient(id));
			}
			RecipeTweaker.INSTANCE.addRecipe(new CauldronRecipe(recipeId, output, ingredients, needsFire));
		} catch (Exception e) {
			System.out.println("Error parsing cauldron recipe - " + e.getMessage());
		}
	}
}