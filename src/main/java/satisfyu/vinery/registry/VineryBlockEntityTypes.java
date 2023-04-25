package satisfyu.vinery.registry;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import satisfyu.vinery.Vinery;
import satisfyu.vinery.block.entity.*;
import satisfyu.vinery.block.entity.chair.ChairEntity;

import java.util.function.Supplier;

public class VineryBlockEntityTypes {
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPE = DeferredRegister.create(
			ForgeRegistries.BLOCK_ENTITY_TYPES, Vinery.MODID);

	public static final RegistryObject<BlockEntityType<VinerySignBlockEntity>> VINERY_SIGN_BLOCK_ENTITY = create(
			"vinery_sign_entity",
			() -> BlockEntityType.Builder.of(VinerySignBlockEntity::new, VineryBlocks.VINERY_CHERRY_SIGN.get(),
					VineryBlocks.VINERY_CHERRY_WALL_SIGN.get()).build(null));

	public static final RegistryObject<BlockEntityType<WoodFiredOvenBlockEntity>> WOOD_FIRED_OVEN_BLOCK_ENTITY = create(
			"wood_fired_oven",
			() -> BlockEntityType.Builder.of(WoodFiredOvenBlockEntity::new, VineryBlocks.WOOD_FIRED_OVEN.get())
					.build(null));

	public static final RegistryObject<EntityType<ChairEntity>> CHAIR = VineryEntites.create("chair",
			EntityType.Builder.of(ChairEntity::new, MobCategory.MISC).sized(0.001F, 0.001F));

	public static final RegistryObject<BlockEntityType<CookingPotEntity>> COOKING_POT_BLOCK_ENTITY = create(
			"cooking_pot",
			() -> BlockEntityType.Builder.of(CookingPotEntity::new, VineryBlocks.COOKING_POT.get()).build(null));

	private static <T extends BlockEntityType<?>> RegistryObject<T> create(final String path, final Supplier<T> type) {
		return BLOCK_ENTITY_TYPE.register(path, type);
	}

	public static final RegistryObject<BlockEntityType<AgingBarrelBlockEntity>> FERMENTATION_BARREL_ENTITY = create(
			"fermentation_barrel",
			() -> BlockEntityType.Builder.of(AgingBarrelBlockEntity::new, VineryBlocks.FERMENTATION_BARREL.get())
					.build(null));

	public static void register(IEventBus eventBus) {
		BLOCK_ENTITY_TYPE.register(eventBus);
	}

	public static final RegistryObject<BlockEntityType<WinePressBlockEntity>> WINE_PRESS_BLOCK_ENTITY = create(
			"wine_press",
			() -> BlockEntityType.Builder.of(WinePressBlockEntity::new, VineryBlocks.WINE_PRESS.get()).build(null));

	public static final RegistryObject<BlockEntityType<WineBoxEntity>> WINE_BOX_BLOCK_ENTITY = create("wine_box",
			() -> BlockEntityType.Builder.of(WineBoxEntity::new, VineryBlocks.WINE_BOX.get()).build(null));

	public static final RegistryObject<BlockEntityType<DrawerBlockEntity>> DRAWER_BLOCK_ENTITY = create(
			"drawer",
			() -> BlockEntityType.Builder.of(DrawerBlockEntity::new, VineryBlocks.DRAWER.get()).build(null));

	public static final RegistryObject<BlockEntityType<DrawerBlockEntity>> CABINET_BLOCK_ENTITY = create(
			"cabinet",
			() -> BlockEntityType.Builder.of(DrawerBlockEntity::new, VineryBlocks.CABINET.get()).build(null));

	public static final RegistryObject<BlockEntityType<FourWineBottleStorageBlockEntity>> FOUR_WINE_RACK_ENTITY = create(
			"four_wine_rack", () -> BlockEntityType.Builder.of(FourWineBottleStorageBlockEntity::new,
					VineryBlocks.FOUR_BOTTLE_STORAGE.get()).build(null));

	public static final RegistryObject<BlockEntityType<NineWineBottleStorageBlockEntity>> NINE_WINE_RACK_ENTITY = create(
			"nine_wine_rack", () -> BlockEntityType.Builder.of(NineWineBottleStorageBlockEntity::new,
					VineryBlocks.NINE_BOTTLE_STORAGE.get()).build(null));

	public static final RegistryObject<BlockEntityType<ShelfEntity>> SHELF_ENTITY = create("shelf",
			() -> BlockEntityType.Builder.of(ShelfEntity::new, VineryBlocks.SHELF.get()).build(null));

	public static final RegistryObject<BlockEntityType<StoragePotBlockEntity>> STORAGE_POT_ENTITY = create("storagepot",
			() -> BlockEntityType.Builder.of(StoragePotBlockEntity::new, VineryBlocks.STORAGE_POT.get()).build(null));

	public static final RegistryObject<BlockEntityType<TallFlowerPotBlockEntity>> TALL_FLOWER_POT_ENTITY = create("tall_flower_pot",
			() -> BlockEntityType.Builder.of(TallFlowerPotBlockEntity::new, VineryBlocks.TALL_FLOWER_POT.get()).build(null));

	public static final RegistryObject<BlockEntityType<FlowerBoxBlockEntity>> FLOWER_BOX_ENTITY = create("flower_box",
			() -> BlockEntityType.Builder.of(FlowerBoxBlockEntity::new, VineryBlocks.FLOWER_BOX.get()).build(null));
}
