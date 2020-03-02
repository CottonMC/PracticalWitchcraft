package io.github.cottonmc.witchcraft.item;

import io.github.cottonmc.witchcraft.Witchcraft;
import io.github.cottonmc.witchcraft.component.DeityComponent;
import io.github.cottonmc.witchcraft.deity.Deity;
import io.github.cottonmc.witchcraft.deity.Pantheon;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.LiteralText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class FavorDebugItem extends Item {
	public FavorDebugItem() {
		super(new Item.Settings().maxCount(1));
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		ItemStack stack = player.getStackInHand(hand);
		if (world.isClient) return new TypedActionResult<>(ActionResult.PASS, stack);
		CompoundTag tag = stack.getOrCreateTag();
		if (!tag.contains("Deity", NbtType.STRING) || (!tag.contains("Amount", NbtType.FLOAT) && !tag.contains("Reset"))) return new TypedActionResult<>(ActionResult.PASS, stack);
		Identifier id = new Identifier(tag.getString("Deity"));
		Deity deity = Pantheon.DEITIES.get(id);
		DeityComponent component = Witchcraft.DEITY_COMPONENT.get(player);
		if (tag.contains("Reset")) {
			component.resetFavor(deity);
			player.addChatMessage(new LiteralText("Reset favor"), true);
			return new TypedActionResult<>(ActionResult.SUCCESS, stack);
		}
		float changeAmount = tag.getFloat("Amount");
		component.shiftFavor(deity, changeAmount);
		return new TypedActionResult<>(ActionResult.SUCCESS, stack);
	}
}
