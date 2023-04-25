package satisfyu.vinery.registry;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.food.Foods;

public class VineryFoods {
	public static final FoodProperties WINE_EMPTY_EFFECT = new FoodProperties.Builder().nutrition(1).saturationMod(0)
			.alwaysEat().fast().effect(() -> new MobEffectInstance(VineryEffects.EMPTY.get(), 45 * 20), 1.0F).build();

	public static final FoodProperties WINE_JELLIE_EFFECT = new FoodProperties.Builder().nutrition(1).saturationMod(0)
			.alwaysEat().fast().effect(() -> new MobEffectInstance(VineryEffects.JELLIE.get(), 45 * 20), 1.0F).build();

	public static final FoodProperties GRAPE = Foods.SWEET_BERRIES;

	public static FoodProperties WINE_EFFECT(MobEffect mobEffect) {
		return new FoodProperties.Builder().nutrition(1).saturationMod(0).alwaysEat().fast().effect(()->
				new MobEffectInstance(mobEffect, 45 * 20), 1.0F).build();
	}
}