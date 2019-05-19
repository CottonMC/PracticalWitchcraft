package io.github.cottonmc.witchcraft.item;

import io.github.cottonmc.witchcraft.Witchcraft;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class WitchcraftItems {

	public static Item BROOMSTICK;

	public static final ItemGroup BREWERY_GROUP = FabricItemGroupBuilder.build(new Identifier(Witchcraft.MODID, "main_group"), () -> new ItemStack(BROOMSTICK));

	public static Item PURGING_INCENSE = register("purging_incense", new IncenseStickItem());

	public static void init() {
		BROOMSTICK = register("broomstick", new Item(new Item.Settings().itemGroup(BREWERY_GROUP).stackSize(1)));
	}

	public static Item register(String name, Item item) {
		Registry.register(Registry.ITEM, new Identifier(Witchcraft.MODID, name), item);
		return item;
	}
}
