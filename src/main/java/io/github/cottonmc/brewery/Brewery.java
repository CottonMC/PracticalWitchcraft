package io.github.cottonmc.brewery;

import io.github.cottonmc.brewery.block.BreweryBlocks;
import io.github.cottonmc.brewery.effect.BreweryEffects;
import io.github.cottonmc.brewery.item.BreweryItems;
//import io.github.cottonmc.skillcheck.api.traits.ClassManager;
import io.github.cottonmc.brewery.util.BreweryNetworking;
import io.github.cottonmc.brewery.util.BucketUtil;
import net.fabricmc.api.ModInitializer;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Items;

public class Brewery implements ModInitializer {
	public static final String MODID = "brewery";

//	public static final Identifier WITCH = ClassManager.registerClass(new Identifier(MODID, "witch"), 5);

	@Override
	public void onInitialize() {
		BreweryItems.init();
		BreweryBlocks.init();
		BreweryEffects.init();
		BreweryNetworking.init();
		BucketUtil.registerFluidBucket(Fluids.WATER, Items.WATER_BUCKET);
		BucketUtil.registerFluidBucket(Fluids.LAVA, Items.LAVA_BUCKET);
	}

}
