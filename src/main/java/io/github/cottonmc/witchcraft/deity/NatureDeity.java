package io.github.cottonmc.witchcraft.deity;

import io.github.cottonmc.cotton.registry.CommonTags;
import net.minecraft.ChatFormat;
import net.minecraft.block.PlantBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stat;
import net.minecraft.stat.Stats;
import net.minecraft.tag.ItemTags;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NatureDeity extends Deity {
	public NatureDeity() {
		super(DeityCharacter.FORGIVING, ChatFormat.GREEN);
	}

	@Override
	public void reload() {
		super.reload();
		Map<Stat, Float> favors = new HashMap<>();
		Map<Stat, Float> disfavors = new HashMap<>();
		List<Item> plants = new ArrayList<>(ItemTags.SAPLINGS.values());
		plants.addAll(CommonTags.PLANTABLES.values());
		for (Item item : plants) {
			Stat stat = Stats.USED.getOrCreateStat(item);
			favors.put(stat, 0.01f);
		}

		EntityType[] rare_passives = new EntityType[]{EntityType.TURTLE, EntityType.POLAR_BEAR, EntityType.FOX, EntityType.PANDA};
		for (EntityType type : rare_passives) {
			Stat stat = Stats.KILLED.getOrCreateStat(type);
			disfavors.put(stat, 1f);
		}
		this.addFavors(favors);
		this.addDisfavors(disfavors);
	}

	@Override
	public String getNameSubkey(PlayerEntity player) {
		return null;
	}

	@Override
	public void bless(PlayerEntity player, float totalFavor) {

	}

	@Override
	public void curse(PlayerEntity player, float totalFavor) {
		if (totalFavor <= -80) {
			PlayerInventory inv = player.inventory;
			int killedPlants = 0;
			for (int i = 0; i < inv.getInvSize(); i++) {
				if (killedPlants >= (totalFavor * -1) * 2) break;
				ItemStack stack = inv.getInvStack(i);
				if (stack.getItem() instanceof BlockItem) {
					BlockItem block = (BlockItem)stack.getItem();
					if (block.getBlock() instanceof PlantBlock) {
						ItemStack replace = new ItemStack(Items.DEAD_BUSH, stack.getAmount());
						killedPlants += stack.getAmount();
						inv.setInvStack(i, replace);
					}
				}
			}
			player.playSound(SoundEvents.ENTITY_WITHER_AMBIENT, SoundCategory.PLAYERS, 0.75f, 1f);
			player.addChatMessage(new TranslatableComponent("msg.witchcraft.nature.kill", getName(player).getFormattedText()), true);
		}
	}
}
