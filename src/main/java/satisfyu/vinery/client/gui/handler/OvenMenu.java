package satisfyu.vinery.client.gui.handler;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import satisfyu.vinery.client.gui.handler.slot.ExtendedSlot;
import satisfyu.vinery.client.gui.handler.slot.StoveOutputSlot;
import satisfyu.vinery.client.gui.sidetip.RecipeGUIHandler;
import satisfyu.vinery.registry.VineryRecipeTypes;
import satisfyu.vinery.registry.VineryScreenHandlerTypes;

public class OvenMenu extends RecipeGUIHandler {
	public static final int FUEL_SLOT = 3;

	public static final int OUTPUT_SLOT = 4;

	private final Container inventory;

	public OvenMenu(int syncId, Inventory playerInventory) {
		this(syncId, playerInventory, new SimpleContainer(5), new SimpleContainerData(4));
	}

	public OvenMenu(int syncId, Inventory playerInventory, Container inventory, ContainerData delegate) {
		super(VineryScreenHandlerTypes.OVEN_SCREEN_HANDLER.get(), syncId, playerInventory, inventory, delegate, 4);
		this.inventory = inventory;
		buildBlockEntityContainer(playerInventory, inventory);
		buildPlayerContainer(playerInventory);
	}

	private static boolean isFuel(ItemStack stack) {
		return AbstractFurnaceBlockEntity.isFuel(stack);
	}

	private void buildBlockEntityContainer(Inventory playerInventory, Container inventory) {
		this.addSlot(new ExtendedSlot(inventory, 0, 29, 18, this::isIngredient));
		this.addSlot(new ExtendedSlot(inventory, 1, 47, 18, this::isIngredient));
		this.addSlot(new ExtendedSlot(inventory, 2, 65, 18, this::isIngredient));
		this.addSlot(new ExtendedSlot(inventory, 3, 42, 48, OvenMenu::isFuel));
		this.addSlot(new StoveOutputSlot(playerInventory.player, inventory, 4, 126, 42));
	}

	private void buildPlayerContainer(Inventory playerInventory) {
		int i;
		for (i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}
		for (i = 0; i < 9; ++i) {
			this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
		}
	}

	private boolean isIngredient(ItemStack stack) {
		return this.level.getRecipeManager().getAllRecipesFor(VineryRecipeTypes.WOOD_FIRED_OVEN_RECIPE_TYPE.get())
				.stream().anyMatch(recipe -> recipe.getIngredients().stream().anyMatch(x -> x.test(stack)));
	}

	public int getScaledProgress(int arrowWidth) {
		final int progress = this.containerData.get(2);
		final int totalProgress = this.containerData.get(3);
		if (progress == 0) {
			return 0;
		}
		return progress * arrowWidth / totalProgress + 1;
	}

	public boolean isBeingBurned() {
		return containerData.get(1) != 0;
	}
	//    @Override
	//    public ItemStack quickMoveStack(Player player, int invSlot) {
	//        ItemStack newStack = ItemStack.EMPTY;
	//        Slot slot = this.slots.get(invSlot);
	//        if (slot != null && slot.hasItem()) {
	//            ItemStack originalStack = slot.getItem();
	//            newStack = originalStack.copy();
	//            if (invSlot < this.inventory.getContainerSize()) {
	//                if (!this.moveItemStackTo(originalStack, this.inventory.getContainerSize(), this.slots.size(), true)) {
	//                    return ItemStack.EMPTY;
	//                }
	//            } else if (!this.moveItemStackTo(originalStack, 0, this.inventory.getContainerSize(), false)) {
	//                return ItemStack.EMPTY;
	//            }
	//
	//            if (originalStack.isEmpty()) {
	//                slot.set(ItemStack.EMPTY);
	//            } else {
	//                slot.setChanged();
	//            }
	//        }
	//
	//        return newStack;
	//    }
}
