package satisfyu.vinery.compat.rei;

import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.display.DisplaySerializerRegistry;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.forge.REIPluginClient;
import me.shedaniel.rei.plugin.common.BuiltinPlugin;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import satisfyu.vinery.compat.rei.cooking.CookingPotCategory;
import satisfyu.vinery.compat.rei.cooking.CookingPotDisplay;
import satisfyu.vinery.compat.rei.press.WinePressCategory;
import satisfyu.vinery.compat.rei.press.WinePressDisplay;
import satisfyu.vinery.compat.rei.stove.WoodFiredOvenCategory;
import satisfyu.vinery.compat.rei.stove.WoodFiredOvenDisplay;
import satisfyu.vinery.compat.rei.wine.FermentationBarrelCategory;
import satisfyu.vinery.compat.rei.wine.FermentationBarrelDisplay;
import satisfyu.vinery.recipe.CookingPotRecipe;
import satisfyu.vinery.recipe.AgingBarrelRecipe;
import satisfyu.vinery.recipe.OvenRecipe;
import satisfyu.vinery.registry.VineryBlocks;

import java.util.ArrayList;
import java.util.List;

@REIPluginClient
public class VineryReiClientPlugin implements REIClientPlugin {
	public static List<Ingredient> ingredients(Recipe<?> recipe, ItemStack stack) {
		List<Ingredient> l = new ArrayList<>(recipe.getIngredients());
		l.add(0, Ingredient.of(stack.getItem()));
		return l;
	}

	@Override
	public void registerCategories(CategoryRegistry registry) {
		registry.add(new CookingPotCategory());
		registry.add(new WoodFiredOvenCategory());
		registry.add(new FermentationBarrelCategory());
		registry.add(new WinePressCategory());
		registry.addWorkstations(CookingPotDisplay.COOKING_POT_DISPLAY, EntryStacks.of(VineryBlocks.COOKING_POT.get()));
		registry.addWorkstations(WoodFiredOvenDisplay.WOOD_FIRED_OVEN_DISPLAY,
				EntryStacks.of(VineryBlocks.WOOD_FIRED_OVEN.get()));
		registry.addWorkstations(FermentationBarrelDisplay.FERMENTATION_BARREL_DISPLAY,
				EntryStacks.of(VineryBlocks.FERMENTATION_BARREL.get()));
		registry.addWorkstations(WinePressDisplay.WINE_PRESS_DISPLAY, EntryStacks.of(VineryBlocks.WINE_PRESS.get()));
		registry.addWorkstations(BuiltinPlugin.FUEL, EntryStacks.of(VineryBlocks.WOOD_FIRED_OVEN.get()));
	}

	@Override
	public void registerDisplays(DisplayRegistry registry) {
		registry.registerFiller(CookingPotRecipe.class, CookingPotDisplay::new);
		registry.registerFiller(OvenRecipe.class, WoodFiredOvenDisplay::new);
		registry.registerFiller(AgingBarrelRecipe.class, FermentationBarrelDisplay::new);
		registry.add(new WinePressDisplay());
	}

	@Override
	public void registerDisplaySerializer(DisplaySerializerRegistry registry) {
		registry.register(WoodFiredOvenDisplay.WOOD_FIRED_OVEN_DISPLAY,
				WoodFiredOvenDisplay.serializer(WoodFiredOvenDisplay::new));
	}
}
