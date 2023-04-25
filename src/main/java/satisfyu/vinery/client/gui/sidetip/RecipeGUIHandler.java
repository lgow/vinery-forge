package satisfyu.vinery.client.gui.sidetip;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class RecipeGUIHandler extends AbstractContainerMenu {
	public final Level level;

	public final Container container;

	public final ContainerData containerData;

	public final int inputSlots;

	public RecipeGUIHandler(@Nullable MenuType<?> pMenuType, int pContainerId, Inventory inventory, Container container, ContainerData containerData, int inputSlots) {
		super(pMenuType, pContainerId);
		this.level = inventory.player.level;
		this.container = container;
		this.containerData = containerData;
		this.inputSlots = inputSlots;
		addDataSlots(this.containerData);
	}

	@Override
	public ItemStack quickMoveStack(Player player, int invSlot) {
		ItemStack newStack = ItemStack.EMPTY;
		Slot slot = this.slots.get(invSlot);
		if (slot != null && slot.hasItem()) {
			ItemStack originalStack = slot.getItem();
			newStack = originalStack.copy();
			if (invSlot < this.container.getContainerSize()) {
				if (!this.moveItemStackTo(originalStack, this.container.getContainerSize(), this.slots.size(), true)) {
					return ItemStack.EMPTY;
				}
			}
			else if (!this.moveItemStackTo(originalStack, 0, this.container.getContainerSize(), false)) {
				return ItemStack.EMPTY;
			}
			if (originalStack.isEmpty()) {
				slot.set(ItemStack.EMPTY);
			}
			else {
				slot.setChanged();
			}
		}
		return newStack;
	}

	@Override
	public boolean stillValid(Player pPlayer) {
		return this.container.stillValid(pPlayer);
	}
}
