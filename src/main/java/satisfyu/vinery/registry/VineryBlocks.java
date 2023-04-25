package satisfyu.vinery.registry;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import satisfyu.vinery.Vinery;
import satisfyu.vinery.block.*;
import satisfyu.vinery.block.tree.CherryTreeGrower;
import satisfyu.vinery.block.tree.OldCherryTreeGrower;
import satisfyu.vinery.item.DrinkBlockBigItem;
import satisfyu.vinery.item.DrinkBlockItem;
import satisfyu.vinery.util.GrapevineType;

import java.util.function.Supplier;

public class VineryBlocks {
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Vinery.MODID);

	public static final RegistryObject<Block> RED_GRAPE_BUSH = registerWithoutItem("red_grape_bush",
			() -> new GrapeBush(BlockBehaviour.Properties.of(Material.PLANT).randomTicks().noCollission()
					.sound(SoundType.SWEET_BERRY_BUSH), GrapevineType.RED));

	public static final RegistryObject<Block> WHITE_GRAPE_BUSH = registerWithoutItem("white_grape_bush",
			() -> new GrapeBush(BlockBehaviour.Properties.of(Material.PLANT).randomTicks().noCollission()
					.sound(SoundType.SWEET_BERRY_BUSH), GrapevineType.WHITE));

	public static final RegistryObject<Block> CHERRY_SAPLING = registerWithItem("cherry_sapling",
			() -> new SaplingBlock(new CherryTreeGrower(), BlockBehaviour.Properties.of(Material.PLANT).noCollission()
					.randomTicks().instabreak().sound(SoundType.GRASS)));

	public static final RegistryObject<Block> OLD_CHERRY_SAPLING = registerWithItem("old_cherry_sapling",
			() -> new SaplingBlock(new OldCherryTreeGrower(), BlockBehaviour.Properties.of(Material.PLANT)
					.noCollission().randomTicks().instabreak().sound(SoundType.GRASS)));

	public static final RegistryObject<Block> GRAPEVINE_LEAVES = registerWithItem("grapevine_leaves",
			() -> new GrapevineLeaves(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES)));

	public static final RegistryObject<Block> CHERRY_LEAVES = registerWithItem("cherry_leaves",
			() -> new CherryLeaves(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES)));

	public static final RegistryObject<Block> WHITE_GRAPE_CRATE = registerWithItem("white_grape_crate",
			() -> new Block(BlockBehaviour.Properties.of(Material.WOOD).strength(2.0F, 3.0F).sound(SoundType.WOOD)));

	public static final RegistryObject<Block> RED_GRAPE_CRATE = registerWithItem("red_grape_crate",
			() -> new Block(BlockBehaviour.Properties.of(Material.WOOD).strength(2.0F, 3.0F).sound(SoundType.WOOD)));

	public static final RegistryObject<Block> CHERRY_CRATE = registerWithItem("cherry_crate",
			() -> new Block(BlockBehaviour.Properties.of(Material.WOOD).strength(2.0F, 3.0F).sound(SoundType.WOOD)));

	public static final RegistryObject<Block> APPLE_CRATE = registerWithItem("apple_crate",
			() -> new Block(BlockBehaviour.Properties.of(Material.WOOD).strength(2.0F, 3.0F).sound(SoundType.WOOD)));

	public static final RegistryObject<Block> GRAPEVINE_POT = registerWithItem("grapevine_pot",
			() -> new GrapevinePotBlock(BlockBehaviour.Properties.copy(Blocks.BARREL)));

	public static final RegistryObject<Block> FERMENTATION_BARREL = registerWithItem("fermentation_barrel",
			() -> new FermentationBarrelBlock(BlockBehaviour.Properties.copy(Blocks.BARREL).noOcclusion()));

	public static final RegistryObject<Block> WINE_PRESS = registerWithItem("wine_press",
			() -> new ApplePressBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(2.0F, 3.0F)
					.sound(SoundType.WOOD).noOcclusion()));

	public static final RegistryObject<Block> CHAIR = registerWithItem("chair",
			() -> new ChairBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(2.0f, 3.0f)
					.sound(SoundType.WOOD)));

	public static final RegistryObject<Block> TABLE = registerWithItem("table",
			() -> new TableBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));

	public static final RegistryObject<Block> WOOD_FIRED_OVEN = registerWithItem("wood_fired_oven",
			() -> new WoodFiredOvenBlock(BlockBehaviour.Properties.copy(Blocks.BRICKS)
					.lightLevel(state -> state.getValue(WoodFiredOvenBlock.LIT) ? 13 : 0)));

	public static final RegistryObject<Block> STOVE = registerWithItem("stove",
			() -> new StoveBlock(BlockBehaviour.Properties.copy(Blocks.BRICKS).lightLevel(block -> 12)));

	public static final RegistryObject<Block> KITCHEN_SINK = registerWithItem("kitchen_sink",
			() -> new KitchenSinkBlock(BlockBehaviour.Properties.copy(Blocks.STONE).noOcclusion()));

	public static final RegistryObject<Block> NINE_BOTTLE_STORAGE = registerWithItem("nine_bottle_storage",
			() -> new NineBottleStorageBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(2.0F, 3.0F)
					.sound(SoundType.WOOD).noOcclusion()));

	public static final RegistryObject<Block> FOUR_BOTTLE_STORAGE = registerWithItem("four_bottle_storage",
			() -> new FourBottleStorageBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(2.0F, 3.0F)
					.sound(SoundType.WOOD).noOcclusion()));

	public static final RegistryObject<Block> CABINET = registerWithItem("cabinet",
			() -> new CabinetBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(2.0F, 3.0F)
					.sound(SoundType.WOOD), VinerySoundEvents.CABINET_OPEN.get(),
					VinerySoundEvents.CABINET_CLOSE.get()));

	public static final RegistryObject<Block> DRAWER = registerWithItem("drawer",
			() -> new DrawerBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(2.0F, 3.0F)
					.sound(SoundType.WOOD), VinerySoundEvents.DRAWER_OPEN.get(), VinerySoundEvents.DRAWER_CLOSE.get()));

	public static final RegistryObject<Block> BARREL = registerWithItem("barrel",
			() -> new BarrelBlock(BlockBehaviour.Properties.copy(Blocks.BARREL)));

	public static final RegistryObject<Block> STORAGE_POT = registerWithItem("storage_pot",
			() -> new StoragePotBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(2.0F, 3.0F)
					.sound(SoundType.WOOD), SoundEvents.DYE_USE, SoundEvents.DYE_USE));

	public static final RegistryObject<Block> STRIPPED_CHERRY_LOG = registerLog("stripped_cherry_log");

	public static final RegistryObject<Block> CHERRY_LOG = registerLog("cherry_log");

	public static final RegistryObject<Block> STRIPPED_CHERRY_WOOD = registerLog("stripped_cherry_wood");

	public static final RegistryObject<Block> CHERRY_WOOD = registerLog("cherry_wood");

	public static final RegistryObject<Block> STRIPPED_OLD_CHERRY_LOG = registerLog("stripped_old_cherry_log");

	public static final RegistryObject<Block> OLD_CHERRY_LOG = registerLog("old_cherry_log");

	public static final RegistryObject<Block> STRIPPED_OLD_CHERRY_WOOD = registerLog("stripped_old_cherry_wood");

	public static final RegistryObject<Block> OLD_CHERRY_WOOD = registerLog("old_cherry_wood");

	public static final RegistryObject<Block> CHERRY_BEAM = registerLog("cherry_beam");

	public static final RegistryObject<Block> CHERRY_PLANKS = registerWithItem("cherry_planks",
			() -> new Block(BlockBehaviour.Properties.of(Material.WOOD).strength(2.0F, 3.0F).sound(SoundType.WOOD)) {
				@Override
				public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) { return true; }

				@Override
				public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) { return 20; }

				@Override
				public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) { return 5; }
			});

	public static final RegistryObject<Block> CHERRY_FLOORBOARD = registerWithItem("cherry_floorboard",
			() -> new Block(BlockBehaviour.Properties.copy(CHERRY_PLANKS.get())));

	public static final RegistryObject<Block> CHERRY_STAIRS = registerWithItem("cherry_stairs",
			() -> new StairBlock(CHERRY_PLANKS.get().defaultBlockState(),
					BlockBehaviour.Properties.copy(CHERRY_PLANKS.get())));

	public static final RegistryObject<Block> CHERRY_SLAB = registerWithItem("cherry_slab",
			() -> new SlabBlock(getSlabSettings()));

	public static final RegistryObject<Block> CHERRY_FENCE = registerWithItem("cherry_fence",
			() -> new FenceBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE)));

	public static final RegistryObject<Block> CHERRY_FENCE_GATE = registerWithItem("cherry_fence_gate",
			() -> new FenceGateBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE)));

	public static final RegistryObject<Block> CHERRY_BUTTON = registerWithItem("cherry_button",
			() -> new WoodButtonBlock(BlockBehaviour.Properties.copy(Blocks.OAK_BUTTON)));

	public static final RegistryObject<Block> CHERRY_PRESSURE_PLATE = registerWithItem("cherry_pressure_plate",
			() -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING,
					BlockBehaviour.Properties.copy(Blocks.OAK_PRESSURE_PLATE)));

	public static final RegistryObject<Block> CHERRY_DOOR = registerWithItem("cherry_door",
			() -> new DoorBlock(BlockBehaviour.Properties.copy(Blocks.OAK_DOOR)));

	public static final WoodType VINERY_CHERRY_WOOD_TYPE = WoodType.register(WoodType.create("vinery_cherry_sign"));

	public static final RegistryObject<Block> CHERRY_TRAPDOOR = registerWithItem("cherry_trapdoor",
			() -> new TrapDoorBlock(BlockBehaviour.Properties.copy(Blocks.OAK_TRAPDOOR)));

	public static final RegistryObject<Block> VINERY_CHERRY_SIGN = registerWithoutItem("vinery_cherry_sign",
			() -> new VineryStandingSignBlock(BlockBehaviour.Properties.of(Material.WOOD).noCollission().strength(1.0f)
					.sound(SoundType.WOOD), VINERY_CHERRY_WOOD_TYPE));

	public static final RegistryObject<Block> VINERY_CHERRY_WALL_SIGN = registerWithoutItem("vinery_cherry_wall_sign",
			() -> new VineryWallSignBlock(BlockBehaviour.Properties.of(Material.WOOD).noCollission().strength(1.0f)
					.sound(SoundType.WOOD).dropsLike(VINERY_CHERRY_SIGN.get()), VINERY_CHERRY_WOOD_TYPE));

	public static final RegistryObject<Block> WINDOW = registerWithItem("window",
			() -> new WindowBlock(BlockBehaviour.Properties.copy(Blocks.GLASS_PANE)));

	public static final RegistryObject<Block> LOAM = registerWithItem("loam",
			() -> new Block(BlockBehaviour.Properties.of(Material.DIRT).strength(2.0F, 3.0F).sound(SoundType.MUD)));

	public static final RegistryObject<Block> LOAM_STAIRS = registerWithItem("loam_stairs",
			() -> new StairBlock(LOAM.get().defaultBlockState(), BlockBehaviour.Properties.of(Material.DIRT)
					.strength(2.0F, 3.0F).sound(SoundType.MUD)));

	public static final RegistryObject<Block> LOAM_SLAB = registerWithItem("loam_slab",
			() -> new SlabBlock(BlockBehaviour.Properties.of(Material.DIRT).strength(2.0F, 3.0F).sound(SoundType.MUD)));

	public static final RegistryObject<Block> COARSE_DIRT_SLAB = registerWithItem("coarse_dirt_slab",
			() -> new VariantSlabBlock(BlockBehaviour.Properties.copy(Blocks.COARSE_DIRT)));

	public static final RegistryObject<Block> DIRT_SLAB = registerWithItem("dirt_slab",
			() -> new VariantSlabBlock(BlockBehaviour.Properties.copy(Blocks.DIRT)));

	public static final RegistryObject<Block> GRASS_SLAB = registerWithItem("grass_slab",
			() -> new SnowyVariantSlabBlock(BlockBehaviour.Properties.copy(Blocks.GRASS_BLOCK)));

	public static final RegistryObject<Block> WINE_BOTTLE = registerWithItem("wine_bottle",
			() -> new EmptyWineBottleBlock(BlockBehaviour.Properties.copy(Blocks.GLASS).instabreak().noOcclusion()));

	public static final RegistryObject<Block> RED_GRAPEJUICE_WINE_BOTTLE = registerEmptyEffectWine(
			"red_grapejuice_wine_bottle", () -> new GrapejuiceWineBottle(getWineSettings()));

	public static final RegistryObject<Block> WHITE_GRAPEJUICE_WINE_BOTTLE = registerEmptyEffectWine(
			"white_grapejuice_wine_bottle", () -> new GrapejuiceWineBottle(getWineSettings()));

	public static final RegistryObject<Block> APPLE_JUICE = registerEmptyEffectWine("apple_juice",
			() -> new GrapejuiceWineBottle(getWineSettings()));

	public static final RegistryObject<Block> CHENET_WINE = registerWine("chenet_wine",
			() -> new ChenetBottleBlock(getWineSettings()), MobEffects.JUMP, true);

	public static final RegistryObject<Block> KING_DANIS_WINE = registerWine("king_danis_wine",
			() -> new MissLilitusBottleBlock(getWineSettings()), MobEffects.LUCK, true);

	public static final RegistryObject<Block> NOIR_WINE = registerWine("noir_wine",
			() -> new WineBottleBlock(getWineSettings()), MobEffects.WATER_BREATHING, false);

	public static final RegistryObject<Block> CLARK_WINE = registerWine("clark_wine",
			() -> new WineBottleBlock(getWineSettings()), MobEffects.FIRE_RESISTANCE, false);

	public static final RegistryObject<Block> MELLOHI_WINE = registerWine("mellohi_wine",
			() -> new MellohiWineBlock(getWineSettings()), MobEffects.DAMAGE_BOOST, true);

	public static final RegistryObject<Block> BOLVAR_WINE = registerWine("bolvar_wine",
			() -> new WineBottleBlock(getWineSettings()), MobEffects.HEALTH_BOOST, false);

	public static final RegistryObject<Block> CHERRY_WINE = registerWine("cherry_wine",
			() -> new CherryWineBlock(getWineSettings()), MobEffects.MOVEMENT_SPEED, false);

	public static final RegistryObject<Block> APPLE_CIDER = registerWine("apple_cider",
			() -> new ChenetBottleBlock(getWineSettings()), MobEffects.HEAL, true);

	public static final RegistryObject<Block> KELP_CIDER = registerWine("kelp_cider",
			() -> new WineBottleBlock(getWineSettings()), MobEffects.WATER_BREATHING, false);

	public static final RegistryObject<Block> SOLARIS_WINE = registerWine("solaris_wine",
			() -> new WineBottleBlock(getWineSettings()), MobEffects.NIGHT_VISION, false);

	public static final RegistryObject<Block> JELLIE_WINE = registerJellieEffectWine("jellie_wine",
			() -> new NonStackableBottleBlock(getWineSettings()));

	public static final RegistryObject<Block> AEGIS_WINE = registerWine("aegis_wine",
			() -> new NonStackableBottleBlock(getWineSettings()), MobEffects.CONFUSION, true);

	public static final RegistryObject<Block> APPLE_WINE = registerWine("apple_wine",
			() -> new WineBottleBlock(getWineSettings()), MobEffects.REGENERATION, true);

	public static final RegistryObject<Block> CHERRY_JAR = registerWithItem("cherry_jar",
			() -> new CherryJarBlock(BlockBehaviour.Properties.of(Material.GLASS).instabreak().noOcclusion()));

	public static final RegistryObject<Block> CHERRY_JAM = registerWithItem("cherry_jam",
			() -> new JamBlock(BlockBehaviour.Properties.of(Material.GLASS).instabreak().noOcclusion()));

	public static final RegistryObject<Block> APPLE_JAM = registerWithItem("apple_jam",
			() -> new JamBlock(BlockBehaviour.Properties.of(Material.GLASS).instabreak().noOcclusion()));

	public static final RegistryObject<Block> SWEETBERRY_JAM = registerWithItem("sweetberry_jam",
			() -> new JamBlock(BlockBehaviour.Properties.of(Material.GLASS).instabreak().noOcclusion()));

	public static final RegistryObject<Block> GRAPE_JAM = registerWithItem("grape_jam",
			() -> new JamBlock(BlockBehaviour.Properties.of(Material.GLASS).instabreak().noOcclusion()));

	public static final RegistryObject<Block> GRAPEVINE_STEM = registerWithItem("grapevine_stem",
			() -> new GrapevineStemBlock(BlockBehaviour.Properties.of(Material.WOOD).randomTicks()
					.sound(SoundType.WOOD)));

	public static final RegistryObject<Block> WINE_BOX = registerWithItem("wine_box",
			() -> new WineBoxBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(2.0F, 3.0F).noOcclusion()));

	public static final RegistryObject<Block> BIG_TABLE = registerWithItem("big_table",
			() -> new BigTableBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(2.0F, 2.0F)));

	public static final RegistryObject<Block> SHELF = registerWithItem("shelf",
			() -> new ShelfBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(2.0F, 2.0F)));

	public static final RegistryObject<Block> FLOWER_BOX = registerWithItem("flower_box",
			() -> new FlowerBoxBlock(Blocks.AIR, BlockBehaviour.Properties.of(Material.DECORATION).instabreak().noOcclusion()));

	public static final RegistryObject<Block> TALL_FLOWER_POT = registerWithItem("tall_flower_pot",
			() -> new TallFlowerPotBlock(BlockBehaviour.Properties.of(Material.DECORATION).instabreak().noOcclusion()));

	public static final RegistryObject<Block> BASKET = registerWithItem("basket",
			() -> new BasketBlock(BlockBehaviour.Properties.of(Material.DECORATION).instabreak().noOcclusion(), 1));

	public static final RegistryObject<Block> COOKING_POT = registerWithItem("cooking_pot",
			() -> new CookingPotBlock(BlockBehaviour.Properties.of(Material.STONE).instabreak().noOcclusion()));

	public static final RegistryObject<Block> STACKABLE_LOG = registerWithItem("stackable_log",
			() -> new StackableLogBlock(getLogBlockSettings().noOcclusion()
					.lightLevel(state -> state.getValue(StackableLogBlock.FIRED) ? 13 : 0)));

	public static final RegistryObject<Block> CRUSTY_BREAD = registerWithItem("crusty_bread",
			() -> new BreadBlock(BlockBehaviour.Properties.copy(Blocks.CAKE).noOcclusion()));

	public static final RegistryObject<Block> APPLE_PIE = registerWithItem("apple_pie",
			() -> new PieBlock(BlockBehaviour.Properties.copy(Blocks.CAKE)));

	private static <T extends Block> RegistryObject<T> registerWithoutItem(String path, Supplier<T> block) {
		return BLOCKS.register(path, block);
	}

	private static <T extends Block> RegistryObject<T> registerWithItem(String name, Supplier<T> block) {
		RegistryObject<T> toReturn = BLOCKS.register(name, block);
		registerBlockItem(name, () -> new BlockItem(toReturn.get(), new Item.Properties().tab(Vinery.TAB)));
		return toReturn;
	}

	private static <T extends Block> RegistryObject<T> registerWithCustomItem(String path, Supplier<T> block, Supplier<Item> itemSupplier) {
		RegistryObject<T> toReturn = BLOCKS.register(path, block);
		registerBlockItem(path, itemSupplier);
		return toReturn;
	}

	private static <T extends Item> void registerBlockItem(String path, Supplier<T> itemSupplier) {
		VineryItems.ITEMS.register(path, itemSupplier);
	}

	private static RegistryObject<Block> registerSign(String path, Supplier<Block> block, MobEffect effect) {
		RegistryObject<Block> toReturn = BLOCKS.register(path, block);
		Item.Properties properties = new Item.Properties().tab(Vinery.TAB);
		return toReturn;
	}

	private static RegistryObject<Block> registerWine(String path, Supplier<Block> block, MobEffect effect, boolean bigWine) {
		RegistryObject<Block> toReturn = BLOCKS.register(path, block);
		Item.Properties properties = new Item.Properties().food(VineryFoods.WINE_EFFECT(effect)).tab(
				Vinery.TAB);
		registerBlockItem(path, !bigWine ? () -> new DrinkBlockItem(toReturn.get(), properties)
				: () -> new DrinkBlockBigItem(toReturn.get(), properties));
		return toReturn;
	}

	private static RegistryObject<Block> registerEmptyEffectWine(String path, Supplier<Block> block) {
		RegistryObject<Block> toReturn = BLOCKS.register(path, block);
		Item.Properties properties = new Item.Properties().food(VineryFoods.WINE_EMPTY_EFFECT).tab(Vinery.TAB);
		registerBlockItem(path, () -> new DrinkBlockItem(toReturn.get(), properties));
		return toReturn;
	}

	private static RegistryObject<Block> registerJellieEffectWine(String path, Supplier<Block> block) {
		RegistryObject<Block> toReturn = BLOCKS.register(path, block);
		Item.Properties properties = new Item.Properties().food(VineryFoods.WINE_JELLIE_EFFECT).tab(Vinery.TAB);
		registerBlockItem(path, () -> new DrinkBlockItem(toReturn.get(), properties));
		return toReturn;
	}

	private static RegistryObject<Block> registerLog(String path) {
		return registerWithItem(path, () -> new FlammableRotatedPillarBlock(getLogBlockSettings()));
	}

	private static BlockBehaviour.Properties getLogBlockSettings() {
		return BlockBehaviour.Properties.of(Material.WOOD).strength(2.0F).sound(SoundType.WOOD);
	}

	private static BlockBehaviour.Properties getWineSettings() {
		return BlockBehaviour.Properties.copy(Blocks.GLASS).noOcclusion().instabreak();
	}

	private static BlockBehaviour.Properties getBushSettings() {
		return BlockBehaviour.Properties.copy(Blocks.SWEET_BERRY_BUSH);
	}

	private static BlockBehaviour.Properties getSlabSettings() {
		return getLogBlockSettings().explosionResistance(3.0F);
	}

	private static BlockBehaviour.Properties getGrapevineSettings() {
		return BlockBehaviour.Properties.of(Material.WOOD).strength(2.0F).randomTicks().sound(SoundType.WOOD);
	}

	public static void register(IEventBus eventBus) {
		BLOCKS.register(eventBus);
	}
}