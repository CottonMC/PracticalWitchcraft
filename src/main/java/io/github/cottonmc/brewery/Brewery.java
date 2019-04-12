package io.github.cottonmc.brewery;

import io.github.cottonmc.brewery.cauldron.StoneCauldronBlock;
import io.github.cottonmc.brewery.cauldron.StoneCauldronEntity;
import io.github.cottonmc.brewery.effect.BreweryStatusEffect;
import net.fabricmc.api.ModInitializer;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.item.block.BlockItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.function.Supplier;

public class Brewery implements ModInitializer {
	public static final String MODID = "brewery";

	public static final Block STONE_CAULDRON = register("stone_cauldron", new StoneCauldronBlock(), ItemGroup.DECORATIONS);
    public static BlockEntityType<StoneCauldronEntity> STONE_CAULDRON_BE = register("stone_cauldron", StoneCauldronEntity::new);

    public static StatusEffect IMMUNITY = register("immunity", new BreweryStatusEffect(StatusEffectType.BENEFICIAL, 0x8147b8));

	@Override
	public void onInitialize() {
		BucketUtil.registerFluidBucket(Fluids.WATER, Items.WATER_BUCKET);
		BucketUtil.registerFluidBucket(Fluids.LAVA, Items.LAVA_BUCKET);
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

	public static StatusEffect register(String name, StatusEffect effect) {
		Registry.register(Registry.STATUS_EFFECT, new Identifier(MODID, name), effect);
		return effect;
	}

}
