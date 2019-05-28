package io.github.cottonmc.witchcraft.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.PacketByteBuf;

public class CauldronRecipeSerializer implements RecipeSerializer<CauldronRecipe> {
	@Override
	public CauldronRecipe read(Identifier id, JsonObject json) {
		DefaultedList<Ingredient> ingredients = getIngredients(JsonHelper.getArray(json, "ingredients"));
		boolean needsFire = JsonHelper.getBoolean(json, "needsFire");
		if (ingredients.isEmpty()) throw new JsonParseException("No ingredients for cauldron recipe!");
		else {
			ItemStack result = ShapedRecipe.getItemStack(JsonHelper.getObject(json, "result"));
			return new CauldronRecipe(id,  result, ingredients, needsFire);
		}
	}

	@Override
	public CauldronRecipe read(Identifier id, PacketByteBuf buf) {
		int ingredientSize = buf.readVarInt();
		DefaultedList<Ingredient> ingredients = DefaultedList.create(ingredientSize, Ingredient.EMPTY);

		for(int i = 0; i < ingredients.size(); i++) {
			ingredients.set(i, Ingredient.fromPacket(buf));
		}

		ItemStack output = buf.readItemStack();
		boolean needsFire = buf.readBoolean();
		return new CauldronRecipe(id, output, ingredients, needsFire);
	}

	@Override
	public void write(PacketByteBuf buf, CauldronRecipe recipe) {
		buf.writeString(recipe.getGroup());

		buf.writeVarInt(recipe.getPreviewInputs().size());
		for (Ingredient ingredient : recipe.getPreviewInputs()) {
			ingredient.write(buf);
		}

		buf.writeItemStack(recipe.getOutput());

		buf.writeBoolean(recipe.needsFire);
	}

	private static DefaultedList<Ingredient> getIngredients(JsonArray json) {
		DefaultedList<Ingredient> ingredients = DefaultedList.create();

		for(int i = 0; i < json.size(); i++) {
			Ingredient ingredient = Ingredient.fromJson(json.get(i));
			if (!ingredient.isEmpty()) {
				ingredients.add(ingredient);
			}
		}

		return ingredients;
	}
}
