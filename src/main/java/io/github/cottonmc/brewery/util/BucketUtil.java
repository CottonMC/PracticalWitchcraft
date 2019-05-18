package io.github.cottonmc.brewery.util;

import alexiil.mc.lib.attributes.fluid.volume.*;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class BucketUtil {
	public static Map<Fluid, Item> FLUID_BUCKETS = new HashMap<>();
	public static Map<Item, Fluid> BUCKET_FLUIDS = new HashMap<>();

	public static ItemStack fillBucketFromFluid(ItemStack bucket, Fluid fluid) {
		Item filled = FLUID_BUCKETS.get(fluid);
		bucket.subtractAmount(1);
		return new ItemStack(filled, 1);
	}

	public static void registerFluidBucket(Fluid fluid, Item bucket) {
		FLUID_BUCKETS.put(fluid, bucket);
		BUCKET_FLUIDS.put(bucket, fluid);
	}

	public static FluidVolume getBucketFluid(ItemStack stack) {
		Fluid fluid = BUCKET_FLUIDS.get(stack.getItem());
		FluidKey key = FluidKeys.get(fluid);
		return NormalFluidVolume.create(NormalFluidKey.builder(fluid, key.spriteId, key.name).build(), FluidVolume.BUCKET);
	}
}
