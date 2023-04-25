package satisfyu.vinery.registry;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import satisfyu.vinery.Vinery;
import satisfyu.vinery.item.*;
import satisfyu.vinery.util.GrapevineType;

import java.util.function.Supplier;

public class VineryItems {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Vinery.MODID);

	public static final RegistryObject<Item> RED_GRAPE_SEEDS = register("red_grape_seeds",
			() -> new GrapeBushSeedItem(VineryBlocks.RED_GRAPE_BUSH.get(), new Item.Properties().tab(Vinery.TAB),
					GrapevineType.RED));

	public static final RegistryObject<Item> RED_GRAPE = register("red_grape",
			() -> new GrapeItem(new Item.Properties().food(VineryFoods.GRAPE).tab(Vinery.TAB), GrapevineType.RED));

	public static final RegistryObject<Item> WHITE_GRAPE_SEEDS = register("white_grape_seeds",
			() -> new GrapeBushSeedItem(VineryBlocks.WHITE_GRAPE_BUSH.get(), new Item.Properties().tab(Vinery.TAB),
					GrapevineType.WHITE));

	public static final RegistryObject<Item> WHITE_GRAPE = register("white_grape",
			() -> new GrapeItem(new Item.Properties().food(VineryFoods.GRAPE).tab(Vinery.TAB), GrapevineType.WHITE));

	public static final RegistryObject<Item> CHERRY = register("cherry",
			() -> new CherryItem(new Item.Properties().food(Foods.SWEET_BERRIES).tab(Vinery.TAB)));

	public static final RegistryObject<Item> BREAD_SLICE = register("bread_slice",
			() -> new Item(new Item.Properties().food(Foods.BAKED_POTATO).tab(Vinery.TAB)));

	public static final RegistryObject<Item> APPLE_MASH = register("apple_mash",
			() -> new Item(new Item.Properties().food(Foods.APPLE).tab(Vinery.TAB)));

	public static final RegistryObject<Item> FAUCET = register("faucet",
			() -> new FaucetItem(new Item.Properties().tab(Vinery.TAB)));

	public static final RegistryObject<Item> STRAW_HAT = register("straw_hat",
			() -> new StrawHatItem(VineryMaterials.VINEMAKER_ARMOR, EquipmentSlot.HEAD,
					new Item.Properties().rarity(Rarity.COMMON).tab(Vinery.TAB)));

	public static final RegistryObject<Item> VINEMAKER_APRON = register("vinemaker_apron",
			() -> new WinemakerArmorItem(VineryMaterials.VINEMAKER_ARMOR, EquipmentSlot.CHEST,
					new Item.Properties().rarity(Rarity.EPIC).tab(Vinery.TAB)));

	public static final RegistryObject<Item> VINEMAKER_LEGGINGS = register("vinemaker_leggings",
			() -> new WinemakerArmorItem(VineryMaterials.VINEMAKER_ARMOR, EquipmentSlot.LEGS,
					new Item.Properties().rarity(Rarity.RARE).tab(Vinery.TAB)));

	public static final RegistryObject<Item> VINEMAKER_BOOTS = register("vinemaker_boots",
			() -> new WinemakerArmorItem(VineryMaterials.VINEMAKER_ARMOR, EquipmentSlot.FEET,
					new Item.Properties().rarity(Rarity.EPIC).tab(Vinery.TAB)));

	public static final RegistryObject<Item> GLOVES = register("gloves",
			() -> new GlovesItem(new Item.Properties().rarity(Rarity.RARE).tab(Vinery.TAB)));

	public static final RegistryObject<Item> DOUGH = register("dough",
			() -> new CherryItem(new Item.Properties().tab(Vinery.TAB)));

	public static final RegistryObject<Item> CHOCOLATE_BREAD = register("chocolate_bread",
			() -> new ChocolateBreadItem(new Item.Properties().food(Foods.BREAD).tab(Vinery.TAB)));

	public static final RegistryObject<Item> TOAST = register("toast",
			() -> new ToastItem(new Item.Properties().food(Foods.BEETROOT_SOUP).tab(Vinery.TAB)));

	public static final RegistryObject<Item> DONUT = register("donut",
			() -> new DoughnutItem(new Item.Properties().food(Foods.CARROT).tab(Vinery.TAB)));

	public static final RegistryObject<Item> MILK_BREAD = register("milk_bread",
			() -> new MilkBreadItem(new Item.Properties().food(Foods.COOKIE).tab(Vinery.TAB)));

	public static final RegistryObject<Item> APPLE_CUPCAKE = register("apple_cupcake",
			() -> new CupcakeItem(new Item.Properties().food(Foods.COOKED_BEEF).tab(Vinery.TAB)));

	public static final RegistryObject<Item> APPLE_PIE_SLICE = register("apple_pie_slice",
			() -> new Item(new Item.Properties().food(Foods.COOKED_BEEF).tab(Vinery.TAB)));

	public static final RegistryObject<Item> APPLESAUCE = register("applesauce",
			() -> new AppleSauceItem(new Item.Properties().food(Foods.COOKED_RABBIT).tab(Vinery.TAB)));

	public static final RegistryObject<Item> MULE_SPAWN_EGG = register("mule_spawn_egg",
			() -> new SpawnEggItem(VineryEntites.TRADER_MULE.get(), 0x8b7867, 0x5a4e43,
					new Item.Properties().tab(Vinery.TAB)));

	public static final RegistryObject<Item> WANDERING_WINEMAKER_SPAWN_EGG = register("wandering_winemaker_spawn_egg",
			() -> new SpawnEggItem(VineryEntites.WANDERING_WINEMAKER.get(), 0xb78272, 0x3c4a73,
					new Item.Properties().tab(Vinery.TAB)));

	public static final RegistryObject<Item> CHERRY_BOAT = register("cherry_boat",
			() -> new VineryBoatItem(new Item.Properties().stacksTo(1).tab(Vinery.TAB), "cherry"));

	public static final RegistryObject<Item> CHERRY_CHEST_BOAT = register("cherry_chest_boat",
			() -> new VineryChestBoatItem(new Item.Properties().stacksTo(1).tab(Vinery.TAB), "cherry"));

	public static final RegistryObject<Item> CHERRY_SIGN = register("cherry_sign",
			() -> new SignItem(new Item.Properties().stacksTo(1).tab(Vinery.TAB), VineryBlocks.VINERY_CHERRY_SIGN.get(),
					VineryBlocks.VINERY_CHERRY_WALL_SIGN.get()));

	public static <T extends Item> RegistryObject<T> register(String name, Supplier<T> item) {
		RegistryObject<T> toReturn = ITEMS.register(name, item);
		return toReturn;
	}

	public static void register(IEventBus eventBus) {
		ITEMS.register(eventBus);
	}
}
