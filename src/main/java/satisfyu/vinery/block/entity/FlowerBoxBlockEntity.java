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
import satisfyu.vinery.registry.VineryBlockEntityTypes;
import satisfyu.vinery.registry.VineryMessages;

public class FlowerBoxBlockEntity extends BlockEntity {
	private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

	public final ItemStackHandler flowers = new ItemStackHandler(2) {
		@Override
		protected void onContentsChanged(int slot) {
			markUpdated();
		}
	};

	public FlowerBoxBlockEntity(BlockPos pPos, BlockState pBlockState) {
		super(VineryBlockEntityTypes.FLOWER_BOX_ENTITY.get(), pPos, pBlockState);
	}

	@Override
	public void onLoad() {
		super.onLoad();
		lazyItemHandler = LazyOptional.of(() -> flowers);
		markUpdated();
	}

	@Override
	public void invalidateCaps() {
		super.invalidateCaps();
		lazyItemHandler.invalidate();
	}

	@Override
	protected void saveAdditional(CompoundTag pTag) {
		pTag.put("inventory", flowers.serializeNBT());
		super.saveAdditional(pTag);
	}

	@Override
	public void load(CompoundTag pTag) {
		super.load(pTag);
		flowers.deserializeNBT(pTag.getCompound("inventory"));
	}

	public void addFlower(ItemStack stack, int index, Entity entity) {
		this.flowers.insertItem(index, stack, false);
		setChanged(level, getBlockPos(), getBlockState());
	}

	public void setFlowers(ItemStackHandler itemStackHandler) {
		for (int i = 0; i < itemStackHandler.getSlots(); i++) {
			flowers.setStackInSlot(i, itemStackHandler.getStackInSlot(i));
		}
	}

	public ItemStack removeFlower(int index) {
		ItemStack itemStack = this.flowers.getStackInSlot(index);
		this.flowers.setStackInSlot(index, ItemStack.EMPTY);
		return itemStack;
	}

	public boolean hasTwoFlowers() {
		boolean b = !(getONEFlower(flowers.getStackInSlot(0)).isEmpty() && getONEFlower(
				flowers.getStackInSlot(1)).isEmpty());
		return b;
	}

	public ItemStack[] removeAllFlowers() {
		ItemStack[] itemStacks = { getONEFlower(flowers.getStackInSlot(0)), getONEFlower(flowers.getStackInSlot(1)) };
		this.flowers.setStackInSlot(0, ItemStack.EMPTY);
		this.flowers.setStackInSlot(1, ItemStack.EMPTY);
		return itemStacks;
	}

	private ItemStack getONEFlower(ItemStack stack) {
		return new ItemStack(stack.getItem(), 1);
	}

	public ItemStack getFlower(int index) {
		return this.flowers.getStackInSlot(index);
	}

	public boolean isSlotEmpty(int index) {
		return index < this.flowers.getSlots() && this.flowers.getStackInSlot(index).isEmpty();
	}

	public void drops() {
		SimpleContainer simpleContainer = new SimpleContainer(flowers.getSlots());
		for (int i = 0; i < flowers.getSlots(); i++) {
			simpleContainer.setItem(i, flowers.getStackInSlot(i));
		}
		Containers.dropContents(level, getBlockPos(), simpleContainer);
	}

	public void markUpdated() {
		this.setChanged();
		if (!level.isClientSide) {
			VineryMessages.sendToClients(new FlowerBoxClientSyncS2CPacket(this.flowers, worldPosition));
		}
	}
}
