package io.github.cottonmc.witchcraft.recipe;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeFinder;
import net.minecraft.recipe.RecipeInputProvider;
import net.minecraft.util.DefaultedList;

public class CauldronInventoryWrapper implements Inventory, RecipeInputProvider {
	private DefaultedList<ItemStack> items;
	public final boolean hasFire;

	public CauldronInventoryWrapper(DefaultedList<ItemStack> items, boolean hasFire) {
		this.items = items;
		this.hasFire = hasFire;
	}

	@Override
	public int getInvSize() {
		return 8;
	}

	@Override
	public boolean isInvEmpty() {
		for (ItemStack stack : items) {
			if (!stack.isEmpty()) return false;
		}
		return true;
	}

	@Override
	public ItemStack getInvStack(int i) {
		return items.get(i);
	}

	@Override
	public ItemStack takeInvStack(int i, int i1) {
		return null;
	}

	@Override
	public ItemStack removeInvStack(int i) {
		return null;
	}

	@Override
	public void setInvStack(int i, ItemStack itemStack) {

	}

	@Override
	public void markDirty() {

	}

	@Override
	public boolean canPlayerUseInv(PlayerEntity playerEntity) {
		return true;
	}

	@Override
	public void clear() {

	}

	@Override
	public void provideRecipeInputs(RecipeFinder finder) {
		for (ItemStack stack : items) {
			finder.addNormalItem(stack);
		}
	}
}
