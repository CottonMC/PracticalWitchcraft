package io.github.cottonmc.witchcraft.recipe;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.*;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class CauldronRecipe implements Recipe<Inventory> {
	Identifier id;
	DefaultedList<Ingredient> inputs;
	ItemStack output;

	public CauldronRecipe(Identifier id, ItemStack output, DefaultedList<Ingredient> inputs) {
		this.id = id;
		this.inputs = inputs;
		this.output = output;
	}

	@Override
	public boolean matches(Inventory inventory, World world) {
		RecipeFinder finder = new RecipeFinder();
		int ingredients = 0;

		for(int i = 0; i < inventory.getInvSize(); ++i) {
			ItemStack stack = inventory.getInvStack(i);
			if (!stack.isEmpty()) {
				++ingredients;
				finder.method_20478(stack, 1);
			}
		}

		return ingredients == this.inputs.size() && finder.findRecipe(this, null);
	}

	@Override
	public ItemStack craft(Inventory inventory) {
		return this.output.copy();
	}

	@Override
	public boolean fits(int i, int i1) {
		return i * i1 >= this.inputs.size();
	}

	@Override
	public ItemStack getOutput() {
		return output;
	}

	@Override
	public Identifier getId() {
		return id;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return WitchcraftRecipes.CAULDRON_SERIALIZER;
	}

	@Override
	public RecipeType<?> getType() {
		return WitchcraftRecipes.CAULDRON;
	}

	@Override
	public DefaultedList<Ingredient> getPreviewInputs() {
		return inputs;
	}
}
