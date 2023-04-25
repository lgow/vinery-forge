package satisfyu.vinery.compat.rei.stove;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.SimpleGridMenuDisplay;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.registry.RecipeManagerContext;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.crafting.Recipe;
import satisfyu.vinery.Vinery;
import satisfyu.vinery.recipe.OvenRecipe;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class WoodFiredOvenDisplay extends BasicDisplay implements SimpleGridMenuDisplay {
	public static final CategoryIdentifier<WoodFiredOvenDisplay> WOOD_FIRED_OVEN_DISPLAY = CategoryIdentifier.of(
			Vinery.MODID, "wood_fired_oven_display");

	private final float xp;

	public WoodFiredOvenDisplay(OvenRecipe recipe) {
		this(EntryIngredients.ofIngredients(recipe.getIngredients()),
				Collections.singletonList(EntryIngredients.of(recipe.getResultItem())), recipe, recipe.getExperience());
	}

	public WoodFiredOvenDisplay(List<EntryIngredient> input, List<EntryIngredient> output, CompoundTag tag) {
		this(input, output, RecipeManagerContext.getInstance().byId(tag, "location"), tag.getFloat("experience"));
	}

	public WoodFiredOvenDisplay(List<EntryIngredient> input, List<EntryIngredient> output, Recipe<?> recipe, float xp) {
		super(input, output, Optional.ofNullable(recipe).map(Recipe::getId));
		this.xp = xp;
	}

	public static <R extends WoodFiredOvenDisplay> Serializer<R> serializer(Serializer.RecipeLessConstructor<R> constructor) {
		return Serializer.ofRecipeLess(constructor, (display, tag) -> {
			tag.putFloat("experience", display.getXp());
		});
	}

	public float getXp() {
		return xp;
	}

	@Override
	public int getWidth() {
		return 3;
	}

	@Override
	public int getHeight() {
		return 1;
	}

	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return WOOD_FIRED_OVEN_DISPLAY;
	}
}
