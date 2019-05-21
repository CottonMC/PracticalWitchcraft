package io.github.cottonmc.witchcraft.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.*;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class CauldronRecipe implements Recipe<CauldronInventoryWrapper> {
	Identifier id;
	DefaultedList<Ingredient> inputs;
	ItemStack output;
	boolean needsFire;

	public CauldronRecipe(Identifier id, ItemStack output, DefaultedList<Ingredient> inputs, boolean needsFire) {
		this.id = id;
		this.inputs = inputs;
		this.output = output;
		this.needsFire = needsFire;
	}

	@Override
	public boolean matches(CauldronInventoryWrapper inventory, World world) {
		if (needsFire && !inventory.hasFire) return false;

		RecipeFinder finder = new RecipeFinder();
		int ingredients = 0;

		for(int i = 0; i < inventory.getInvSize(); ++i) {
			ItemStack stack = inventory.getInvStack(i);
			if (!stack.isEmpty()) {
				++ingredients;
				finder.method_20478(stack, 1);
			}
		}

		return ingredients == this.inputs.size() && finder.findRecipe(this, null) && inventory.hasFire == needsFire;
	}

	@Override
	public ItemStack craft(CauldronInventoryWrapper inventory) {
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
