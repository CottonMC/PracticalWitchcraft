package io.github.cottonmc.witchcraft;

import io.github.cottonmc.witchcraft.block.WitchcraftBlocks;
import io.github.cottonmc.witchcraft.component.DeityComponent;
import io.github.cottonmc.witchcraft.deity.DeityReloader;
import io.github.cottonmc.witchcraft.deity.Pantheon;
import io.github.cottonmc.witchcraft.effect.WitchcraftEffects;
import io.github.cottonmc.witchcraft.impl.DeityComponentImpl;
import io.github.cottonmc.witchcraft.item.WitchcraftItems;
import io.github.cottonmc.witchcraft.recipe.WitchcraftRecipes;
import io.github.cottonmc.witchcraft.util.WitchcraftNetworking;
import nerdhub.cardinal.components.api.ComponentRegistry;
import nerdhub.cardinal.components.api.ComponentType;
import nerdhub.cardinal.components.api.event.EntityComponentCallback;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.resource.ResourceType;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public class Witchcraft implements ModInitializer {
	public static final String MODID = "witchcraft";

	public static final Tag<EntityType<?>> RARE_PASSIVES = TagRegistry.entityType(new Identifier(MODID, "rare_passives"));

	public static final ComponentType<DeityComponent> DEITY_COMPONENT = ComponentRegistry.INSTANCE.registerIfAbsent(new Identifier(MODID, "deity_component"), DeityComponent.class);


	@Override
	public void onInitialize() {
		EntityComponentCallback.event(PlayerEntity.class).register((entity, container) -> container.put(DEITY_COMPONENT, new DeityComponentImpl(entity)));
		WitchcraftItems.init();
		WitchcraftBlocks.init();
		WitchcraftRecipes.init();
		WitchcraftEffects.init();
		WitchcraftNetworking.init();
		Pantheon.init();

		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new DeityReloader());
	}

}
