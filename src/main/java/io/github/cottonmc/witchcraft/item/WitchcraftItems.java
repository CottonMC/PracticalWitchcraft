package io.github.cottonmc.witchcraft.item;

import io.github.cottonmc.witchcraft.Witchcraft;
import io.github.cottonmc.witchcraft.util.WitchcraftNetworking;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.client.network.packet.EntityPotionEffectS2CPacket;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class WitchcraftItems {

	public static Item BROOMSTICK;

	public static final ItemGroup BREWERY_GROUP = FabricItemGroupBuilder.build(new Identifier(Witchcraft.MODID, "main_group"), () -> new ItemStack(BROOMSTICK));

	public static Item PURGING_INCENSE = register("purging_incense", new IncenseStickItem((player) -> {
		if (player.hasStatusEffect(StatusEffects.BAD_OMEN)) {
			StatusEffectInstance inst = player.getStatusEffect(StatusEffects.BAD_OMEN);
			int level = inst.getAmplifier();
			int time = inst.getDuration();
			player.removePotionEffect(StatusEffects.BAD_OMEN);
			WitchcraftNetworking.removeEffect((ServerPlayerEntity) player, StatusEffects.BAD_OMEN);
			if (level != 0) {
				StatusEffectInstance newInst = new StatusEffectInstance(StatusEffects.BAD_OMEN, time, level - 1, false, false, true);
				player.addPotionEffect(newInst);
				((ServerPlayerEntity) player).networkHandler.sendPacket(new EntityPotionEffectS2CPacket(player.getEntityId(), newInst));
			}
		}
	}));

	public static void init() {
		BROOMSTICK = register("broomstick", new Item(new Item.Settings().itemGroup(BREWERY_GROUP).stackSize(1)));
	}

	public static Item register(String name, Item item) {
		Registry.register(Registry.ITEM, new Identifier(Witchcraft.MODID, name), item);
		return item;
	}
}
