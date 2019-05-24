package io.github.cottonmc.witchcraft.block;

import io.github.cottonmc.witchcraft.item.WitchcraftItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LanternBlock;
import net.minecraft.block.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.IntegerProperty;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class FaeLanternBlock extends LanternBlock {
	public static final IntegerProperty FAIRIES = IntegerProperty.create("fairies", 0, 7);

	public FaeLanternBlock() {
		super(FabricBlockSettings.of(Material.METAL).strength(3.5F, 3.5F).sounds(BlockSoundGroup.LANTERN).ticksRandomly().build());
		this.setDefaultState(super.getDefaultState().with(FAIRIES, 0));
	}

	@Override
	public void onScheduledTick(BlockState state, World world, BlockPos pos, Random rand) {
		int fairies = state.get(FAIRIES);
		if (fairies < 7 && rand.nextInt(8) == 0) {
			world.setBlockState(pos, state.with(FAIRIES, fairies + 1));
			//TODO: add in custom sounds
			world.playSound(null, pos, SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.BLOCKS, 1f, 1f);
		}
	}

	@Override
	public boolean activate(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (world.isClient) return true;
		int fairies = state.get(FAIRIES);
		ItemStack stack = player.getStackInHand(hand);
		if (stack.isEmpty()) {
			TranslatableComponent message = new TranslatableComponent("msg.witchcraft.fae.none");
			switch(fairies) {
				case 1:
					message = new TranslatableComponent("msg.witchcraft.fae.one");
					break;
				case 2:
				case 3:
				case 4:
					message = new TranslatableComponent("msg.witchcraft.fae.few");
					break;
				case 5:
				case 6:
				case 7:
					message = new TranslatableComponent("msg.witchcraft.fae.many");
				default:
					break;
			}
			player.addChatMessage(message, true);
		}
		if (stack.getItem() == Items.GLASS_BOTTLE && fairies > 0) {
			if (!player.abilities.creativeMode) {
				ItemStack bottledFairy = new ItemStack(WitchcraftItems.BOTTLED_FAIRY);
				player.incrementStat(Stats.USE_CAULDRON);
				stack.subtractAmount(1);
				if (stack.isEmpty()) {
					player.setStackInHand(hand, bottledFairy);
				} else if (!player.inventory.insertStack(bottledFairy)) {
					player.dropItem(bottledFairy, false);
				}
			}
			world.setBlockState(pos, state.with(FAIRIES, fairies - 1));
		}
		return super.activate(state, world, pos, player, hand, hit);
	}

	@Override
	public int getLuminance(BlockState state) {
		int fairies = state.get(FAIRIES);
		return (2 * fairies) + 1;
	}

	@Override
	protected void appendProperties(StateFactory.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(FAIRIES);
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random rand) {
		super.randomDisplayTick(state, world, pos, rand);
	}
}
