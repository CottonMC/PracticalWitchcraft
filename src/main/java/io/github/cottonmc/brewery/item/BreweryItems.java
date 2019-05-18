package io.github.cottonmc.brewery.item;

import io.github.cottonmc.brewery.Brewery;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class BreweryItems {

	public static final Item PURGING_INCENSE = register("purging_incense", new Item(new Item.Settings().itemGroup(ItemGroup.MATERIALS)));

	public static void init() {

	}

	public static Item register(String name, Item item) {
		Registry.register(Registry.ITEM, new Identifier(Brewery.MODID, name), item);
		return item;
	}
}
