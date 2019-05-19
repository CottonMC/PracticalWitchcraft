package io.github.cottonmc.witchcraft.recipe;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class CauldronRecipe implements Recipe<Inventory> {

	@Override
	public boolean matches(Inventory inventory, World world) {
		return false;
	}

	@Override
	public ItemStack craft(Inventory inventory) {
		return null;
	}

	@Override
	public boolean fits(int i, int i1) {
		return i <= 8 && i1 == 1;
	}

	@Override
	public ItemStack getOutput() {
		return null;
	}

	@Override
	public Identifier getId() {
		return null;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return null;
	}

	@Override
	public RecipeType<?> getType() {
		return null;
	}
}
