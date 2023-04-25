package satisfyu.vinery.client.gui.handler;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import satisfyu.vinery.block.entity.CookingPotEntity;
import satisfyu.vinery.client.gui.handler.slot.ExtendedSlot;
import satisfyu.vinery.client.gui.sidetip.RecipeGUIHandler;
import satisfyu.vinery.registry.VineryBlocks;
import satisfyu.vinery.registry.VineryRecipeTypes;
import satisfyu.vinery.registry.VineryScreenHandlerTypes;

public class CookingPotMenu extends RecipeGUIHandler {
	private final Container inventory;

	public CookingPotMenu(int syncId, Inventory inventory) {
		this(syncId, inventory, new SimpleContainer(8), new SimpleContainerData(2));
	}

	public CookingPotMenu(int syncId, Inventory inventory, Container container, ContainerData containerData) {
		super(VineryScreenHandlerTypes.COOKING_POT_SCREEN_HANDLER.get(), syncId, inventory, container, containerData,
				7);
		this.inventory = inventory;
		buildBlockEntityContainer(container);
		buildPlayerContainer(inventory);
	}

	private void buildBlockEntityContainer(Container container) {
		this.addSlot(new ExtendedSlot(container, 6, 95, 55,
				stack -> stack.is(Item.byBlock(VineryBlocks.CHERRY_JAR.get())) || stack.is(Items.BOWL)));
		for (int x = 0; x < 2; x++) {
			for (int slot = 0; slot < 3; slot++) {
				this.addSlot(new Slot(container, slot + x + (x * 2), 30 + (slot * 18), 17 + (x * 18)));
			}
		}
		this.addSlot(new Slot(container, 7, 124, 28) {
			@Override
			public boolean mayPlace(ItemStack pStack) {
				return false;
			}
		});
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

	public boolean isBeingBurned() {
		return containerData.get(1) != 0;
	}

	public int getScaledProgress(int arrowWidth) {
		final int progress = this.containerData.get(0);
		final int totalProgress = CookingPotEntity.MAX_COOKING_TIME;
		return progress == 0 ? 0 : (progress * arrowWidth / totalProgress + 1);
	}

	private boolean isIngredient(ItemStack stack) {
		return this.level.getRecipeManager().getAllRecipesFor(VineryRecipeTypes.COOKING_POT_RECIPE_TYPE.get()).stream()
				.anyMatch(recipe -> recipe.getIngredients().stream().anyMatch(x -> x.test(stack)));
	}
}
