package satisfyu.vinery.client.gui.handler;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import satisfyu.vinery.client.gui.handler.slot.ExtendedSlot;
import satisfyu.vinery.client.gui.handler.slot.StoveOutputSlot;
import satisfyu.vinery.client.gui.sidetip.RecipeGUIHandler;
import satisfyu.vinery.registry.VineryBlocks;
import satisfyu.vinery.registry.VineryRecipeTypes;
import satisfyu.vinery.registry.VineryScreenHandlerTypes;

public class AgingBarrelMenu extends RecipeGUIHandler {
	private final Container inventory;

	public AgingBarrelMenu(int syncId, Inventory inventory) {
		this(syncId, inventory, new SimpleContainer(6), new SimpleContainerData(2));
	}

	public AgingBarrelMenu(int syncId, Inventory inventory, Container container, ContainerData data) {
		super(VineryScreenHandlerTypes.AGING_BARREL_SCREEN_HANDLER.get(), syncId, inventory, container, data, 5);
		this.inventory = inventory;
		buildBlockEntityContainer(inventory, container);
		buildPlayerContainer(inventory);
	}

	private void buildBlockEntityContainer(Inventory playerInventory, Container inventory) {
		// Wine input
		this.addSlot(new ExtendedSlot(inventory, 0, 79, 51,
				stack -> stack.is(Item.byBlock(VineryBlocks.WINE_BOTTLE.get()))));
		// Inputs
		this.addSlot(new ExtendedSlot(inventory, 2, 33, 26, this::isIngredient));
		this.addSlot(new ExtendedSlot(inventory, 3, 51, 26, this::isIngredient));
		this.addSlot(new ExtendedSlot(inventory, 4, 33, 44, this::isIngredient));
		this.addSlot(new ExtendedSlot(inventory, 5, 51, 44, this::isIngredient));
		// Output
		this.addSlot(new StoveOutputSlot(playerInventory.player, inventory, 1, 128, 35));
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
		return this.level.getRecipeManager().getAllRecipesFor(VineryRecipeTypes.FERMENTATION_BARREL_RECIPE_TYPE.get())
				.stream().anyMatch(recipe -> recipe.getIngredients().stream().anyMatch(x -> x.test(stack)));
	}

	public int getScaledProgress(int arrowWidth) {
		final int progress = this.containerData.get(0);
		final int totalProgress = this.containerData.get(1);
		if (progress == 0) {
			return 0;
		}
		return progress * arrowWidth / totalProgress + 1;
	}
}
