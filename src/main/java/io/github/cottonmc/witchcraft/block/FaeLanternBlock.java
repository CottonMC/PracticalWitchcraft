package io.github.cottonmc.witchcraft.block;

import io.github.cottonmc.witchcraft.block.entity.FaeLanternEntity;
import io.github.cottonmc.witchcraft.item.WitchcraftItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
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
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.Random;

public class FaeLanternBlock extends LanternBlock implements BlockEntityProvider {
	public static final IntegerProperty PIXIES = IntegerProperty.create("pixies", 0, 7);

	public FaeLanternBlock() {
		super(FabricBlockSettings.of(Material.METAL).strength(3.5F, 3.5F).sounds(BlockSoundGroup.LANTERN).ticksRandomly().build());
		this.setDefaultState(super.getDefaultState().with(PIXIES, 0));
	}

	@Override
	public void onScheduledTick(BlockState state, World world, BlockPos pos, Random rand) {
		int pixies = state.get(PIXIES);
		if (pixies < 7 && rand.nextInt(8) == 0) {
			world.setBlockState(pos, state.with(PIXIES, pixies + 1));
			//TODO: add in custom sounds
			world.playSound(null, pos, SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.BLOCKS, 1f, 1f);
		}
	}

	@Override
	public boolean activate(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (world.isClient) return true;
		int pixies = state.get(PIXIES);
		ItemStack stack = player.getStackInHand(hand);
		if (stack.isEmpty()) {
			TranslatableComponent message = new TranslatableComponent("msg.witchcraft.fae.none");
			switch(pixies) {
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
		if (stack.getItem() == Items.GLASS_BOTTLE && pixies > 0) {
			if (!player.abilities.creativeMode) {
				ItemStack bottledPixie = new ItemStack(WitchcraftItems.BOTTLED_PIXIE);
				player.incrementStat(Stats.USE_CAULDRON);
				stack.subtractAmount(1);
				if (stack.isEmpty()) {
					player.setStackInHand(hand, bottledPixie);
				} else if (!player.inventory.insertStack(bottledPixie)) {
					player.dropItem(bottledPixie, false);
				}
			}
			world.setBlockState(pos, state.with(PIXIES, pixies - 1));
		}
		return super.activate(state, world, pos, player, hand, hit);
	}

	@Override
	public int getLuminance(BlockState state) {
		int pixies = state.get(PIXIES);
		return (2 * pixies) + 1;
	}

	@Override
	protected void appendProperties(StateFactory.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(PIXIES);
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random rand) {
		super.randomDisplayTick(state, world, pos, rand);
	}

	@Override
	public BlockEntity createBlockEntity(BlockView view) {
		return new FaeLanternEntity();
	}
}
