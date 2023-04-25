package satisfyu.vinery.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import satisfyu.vinery.block.WoodFiredOvenBlock;
import satisfyu.vinery.client.gui.handler.OvenMenu;
import satisfyu.vinery.recipe.OvenRecipe;
import satisfyu.vinery.registry.VineryBlockEntityTypes;
import satisfyu.vinery.registry.VineryRecipeTypes;

public class WoodFiredOvenBlockEntity extends BlockEntity implements Container, MenuProvider {
	public static final int TOTAL_COOKING_TIME = 240;

	protected static final int FUEL_SLOT = OvenMenu.FUEL_SLOT;

	protected static final int[] INGREDIENT_SLOTS = { 0, 1, 2 };

	protected static final int OUTPUT_SLOT = OvenMenu.OUTPUT_SLOT;

	protected int burnTime;

	protected int burnTimeTotal;

	protected int cookTime;

	protected int cookTimeTotal;

	private final ContainerData propertyDelegate = new ContainerData() {
		@Override
		public int get(int index) {
			return switch (index) {
				case 0 -> WoodFiredOvenBlockEntity.this.burnTime;
				case 1 -> WoodFiredOvenBlockEntity.this.burnTimeTotal;
				case 2 -> WoodFiredOvenBlockEntity.this.cookTime;
				case 3 -> WoodFiredOvenBlockEntity.this.cookTimeTotal;
				default -> 0;
			};
		}

		@Override
		public void set(int index, int value) {
			switch (index) {
				case 0 -> WoodFiredOvenBlockEntity.this.burnTime = value;
				case 1 -> WoodFiredOvenBlockEntity.this.burnTimeTotal = value;
				case 2 -> WoodFiredOvenBlockEntity.this.cookTime = value;
				case 3 -> WoodFiredOvenBlockEntity.this.cookTimeTotal = value;
			}
		}

		@Override
		public int getCount() {
			return 4;
		}
	};

	protected float experience;

	private NonNullList<ItemStack> inventory;

	public WoodFiredOvenBlockEntity(BlockPos pos, BlockState state) {
		super(VineryBlockEntityTypes.WOOD_FIRED_OVEN_BLOCK_ENTITY.get(), pos, state);
		this.inventory = NonNullList.withSize(5, ItemStack.EMPTY);
	}

	public static void tick(Level world, BlockPos pos, BlockState state, WoodFiredOvenBlockEntity blockEntity) {
		if (world.isClientSide) {
			return;
		}
		boolean initialBurningState = blockEntity.isBurning();
		boolean dirty = false;
		if (initialBurningState) {
			--blockEntity.burnTime;
		}
		final OvenRecipe recipe = world.getRecipeManager().getRecipeFor(
				VineryRecipeTypes.WOOD_FIRED_OVEN_RECIPE_TYPE.get(), blockEntity, world).orElse(null);
		if (!initialBurningState && blockEntity.canCraft(recipe)) {
			blockEntity.burnTime = blockEntity.burnTimeTotal = blockEntity.getTotalBurnTime(
					blockEntity.getItem(FUEL_SLOT));
			if (blockEntity.burnTime > 0) {
				dirty = true;
				final ItemStack fuelStack = blockEntity.getItem(FUEL_SLOT);
				if (fuelStack.getItem().hasCraftingRemainingItem()) {
					blockEntity.setItem(FUEL_SLOT, new ItemStack(fuelStack.getItem().getCraftingRemainingItem()));
				}
				else if (fuelStack.getCount() > 1) {
					blockEntity.removeItem(FUEL_SLOT, 1);
				}
				else if (fuelStack.getCount() == 1) {
					blockEntity.setItem(FUEL_SLOT, ItemStack.EMPTY);
				}
			}
		}
		if (blockEntity.isBurning() && blockEntity.canCraft(recipe)) {
			++blockEntity.cookTime;
			if (blockEntity.cookTime == blockEntity.cookTimeTotal) {
				blockEntity.cookTime = 0;
				blockEntity.craft(recipe);
				dirty = true;
			}
		}
		else if (!blockEntity.canCraft(recipe)) {
			blockEntity.cookTime = 0;
		}
		if (initialBurningState != blockEntity.isBurning()) {
			if (state.getValue(WoodFiredOvenBlock.LIT) != (blockEntity.burnTime > 0)) {
				world.setBlock(pos, state.setValue(WoodFiredOvenBlock.LIT, blockEntity.burnTime > 0), Block.UPDATE_ALL);
				dirty = true;
			}
		}
		if (dirty) {
			blockEntity.setChanged();
		}
	}

	public void dropExperience(ServerLevel world, Vec3 pos) {
		ExperienceOrb.award(world, pos, (int) experience);
	}

	@Override
	public void load(CompoundTag nbt) {
		super.load(nbt);
		this.inventory = NonNullList.withSize(5, ItemStack.EMPTY);
		ContainerHelper.loadAllItems(nbt, this.inventory);
		this.burnTime = nbt.getShort("BurnTime");
		this.cookTime = nbt.getShort("CookTime");
		this.cookTimeTotal = nbt.getShort("CookTimeTotal");
		this.burnTimeTotal = this.getTotalBurnTime(this.getItem(FUEL_SLOT));
		this.experience = nbt.getFloat("Experience");
	}

	@Override
	protected void saveAdditional(CompoundTag nbt) {
		super.saveAdditional(nbt);
		nbt.putShort("BurnTime", (short) this.burnTime);
		nbt.putShort("CookTime", (short) this.cookTime);
		nbt.putShort("CookTimeTotal", (short) this.cookTimeTotal);
		nbt.putFloat("Experience", this.experience);
		ContainerHelper.saveAllItems(nbt, this.inventory);
	}

	protected boolean isBurning() {
		return this.burnTime > 0;
	}

	protected boolean canCraft(OvenRecipe recipe) {
		if (recipe == null || recipe.getResultItem().isEmpty()) {
			return false;
		}
		else if (this.getItem(FUEL_SLOT).isEmpty()) {
			return false;
		}
		else if (this.getItem(OUTPUT_SLOT).isEmpty()) {
			return true;
		}
		else {
			final ItemStack recipeOutput = recipe.getResultItem();
			final ItemStack outputSlotStack = this.getItem(OUTPUT_SLOT);
			final int outputSlotCount = outputSlotStack.getCount();
			if (!outputSlotStack.sameItem(recipeOutput)) {
				return false;
			}
			else if (outputSlotCount < this.getMaxStackSize() && outputSlotCount < outputSlotStack.getMaxStackSize()) {
				return true;
			}
			else {
				return outputSlotCount < recipeOutput.getMaxStackSize();
			}
		}
	}

	protected void craft(OvenRecipe recipe) {
		if (recipe == null || !canCraft(recipe)) {
			return;
		}
		final ItemStack recipeOutput = recipe.getResultItem();
		final ItemStack outputSlotStack = this.getItem(OUTPUT_SLOT);
		if (outputSlotStack.isEmpty()) {
			setItem(OUTPUT_SLOT, recipeOutput.copy());
		}
		else if (outputSlotStack.is(recipeOutput.getItem())) {
			outputSlotStack.grow(recipeOutput.getCount());
		}
		final NonNullList<Ingredient> ingredients = recipe.getIngredients();
		boolean[] slotUsed = new boolean[3];
		for (int i = 0; i < recipe.getIngredients().size(); i++) {
			Ingredient ingredient = ingredients.get(i);
			// Looks for the best slot to take it from
			ItemStack bestSlot = getItem(i);
			if (ingredient.test(bestSlot) && !slotUsed[i]) {
				slotUsed[i] = true;
				ItemStack remainderStack = getRemainderItem(bestSlot);
				bestSlot.shrink(1);
				if (!remainderStack.isEmpty()) {
					setItem(i, remainderStack);
				}
			}
			else {
				// check all slots in search of the ingredient
				for (int j = 0; j < 3; j++) {
					ItemStack stack = getItem(j);
					if (ingredient.test(stack) && !slotUsed[j]) {
						slotUsed[j] = true;
						ItemStack remainderStack = getRemainderItem(stack);
						stack.shrink(1);
						if (!remainderStack.isEmpty()) {
							setItem(j, remainderStack);
						}
					}
				}
			}
		}
		this.experience += recipe.getExperience();
	}

	protected int getTotalBurnTime(ItemStack fuel) {
		if (fuel.isEmpty()) {
			return 0;
		}
		else {
			final Item item = fuel.getItem();
			return AbstractFurnaceBlockEntity.getFuel().getOrDefault(item, 0);
		}
	}

	private ItemStack getRemainderItem(ItemStack stack) {
		if (stack.getItem().hasCraftingRemainingItem()) {
			return new ItemStack(stack.getItem().getCraftingRemainingItem());
		}
		return ItemStack.EMPTY;
	}

	@Override
	public int getContainerSize() {
		return inventory.size();
	}

	@Override
	public boolean isEmpty() {
		return inventory.stream().allMatch(ItemStack::isEmpty);
	}

	@Override
	public ItemStack getItem(int slot) {
		return this.inventory.get(slot);
	}

	@Override
	public ItemStack removeItem(int slot, int amount) {
		return ContainerHelper.removeItem(this.inventory, slot, amount);
	}

	@Override
	public ItemStack removeItemNoUpdate(int slot) {
		return ContainerHelper.takeItem(this.inventory, slot);
	}

	@Override
	public void setItem(int slot, ItemStack stack) {
		final ItemStack stackInSlot = this.inventory.get(slot);
		boolean dirty = !stack.isEmpty() && stack.sameItem(stackInSlot) && ItemStack.tagMatches(stack, stackInSlot);
		this.inventory.set(slot, stack);
		if (stack.getCount() > this.getMaxStackSize()) {
			stack.setCount(this.getMaxStackSize());
		}
		if (slot == INGREDIENT_SLOTS[0] || slot == INGREDIENT_SLOTS[1] || slot == INGREDIENT_SLOTS[2] && !dirty) {
			this.cookTimeTotal = TOTAL_COOKING_TIME;
			this.cookTime = 0;
			this.setChanged();
		}
	}

	@Override
	public boolean stillValid(Player player) {
		if (this.level.getBlockEntity(this.worldPosition) != this) {
			return false;
		}
		else {
			return player.distanceToSqr((double) this.worldPosition.getX() + 0.5,
					(double) this.worldPosition.getY() + 0.5, (double) this.worldPosition.getZ() + 0.5) <= 64.0;
		}
	}

	@Override
	public void clearContent() {
		inventory.clear();
	}

	@Override
	public Component getDisplayName() {
		return Component.translatable(this.getBlockState().getBlock().getDescriptionId());
	}

	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int syncId, Inventory inv, Player player) {
		return new OvenMenu(syncId, inv, this, this.propertyDelegate);
	}
}
