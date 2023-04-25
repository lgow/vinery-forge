package satisfyu.vinery.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.Nullable;
import satisfyu.vinery.block.CookingPotBlock;
import satisfyu.vinery.client.gui.handler.CookingPotMenu;
import satisfyu.vinery.compat.farmersdelight.FarmersCookingPot;
import satisfyu.vinery.recipe.CookingPotRecipe;
import satisfyu.vinery.registry.VineryBlockEntityTypes;
import satisfyu.vinery.registry.VineryRecipeTypes;
import satisfyu.vinery.util.VineryTags;
import satisfyu.vinery.util.VineryUtils;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;

public class CookingPotEntity extends BlockEntity implements Container, MenuProvider {
	public static final int MAX_COOKING_TIME = 600; // Time in ticks (30s)

	public static final int BOTTLE_INPUT_SLOT = 6;

	public static final int OUTPUT_SLOT = 7;

	private static final int MAX_CAPACITY = 8;

	private static final int INGREDIENTS_AREA = 2 * 3;

	private static boolean isBeingBurned;

	private final NonNullList<ItemStack> inventory = NonNullList.withSize(MAX_CAPACITY, ItemStack.EMPTY);

	private final ContainerData delegate;

	private int cookingTime;

	public CookingPotEntity(BlockPos pos, BlockState state) {
		super(VineryBlockEntityTypes.COOKING_POT_BLOCK_ENTITY.get(), pos, state);
		this.delegate = new ContainerData() {
			@Override
			public int get(int index) {
				return switch (index) {
					case 0 -> CookingPotEntity.this.cookingTime;
					case 1 -> isBeingBurned ? 1 : 0;
					default -> 0;
				};
			}

			@Override
			public void set(int index, int value) {
				switch (index) {
					case 0 -> CookingPotEntity.this.cookingTime = value;
					case 1 -> isBeingBurned = value != 0;
				}
			}

			@Override
			public int getCount() {
				return 2;
			}
		};
	}

	public static boolean isBeingBurned(CookingPotEntity entity) {
		if (entity.getLevel() == null) { throw new NullPointerException("Null world invoked"); }
		final BlockState belowState = entity.getLevel().getBlockState(entity.getBlockPos().below());
		final var optionalList = Registry.BLOCK.getTag(VineryTags.Blocks.ALLOWS_COOKING_ON_POT);
		final var entryList = optionalList.orElse(null);
		if (entryList == null) {
			return false;
		}
		else if (!entryList.contains(belowState.getBlock().builtInRegistryHolder())) {
			return false;
		}
		else { return belowState.getValue(BlockStateProperties.LIT); }
	}

	private static boolean canCraft(Recipe<?> recipe, CookingPotEntity entity) {
		if (recipe == null || recipe.getResultItem().isEmpty()) {
			return false;
		}
		if (recipe instanceof CookingPotRecipe c) {
			if (!entity.getItem(BOTTLE_INPUT_SLOT).is(c.getContainer().getItem())) {
				return false;
			}
			else if (entity.getItem(OUTPUT_SLOT).isEmpty()) {
				return true;
			}
			else {
				final ItemStack recipeOutput = c.getResultItem();
				final ItemStack outputSlotStack = entity.getItem(OUTPUT_SLOT);
				final int outputSlotCount = outputSlotStack.getCount();
				if (!outputSlotStack.sameItem(recipeOutput)) {
					return false;
				}
				else if (outputSlotCount < entity.getMaxStackSize()
						&& outputSlotCount < outputSlotStack.getMaxStackSize()) {
					return true;
				}
				else {
					return outputSlotCount < recipeOutput.getMaxStackSize();
				}
			}
		}
		else {
			if (VineryUtils.isFDLoaded()) {
				return FarmersCookingPot.canCraft(recipe, entity);
			}
		}
		return false;
	}

	private static void craft(Recipe<?> recipe, CookingPotEntity entity) {
		if (!canCraft(recipe, entity)) {
			return;
		}
		final ItemStack recipeOutput = recipe.getResultItem();
		final ItemStack outputSlotStack = entity.getItem(OUTPUT_SLOT);
		if (outputSlotStack.isEmpty()) {
			entity.setItem(OUTPUT_SLOT, recipeOutput.copy());
		}
		else if (outputSlotStack.is(recipeOutput.getItem())) {
			outputSlotStack.grow(recipeOutput.getCount());
		}
		final NonNullList<Ingredient> ingredients = recipe.getIngredients();
		boolean[] slotUsed = new boolean[INGREDIENTS_AREA];
		for (int i = 0; i < recipe.getIngredients().size(); i++) {
			Ingredient ingredient = ingredients.get(i);
			// Looks for the best slot to take it from
			final ItemStack bestSlot = entity.getItem(i);
			if (ingredient.test(bestSlot) && !slotUsed[i]) {
				slotUsed[i] = true;
				bestSlot.shrink(1);
			}
			else {
				// check all slots in search of the ingredient
				for (int j = 0; j < INGREDIENTS_AREA; j++) {
					ItemStack stack = entity.getItem(j);
					if (ingredient.test(stack) && !slotUsed[j]) {
						slotUsed[j] = true;
						stack.shrink(1);
					}
				}
			}
		}
		entity.getItem(BOTTLE_INPUT_SLOT).shrink(1);
	}

	public static void tick(Level world, BlockPos pos, BlockState state, CookingPotEntity blockEntity) {
		if (world.isClientSide()) {
			return;
		}
		isBeingBurned = isBeingBurned(blockEntity);
		if (!isBeingBurned) {
			if (state.getValue(CookingPotBlock.LIT)) {
				world.setBlock(pos, state.setValue(CookingPotBlock.LIT, false), Block.UPDATE_ALL);
			}
			return;
		}
		Recipe<?> recipe = VineryUtils.isFDLoaded() ? world.getRecipeManager().getRecipeFor(
				VineryRecipeTypes.COOKING_POT_RECIPE_TYPE.get(), blockEntity, world).orElse(null)
				: (Recipe<?>) world.getRecipeManager().getRecipeFor((RecipeType) ModRecipeTypes.COOKING.get(),
						blockEntity, world).orElse(null);
		boolean canCraft = canCraft(recipe, blockEntity);
		if (canCraft) {
			blockEntity.cookingTime++;
			if (blockEntity.cookingTime >= MAX_COOKING_TIME) {
				blockEntity.cookingTime = 0;
				craft(recipe, blockEntity);
			}
		}
		else if (!canCraft(recipe, blockEntity)) {
			blockEntity.cookingTime = 0;
		}
		if (canCraft) {
			world.setBlock(pos,
					blockEntity.getBlockState().getBlock().defaultBlockState().setValue(CookingPotBlock.COOKING, true)
							.setValue(CookingPotBlock.LIT, true), Block.UPDATE_ALL);
		}
		else if (state.getValue(CookingPotBlock.COOKING)) {
			world.setBlock(pos,
					blockEntity.getBlockState().getBlock().defaultBlockState().setValue(CookingPotBlock.COOKING, false)
							.setValue(CookingPotBlock.LIT, true), Block.UPDATE_ALL);
		}
		else if (state.getValue(CookingPotBlock.LIT) != isBeingBurned) {
			world.setBlock(pos, state.setValue(CookingPotBlock.LIT, isBeingBurned), Block.UPDATE_ALL);
		}
	}

	private static void setItem(int slot, ItemStack stack, CookingPotEntity entity) {
		entity.inventory.set(slot, stack);
		if (stack.getCount() > entity.getMaxStackSize()) {
			stack.setCount(entity.getMaxStackSize());
		}
		entity.setChanged();
	}

	@Override
	public void load(CompoundTag nbt) {
		super.load(nbt);
		ContainerHelper.loadAllItems(nbt, this.inventory);
		this.cookingTime = nbt.getInt("CookingTime");
	}

	@Override
	protected void saveAdditional(CompoundTag nbt) {
		super.saveAdditional(nbt);
		ContainerHelper.saveAllItems(nbt, this.inventory);
		nbt.putInt("CookingTime", this.cookingTime);
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
		this.inventory.set(slot, stack);
		if (stack.getCount() > this.getMaxStackSize()) {
			stack.setCount(this.getMaxStackSize());
		}
		this.setChanged();
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
		return new CookingPotMenu(syncId, inv, this, this.delegate);
	}
}


