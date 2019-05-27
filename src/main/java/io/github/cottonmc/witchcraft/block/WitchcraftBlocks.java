package io.github.cottonmc.witchcraft.block;

import io.github.cottonmc.witchcraft.Witchcraft;
import io.github.cottonmc.witchcraft.block.entity.FaeLanternEntity;
import io.github.cottonmc.witchcraft.block.entity.IncenseBurnerEntity;
import io.github.cottonmc.witchcraft.block.entity.StoneCauldronEntity;
import io.github.cottonmc.witchcraft.effect.WitchcraftEffects;
import io.github.cottonmc.witchcraft.item.WitchcraftItems;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.FlowerBlock;
import net.minecraft.block.FlowerPotBlock;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.function.Supplier;

public class WitchcraftBlocks {
	public static Block.Settings flower() {
		return FabricBlockSettings.of(Material.PLANT).noCollision().breakInstantly().sounds(BlockSoundGroup.GRASS).build();
	}
	public static Block.Settings flowerPot() {
		return FabricBlockSettings.of(Material.PART).breakInstantly().build();
	}
	//plants and flowers, along with flowerpots
	public static final Block BORAGE = register("borage", new FlowerBlock(WitchcraftEffects.IMMUNITY, 7, flower()), WitchcraftItems.WITCHCRAFT_GROUP);
	public static final Block POTTED_BORAGE = register("potted_borage", new FlowerPotBlock(BORAGE, flowerPot()));
	public static final Block PINK_BORAGE = register("pink_borage", new FlowerBlock(WitchcraftEffects.IMMUNITY, 15, flower()), WitchcraftItems.WITCHCRAFT_GROUP);
	public static final Block POTTED_PINK_BORAGE = register("potted_pink_borage", new FlowerPotBlock(PINK_BORAGE, flowerPot()));

	public static final Block STONE_CAULDRON = register("stone_cauldron", new StoneCauldronBlock(), WitchcraftItems.WITCHCRAFT_GROUP);
	public static final BlockEntityType<StoneCauldronEntity> STONE_CAULDRON_BE = register("stone_cauldron", StoneCauldronEntity::new, STONE_CAULDRON);

	public static final Block INCENSE_BURNER = register("incense_burner", new IncenseBurnerBlock(), WitchcraftItems.WITCHCRAFT_GROUP);
	public static final BlockEntityType<IncenseBurnerEntity> INCENSE_BURNER_BE = register("incense_burner", IncenseBurnerEntity::new, INCENSE_BURNER);

	public static final Block FAE_LANTERN = register("fae_lantern", new FaeLanternBlock(), WitchcraftItems.WITCHCRAFT_GROUP);
	public static final BlockEntityType<FaeLanternEntity> FAE_LANTERN_BE = register("fae_lantern", FaeLanternEntity::new, FAE_LANTERN);

	public static void init() {
	}

	public static Block register(String name, Block block) {
		Registry.register(Registry.BLOCK, new Identifier(Witchcraft.MODID, name), block);
		return block;
	}

	public static Block register(String name, Block block, ItemGroup tab) {
		Registry.register(Registry.BLOCK, new Identifier(Witchcraft.MODID, name), block);
		BlockItem item = new BlockItem(block, new Item.Settings().itemGroup(tab));
		WitchcraftItems.register(name, item);
		return block;
	}

	public static BlockEntityType register(String name, Supplier<BlockEntity> be, Block...blocks) {
		return Registry.register(Registry.BLOCK_ENTITY, new Identifier(Witchcraft.MODID, name), BlockEntityType.Builder.create(be, blocks).build(null));
	}
}
