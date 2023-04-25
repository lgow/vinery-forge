package satisfyu.vinery.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import satisfyu.vinery.networking.ItemStackSyncS2CPacket;
import satisfyu.vinery.registry.VineryMessages;

import java.util.ArrayList;

public class StorageBlockEntity extends BlockEntity {
		public int size;

		public NonNullList<ItemStack> inventory;

	public StorageBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
		super(pType, pPos, pBlockState);
	}

	public StorageBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState, int size) {
		super(pType, pPos, pBlockState);
		this.size = size;
		this.inventory = NonNullList.withSize(this.size, ItemStack.EMPTY);
	}

	public ItemStack removeStack(int slot) {
		ItemStack stack = inventory.set(slot, ItemStack.EMPTY);
		setChanged();
		return stack;
	}

	public void setStack(int slot, ItemStack stack) {
		inventory.set(slot, stack);
		setChanged();
	}

	@Override
	public void setChanged() {
		VineryMessages.sendToClients(new ItemStackSyncS2CPacket(worldPosition, new ArrayList<>(inventory)));
		super.setChanged();
	}

	@Override
	public void load(CompoundTag nbt) {
		this.size = nbt.getInt("size");
		this.inventory = NonNullList.withSize(this.size, ItemStack.EMPTY);
		ContainerHelper.loadAllItems(nbt, this.inventory);
		super.load(nbt);
	}

	@Override
	protected void saveAdditional(CompoundTag nbt) {
		ContainerHelper.saveAllItems(nbt, this.inventory);
		nbt.putInt("size", this.size);
		super.saveAdditional(nbt);
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public CompoundTag getUpdateTag() {
		return this.saveWithoutMetadata();
	}

	public NonNullList<ItemStack> getInventory() {
		return inventory;
	}

	public void setInventory(NonNullList<ItemStack> inventory) {
		for (int i = 0; i < inventory.size(); i++) {
			this.inventory.set(i, inventory.get(i));
		}
	}
}
