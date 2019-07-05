package io.github.cottonmc.witchcraft.item;

import io.github.cottonmc.witchcraft.util.IncenseAction;
import net.minecraft.item.Item;

public class IncenseStickItem extends Item {
	private IncenseAction action;

	public IncenseStickItem(IncenseAction action) {
		super(new Item.Settings().group(WitchcraftItems.WITCHCRAFT_GROUP));
		this.action = action;
	}

	public IncenseAction getAction() {
		return action;
	}
}
