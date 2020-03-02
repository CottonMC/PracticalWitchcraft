package io.github.cottonmc.witchcraft.impl;

import io.github.cottonmc.witchcraft.Witchcraft;
import io.github.cottonmc.witchcraft.component.DeityComponent;
import io.github.cottonmc.witchcraft.deity.Deity;
import io.github.cottonmc.witchcraft.deity.Pantheon;
import nerdhub.cardinal.components.api.ComponentType;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class DeityComponentImpl implements DeityComponent {
	private Entity entity;
	private Deity devotion = null;
	private Map<Deity, Float> favors = new HashMap<>();

	public DeityComponentImpl(Entity entity) {
		this.entity = entity;
	}

	@Override
	public Deity getDevotion() {
		return devotion;
	}

	@Override
	public void setDevotion(Deity deity) {
		devotion = deity;
		sync();
	}

	@Override
	public void forsakeDeity() {
		devotion = null; //TODO: default value?
		sync();
	}

	@Override
	public void shiftFavor(Deity deity, float amount, boolean passive) {
		if (amount == 0) return;
		float favor;
		int oldFavor;
		int newFavor;
		if (!favors.containsKey(deity)) {
			oldFavor = 0;
			newFavor = (int)amount;
			favor = amount;
		} else {
			favor = favors.get(deity);
			oldFavor = (int)favor;
			favor += amount;
			if (favor > 100) favor = 100;
			if (favor < -100) favor = -100;
			newFavor = (int) (favor);
		}
		favors.put(deity, favor);
		sync();
		if (passive) return;
		boolean rollover = oldFavor != newFavor;
		if (getEntity() instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity)getEntity();
			Text message = deity.getFavorMessage(player, favor, amount, rollover);
			if (message != null) player.addChatMessage(message, true);
			deity.affectPlayer(player, favor, amount, rollover);
		}
	}

	@Override
	public void resetFavor(Deity deity) {
		favors.put(deity, 0f);
		sync();
	}

	@Override
	public void setFavor(Deity deity, float amount, boolean passive) {
		float favor = amount;
		if (favor > 100) favor = 100;
		if (favor < -100) favor = 100;
		favors.put(deity, favor);
		sync();
		if (passive) return;
		boolean rollover = Math.abs(amount) >= 1;
		if (getEntity() instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity)getEntity();
			Text message = deity.getFavorMessage(player, favor, amount, rollover);
			if (message != null) player.addChatMessage(message, true);
			deity.affectPlayer(player, favor, amount, rollover);
		}
	}

	@Override
	public float getFavor(Deity deity) {
		return favors.getOrDefault(deity, 0f);
	}

	@Override
	public ComponentType<?> getComponentType() {
		return Witchcraft.DEITY_COMPONENT;
	}

	@Override
	public Entity getEntity() {
		return entity;
	}

	@Override
	public void fromTag(CompoundTag tag) {
		favors.clear();
		if (tag.contains("Devotion", NbtType.STRING)) {
			devotion = Pantheon.DEITIES.get(new Identifier(tag.getString("Devotion")));
		} else {
			devotion = null;
		}
		CompoundTag favorTag = tag.getCompound("Favors");
		for (String key : favorTag.getKeys()) {
			Deity deity = Pantheon.DEITIES.get(new Identifier(key));
			favors.put(deity, favorTag.getFloat(key));
		}
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		if (devotion != null) {
			tag.putString("Devotion", Pantheon.DEITIES.getId(devotion).toString());
		}
		CompoundTag favorTag = new CompoundTag();
		for (Deity deity : favors.keySet()) {
			favorTag.putFloat(Pantheon.DEITIES.getId(devotion).toString(), favors.get(deity));
		}
		tag.put("Favors", favorTag);
		return tag;
	}
}
