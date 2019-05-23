package io.github.cottonmc.witchcraft;

import io.github.cottonmc.witchcraft.block.WitchcraftBlocks;
import io.github.cottonmc.witchcraft.deity.DeityReloader;
import io.github.cottonmc.witchcraft.deity.Pantheon;
import io.github.cottonmc.witchcraft.effect.WitchcraftEffects;
import io.github.cottonmc.witchcraft.item.WitchcraftItems;
import io.github.cottonmc.skillcheck.api.traits.ClassManager;
import io.github.cottonmc.witchcraft.recipe.WitchcraftRecipes;
import io.github.cottonmc.witchcraft.util.WitchcraftNetworking;
import io.github.cottonmc.witchcraft.util.BucketUtil;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Items;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

public class Witchcraft implements ModInitializer {
	public static final String MODID = "witchcraft";

	public static final Identifier WITCH = ClassManager.registerClass(new Identifier(MODID, "witch"), 5);

	@Override
	public void onInitialize() {
		WitchcraftItems.init();
		WitchcraftBlocks.init();
		WitchcraftRecipes.init();
		WitchcraftEffects.init();
		WitchcraftNetworking.init();
		Pantheon.init();
		BucketUtil.registerFluidBucket(Fluids.WATER, Items.WATER_BUCKET);
		BucketUtil.registerFluidBucket(Fluids.LAVA, Items.LAVA_BUCKET);

		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new DeityReloader());
	}

}
