package io.github.cottonmc.witchcraft.component;

import alexiil.mc.lib.attributes.fluid.amount.FluidAmount;
import alexiil.mc.lib.attributes.fluid.volume.FluidKey;
import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundTag;

public class WitchcraftFluidVolume extends FluidVolume {
	public WitchcraftFluidVolume(FluidKey key, FluidAmount amount) {
		super(key, amount);
	}

	public WitchcraftFluidVolume(FluidKey key, CompoundTag tag) {
		super(key, tag);
	}

	public WitchcraftFluidVolume(FluidKey key, JsonObject json) {
		super(key, json);
	}
}
