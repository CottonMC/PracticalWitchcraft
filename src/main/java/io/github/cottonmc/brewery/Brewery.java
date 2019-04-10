package io.github.cottonmc.brewery;

import net.fabricmc.api.ModInitializer;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.block.BlockItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.function.Supplier;

public class Brewery implements ModInitializer {
	public static final String MODID = "brewery";

	public static final Block STONE_CAULDRON = register("stone_cauldron", new StoneCauldronBlock(), ItemGroup.DECORATIONS);
    public static BlockEntityType<StoneCauldronEntity> STONE_CAULDRON_BE = register("stone_cauldron", StoneCauldronEntity::new);

	@Override
	public void onInitialize() {
	}

	public static Block register(String name, Block block, ItemGroup tab) {
		Registry.register(Registry.BLOCK, new Identifier(MODID, name), block);
		BlockItem item = new BlockItem(block, new Item.Settings().itemGroup(tab));
		register(name, item);
		return block;
	}

	public static Item register(String name, Item item) {
		Registry.register(Registry.ITEM, new Identifier(MODID, name), item);
		return item;
	}

	public static BlockEntityType register(String name, Supplier<BlockEntity> be) {
		return Registry.register(Registry.BLOCK_ENTITY, new Identifier(MODID, name), BlockEntityType.Builder.create(be).build(null));
	}

}
