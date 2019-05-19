package io.github.cottonmc.witchcraft.behavior;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DefaultedList;

public class CauldronInventoryWrapper implements Inventory {
	private DefaultedList<ItemStack> items;

	public CauldronInventoryWrapper(DefaultedList<ItemStack> items) {
		this.items = items;
	}

	@Override
	public int getInvSize() {
		return 8;
	}

	@Override
	public boolean isInvEmpty() {
		return !items.isEmpty();
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
}
