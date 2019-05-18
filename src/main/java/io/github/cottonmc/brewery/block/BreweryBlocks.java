package io.github.cottonmc.brewery.block;

import io.github.cottonmc.brewery.Brewery;
import io.github.cottonmc.brewery.block.entity.IncenseBurnerEntity;
import io.github.cottonmc.brewery.block.entity.StoneCauldronEntity;
import io.github.cottonmc.brewery.item.BreweryItems;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.function.Supplier;

public class BreweryBlocks {

	public static final Block STONE_CAULDRON = register("stone_cauldron", new StoneCauldronBlock(), ItemGroup.DECORATIONS);
	public static final BlockEntityType<StoneCauldronEntity> STONE_CAULDRON_BE = register("stone_cauldron", StoneCauldronEntity::new, STONE_CAULDRON);

	public static final Block INCENSE_BURNER = register("incense_burner", new IncenseBurnerBlock(), ItemGroup.DECORATIONS);
	public static final BlockEntityType<IncenseBurnerEntity> INCENSE_BURNER_BE = register("incense_burner", IncenseBurnerEntity::new, INCENSE_BURNER);

	public static void init() {
	}

	public static Block register(String name, Block block, ItemGroup tab) {
		Registry.register(Registry.BLOCK, new Identifier(Brewery.MODID, name), block);
		BlockItem item = new BlockItem(block, new Item.Settings().itemGroup(tab));
		BreweryItems.register(name, item);
		return block;
	}

	public static BlockEntityType register(String name, Supplier<BlockEntity> be, Block...blocks) {
		return Registry.register(Registry.BLOCK_ENTITY, new Identifier(Brewery.MODID, name), BlockEntityType.Builder.create(be, blocks).build(null));
	}
}
