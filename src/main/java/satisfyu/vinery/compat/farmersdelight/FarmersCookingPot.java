package satisfyu.vinery.compat.farmersdelight;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import satisfyu.vinery.block.entity.CookingPotEntity;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;

import java.util.stream.Stream;

public class FarmersCookingPot {
	public static boolean canCraft(Recipe<?> recipe, CookingPotEntity entity) {
		if (recipe instanceof CookingPotRecipe r) {
			if (!entity.getItem(CookingPotEntity.BOTTLE_INPUT_SLOT).is(r.getOutputContainer().getItem())) {
				return false;
			}
			else if (entity.getItem(CookingPotEntity.OUTPUT_SLOT).isEmpty()) {
				return true;
			}
			else {
				final ItemStack recipeOutput = r.getResultItem();
				final ItemStack outputSlotStack = entity.getItem(CookingPotEntity.OUTPUT_SLOT);
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
		return false;
	}

	public static ItemStack getContainer(Recipe<RecipeWrapper> recipe) {
		if (recipe instanceof CookingPotRecipe c) {
			return c.getOutputContainer();
		}
		else { return ItemStack.EMPTY; }
	}

	public static Class<CookingPotRecipe> getRecipeClass() {
		return CookingPotRecipe.class;
	}

	public static boolean isItemIngredient(ItemStack stack, Level world) {
		return recipeStream(world).anyMatch(cookingPotRecipe -> cookingPotRecipe.getIngredients().stream()
				.anyMatch(ingredient -> ingredient.test(stack)));
	}

	public static boolean isItemContainer(ItemStack stack, Level world) {
		return recipeStream(world).anyMatch(
				cookingPotRecipe -> cookingPotRecipe.getOutputContainer().is(stack.getItem()));
	}

	private static Stream<CookingPotRecipe> recipeStream(Level world) {
		return world.getRecipeManager().getAllRecipesFor((RecipeType<CookingPotRecipe>) Registry.RECIPE_TYPE.get(
				new ResourceLocation("farmersdelight", "cooking"))).stream();
	}
}