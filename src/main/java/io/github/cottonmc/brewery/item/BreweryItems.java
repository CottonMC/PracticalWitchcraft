package io.github.cottonmc.brewery.item;

import io.github.cottonmc.brewery.Brewery;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class BreweryItems {

	public static Item PURGING_INCENSE;

	public static final ItemGroup BREWERY_GROUP = FabricItemGroupBuilder.build(new Identifier(Brewery.MODID, "main_group"), () -> new ItemStack(PURGING_INCENSE));

	public static void init() {
		PURGING_INCENSE = register("purging_incense", new IncenseStickItem());
	}

	public static Item register(String name, Item item) {
		Registry.register(Registry.ITEM, new Identifier(Brewery.MODID, name), item);
		return item;
	}
}
