package satisfyu.vinery.registry;

import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import satisfyu.vinery.Vinery;
import satisfyu.vinery.recipe.CookingPotRecipe;
import satisfyu.vinery.recipe.AgingBarrelRecipe;
import satisfyu.vinery.recipe.OvenRecipe;

import java.util.function.Supplier;

public class VineryRecipeTypes {
	public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(
			ForgeRegistries.RECIPE_TYPES, Vinery.MODID);

	public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(
			ForgeRegistries.RECIPE_SERIALIZERS, Vinery.MODID);

	public static final RegistryObject<RecipeType<OvenRecipe>> WOOD_FIRED_OVEN_RECIPE_TYPE = create(
			"wood_fired_oven_cooking");

	public static final RegistryObject<RecipeSerializer<OvenRecipe>> WOOD_FIRED_OVEN_RECIPE_SERIALIZER = create(
			"wood_fired_oven_cooking", OvenRecipe.Serializer::new);

	public static final RegistryObject<RecipeType<AgingBarrelRecipe>> FERMENTATION_BARREL_RECIPE_TYPE = create(
			"wine_fermentation");

	public static final RegistryObject<RecipeSerializer<AgingBarrelRecipe>> FERMENTATION_BARREL_RECIPE_SERIALIZER = create(
			"wine_fermentation", AgingBarrelRecipe.Serializer::new);

	public static final RegistryObject<RecipeType<CookingPotRecipe>> COOKING_POT_RECIPE_TYPE = create("pot_cooking");

	public static final RegistryObject<RecipeSerializer<CookingPotRecipe>> COOKING_POT_RECIPE_SERIALIZER = create(
			"pot_cooking", CookingPotRecipe.Serializer::new);

	private static <T extends Recipe<?>> RegistryObject<RecipeSerializer<T>> create(String name, Supplier<RecipeSerializer<T>> serializer) {
		return RECIPE_SERIALIZERS.register(name, serializer);
	}

	private static <T extends Recipe<?>> RegistryObject<RecipeType<T>> create(String name) {
		Supplier<RecipeType<T>> type = () -> new RecipeType<>() {
			@Override
			public String toString() {
				return name;
			}
		};
		return RECIPE_TYPES.register(name, type);
	}

	public static void register(IEventBus eventBus) {
		RECIPE_TYPES.register(eventBus);
		RECIPE_SERIALIZERS.register(eventBus);
	}
}
