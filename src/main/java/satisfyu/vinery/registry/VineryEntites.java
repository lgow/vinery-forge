package satisfyu.vinery.registry;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import satisfyu.vinery.Vinery;
import satisfyu.vinery.entity.TraderMuleEntity;
import satisfyu.vinery.entity.VineryBoat;
import satisfyu.vinery.entity.VineryChestBoat;
import satisfyu.vinery.entity.WanderingWinemakerEntity;
import satisfyu.vinery.util.VineryIdentifier;

public class VineryEntites {
	public static final DeferredRegister<EntityType<?>> ENTITY_TYPE = DeferredRegister.create(
			ForgeRegistries.ENTITY_TYPES, Vinery.MODID);

	public static final RegistryObject<EntityType<TraderMuleEntity>> TRADER_MULE = create("mule", EntityType.Builder.of(
			TraderMuleEntity::new, MobCategory.CREATURE).sized(0.9f, 1.87f).setTrackingRange(10));

	public static final RegistryObject<EntityType<WanderingWinemakerEntity>> WANDERING_WINEMAKER = create(
			"wandering_winemaker", EntityType.Builder.of(WanderingWinemakerEntity::new, MobCategory.CREATURE)
					.sized(0.6f, 1.95f).setTrackingRange(10));

	public static final RegistryObject<EntityType<VineryBoat>> BOAT = create("boat", EntityType.Builder.<VineryBoat> of(
			VineryBoat::new, MobCategory.MISC).sized(1.375F, 0.5625F).setTrackingRange(10));

	public static final RegistryObject<EntityType<VineryChestBoat>> CHEST_BOAT = create("chest_boat",
			EntityType.Builder.<VineryChestBoat> of(VineryChestBoat::new, MobCategory.MISC).sized(1.375F, 0.5625F)
					.setTrackingRange(10));

	public static <T extends Entity> RegistryObject<EntityType<T>> create(final String path, EntityType.Builder<T> entityType) {
		return ENTITY_TYPE.register(path, () -> entityType.build(new VineryIdentifier(path).toString()));
	}

	public static void register(IEventBus eventBus) {
		ENTITY_TYPE.register(eventBus);
	}
}