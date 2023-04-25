package satisfyu.vinery.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import satisfyu.vinery.networking.FlowerBoxClientSyncS2CPacket;
import satisfyu.vinery.networking.TallFlowerPotClientSyncS2CPacket;
import satisfyu.vinery.registry.VineryBlockEntityTypes;
import satisfyu.vinery.registry.VineryMessages;

public class TallFlowerPotBlockEntity extends BlockEntity {
	private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

	public final ItemStackHandler flower = new ItemStackHandler(1) {
		@Override
		protected void onContentsChanged(int slot) {
			markUpdated();
		}
	};

	public TallFlowerPotBlockEntity(BlockPos pPos, BlockState pBlockState) {
		super(VineryBlockEntityTypes.TALL_FLOWER_POT_ENTITY.get(), pPos, pBlockState);
	}

	@Override
	public void onLoad() {
		super.onLoad();
		lazyItemHandler = LazyOptional.of(() -> flower);
		markUpdated();
	}

	@Override
	public void invalidateCaps() {
		super.invalidateCaps();
		lazyItemHandler.invalidate();
	}

	@Override
	protected void saveAdditional(CompoundTag pTag) {
		pTag.put("inventory", flower.serializeNBT());
		super.saveAdditional(pTag);
	}

	@Override
	public void load(CompoundTag pTag) {
		super.load(pTag);
		flower.deserializeNBT(pTag.getCompound("inventory"));
	}

	public void addFlower(ItemStack stack) {
		this.flower.insertItem(0, stack, false);
		setChanged(level, getBlockPos(), getBlockState());
	}

	public void setFlower(ItemStackHandler itemStackHandler) {
		for (int i = 0; i < itemStackHandler.getSlots(); i++) {
			flower.setStackInSlot(i, itemStackHandler.getStackInSlot(i));
		}
	}

	public ItemStack removeFlower() {
		ItemStack itemStack = this.flower.getStackInSlot(0);
		this.flower.setStackInSlot(0, ItemStack.EMPTY);
		return itemStack;
	}

	public boolean hasFlower() {
		return this.getFlower().isEmpty();
	}

	public ItemStack getFlower() {
		return this.flower.getStackInSlot(0);
	}

	public void drops() {
		SimpleContainer simpleContainer = new SimpleContainer(flower.getSlots());
		for (int i = 0; i < flower.getSlots(); i++) {
			simpleContainer.setItem(i, flower.getStackInSlot(i));
		}
		Containers.dropContents(level, getBlockPos(), simpleContainer);
	}

	public void markUpdated() {
		this.setChanged();
		if (!level.isClientSide) {
			VineryMessages.sendToClients(new TallFlowerPotClientSyncS2CPacket(this.flower, worldPosition));
		}
	}
}