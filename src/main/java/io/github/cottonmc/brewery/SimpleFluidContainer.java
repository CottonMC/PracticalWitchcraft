package io.github.cottonmc.brewery;

import io.github.prospector.silk.fluid.DropletValues;
import io.github.prospector.silk.fluid.FluidContainer;
import io.github.prospector.silk.fluid.FluidInstance;
import io.github.prospector.silk.util.ActionType;
import net.minecraft.fluid.Fluid;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.math.Direction;

import java.util.ArrayList;

public class SimpleFluidContainer implements FluidContainer {
	private FluidInstance EMPTY = new FluidInstance(); //TODO: As soon as possible, switch to FluidInstance.EMPTY
	protected DefaultedList<FluidInstance> storage;
	protected final ArrayList<Runnable> observers = new ArrayList<>();
	protected int tankSize = DropletValues.BLOCK;

	public SimpleFluidContainer(int numTanks, int tankSize) {
		storage = DefaultedList.create(numTanks, EMPTY);
		this.tankSize = tankSize;
	}

	protected boolean checkSlot(int slotIndex) {
		return slotIndex>=0 && slotIndex<storage.size();
	}

	protected void onChanged() {
		for(Runnable r : observers) r.run();
	}

	//implements Observable {
	public void listen(Runnable onChanged) {
		observers.add(onChanged);
	}
	//}

	public FluidInstance extract(int slotIndex, int amount, ActionType action) {
		//TODO: Access control

		if (!checkSlot(slotIndex)) return EMPTY;
		if (storage.get(slotIndex).getAmount()==0) return EMPTY;
		if (amount<=0) return EMPTY;

		int toTake = Math.min(storage.get(slotIndex).getAmount(), amount);

		FluidInstance result = storage.get(slotIndex).copy();
		result.setAmount(toTake);

		if (action==ActionType.PERFORM) {
			storage.get(slotIndex).subtractAmount(toTake);
			onChanged();
		}

		return result;
	}

	public FluidInstance insert(int slotIndex, FluidInstance fluidInstance, ActionType action) {
		//TODO: Access control

		if (!checkSlot(slotIndex)) return fluidInstance;
		if (fluidInstance.getAmount()==0) return EMPTY;

		FluidInstance existing = storage.get(slotIndex);
		if (existing.getAmount()==0) {
			FluidInstance result = EMPTY;
			FluidInstance accepted = fluidInstance.copy();

			int max = getMaxAmount(slotIndex);
			if (accepted.getAmount()>max) {
				result = accepted.copy();
				result.setAmount(accepted.getAmount()-max);
				accepted.setAmount(max);
			}

			if (action==ActionType.PERFORM) {
				storage.set(slotIndex, accepted);
				onChanged();
			}

			return result;
		} else {
			FluidInstance result = EMPTY;
			FluidInstance accepted = existing.copy();

			boolean canStack = fluidInstance.getFluid()==existing.getFluid();
			if (canStack) {
				accepted.setAmount(existing.getAmount()+fluidInstance.getAmount());
				int max = getMaxAmount(slotIndex);
				if (accepted.getAmount()>max) {
					result = accepted.copy();
					result.setAmount(accepted.getAmount()-max);
					accepted.setAmount(max);
				}

				if (action==ActionType.PERFORM) {
					storage.set(slotIndex, accepted);
					onChanged();
				}
			}
			return result;
		}
	}

	public int getMaxAmount(int slotIndex) {
		return tankSize;
	}

	public FluidInstance insert(FluidInstance fluidInstance, ActionType action) {
		//TODO: Is this the best way? It certainly might not be the fastest.

		FluidInstance remaining = fluidInstance.copy();
		for(int i=0; i<storage.size(); i++) {
			remaining = insert(i, remaining, action);
			if (remaining.getAmount()==0) return EMPTY;
		}

		return remaining;
	}

	public void fromTag(Tag tag) {
		if (tag instanceof CompoundTag) {
			//single-slot
			CompoundTag stackTag = (CompoundTag)tag;
			FluidInstance fluidInstance = new FluidInstance();
			fluidInstance.fromTag(stackTag);
			if (storage.size()>0) storage.set(0, fluidInstance);
		} else if (tag instanceof ListTag) {
			//multi-slot
			ListTag listTag = (ListTag)tag;
			if (listTag.getListType()!=10) return; //the list needs to contain compounds.
			for(int i=0; i<listTag.size(); i++) {
				if (storage.size()<=i) break; //Don't unpack instances past this component's capacity.
				CompoundTag fluidInstnaceTag = listTag.getCompoundTag(i);
				FluidInstance fluidInstance = new FluidInstance();
				fluidInstance.fromTag(fluidInstnaceTag);
				storage.set(i, (fluidInstance.getAmount()==0) ? EMPTY : fluidInstance);
			}
		}
	}

	public Tag toTag() {
		if (storage.size()==1) {
			return storage.get(0).toTag(new CompoundTag());
		} else {
			ListTag listTag = new ListTag();
			for(int i=0; i<storage.size(); i++) {
				listTag.add(storage.get(i).toTag(new CompoundTag()));
			}
			return listTag;
		}
	}

	//implements FluidContainer {
	@Override
	public boolean canExtractFluid(Direction dir, Fluid fluid, int amount) {
		for(int i=0; i<storage.size(); i++) {
			if (storage.get(i).getFluid()==fluid && storage.get(i).getAmount()>=amount) return true;
		}
		return false;
	}

	@Override
	public boolean canInsertFluid(Direction dir, Fluid fluid, int amount) {
		return insert(new FluidInstance(fluid, amount), ActionType.SIMULATE).getAmount()==0;
	}

	@Override
	public void extractFluid(Direction dir, Fluid fluid, int amount) {
		//Try to find a stack of the specified type
		for(int i=0; i<storage.size(); i++) {
			if (storage.get(i).getFluid()==fluid) {
				//Found it!
				extract(i, amount, ActionType.PERFORM);
			}
		}
	}

	@Override
	public FluidInstance[] getFluids(Direction dir) {
		return (FluidInstance[])storage.toArray();
	}

	@Override
	public int getMaxCapacity() {
		return tankSize;
	}

	@Override
	public void insertFluid(Direction dir, Fluid fluid, int amount) {
		insert(new FluidInstance(fluid, amount), ActionType.PERFORM);
	}

	@Override
	public void setFluid(Direction dir, FluidInstance fluidInstance) {
		insert(fluidInstance, ActionType.PERFORM);
	}
	//}

	public FluidInstance getFluid(int slot) {
		return storage.get(slot);
	}
}
