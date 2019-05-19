package io.github.cottonmc.witchcraft.util;

import io.github.cottonmc.witchcraft.Witchcraft;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.network.packet.CustomPayloadS2CPacket;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.registry.Registry;

public class WitchcraftNetworking {
	public static final Identifier REMOVE_EFFECT = new Identifier(Witchcraft.MODID, "remove_effect");

	public static void init() {
		if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
			ClientSidePacketRegistry.INSTANCE.register(REMOVE_EFFECT, ((packetContext, packetByteBuf) -> {
				Identifier effectId = packetByteBuf.readIdentifier();
				StatusEffect effect = Registry.STATUS_EFFECT.get(effectId);
				packetContext.getPlayer().removePotionEffect(effect);
			}));
		}
	}

	@Environment(EnvType.CLIENT)
	public static void removeEffect(ServerPlayerEntity player, StatusEffect effect) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeIdentifier(Registry.STATUS_EFFECT.getId(effect));
		player.networkHandler.sendPacket(new CustomPayloadS2CPacket(REMOVE_EFFECT, buf));
	}
}
