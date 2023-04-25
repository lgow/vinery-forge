package satisfyu.vinery.world;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.Registry;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.RandomSpreadFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.BeehiveDecorator;
import net.minecraft.world.level.levelgen.feature.trunkplacers.FancyTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import satisfyu.vinery.Vinery;
import satisfyu.vinery.block.CherryLeaves;
import satisfyu.vinery.registry.VineryBlocks;

import java.util.function.Supplier;

public class VineryConfiguredFeatures {
	public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES,
			Vinery.MODID);

	public static final DeferredRegister<ConfiguredFeature<?, ?>> CONFIGURED_FEATURES = DeferredRegister.create(
			Registry.CONFIGURED_FEATURE_REGISTRY, Vinery.MODID);

	public static final DeferredRegister<PlacedFeature> PLACED_FEATURES = DeferredRegister.create(
			Registry.PLACED_FEATURE_REGISTRY, Vinery.MODID);

	public static final RegistryObject<ConfiguredFeature<?, ?>> CHERRY = registerConfiguredFeature("cherry",
			() -> new ConfiguredFeature<>(Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
					BlockStateProvider.simple(VineryBlocks.CHERRY_LOG.get()), new StraightTrunkPlacer(5, 2, 0),
					SimpleStateProvider.simple(VineryBlocks.CHERRY_LEAVES.get().defaultBlockState()),
					new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3),
					new TwoLayersFeatureSize(1, 0, 1)).ignoreVines().build()));

	public static final RegistryObject<ConfiguredFeature<TreeConfiguration, ?>> CHERRY_VARIANT = registerConfiguredFeature(
			"cherry_variant",
			() -> new ConfiguredFeature<>(Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
					BlockStateProvider.simple(VineryBlocks.CHERRY_LOG.get()), new StraightTrunkPlacer(5, 2, 0),
					SimpleStateProvider.simple(
							VineryBlocks.CHERRY_LEAVES.get().defaultBlockState().setValue(CherryLeaves.VARIANT, true)
									.setValue(CherryLeaves.HAS_CHERRIES, false)),
					new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3),
					new TwoLayersFeatureSize(1, 0, 1)).ignoreVines().build()));

	public static final RegistryObject<ConfiguredFeature<?, ?>> OLD_CHERRY = registerConfiguredFeature("old_cherry",
			() -> new ConfiguredFeature<>(Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
					BlockStateProvider.simple(VineryBlocks.OLD_CHERRY_LOG.get()), new FancyTrunkPlacer(4, 14, 2),
					SimpleStateProvider.simple(VineryBlocks.CHERRY_LEAVES.get().defaultBlockState()),
					new RandomSpreadFoliagePlacer(ConstantInt.of(3), ConstantInt.of(0), ConstantInt.of(2), 50),
					new TwoLayersFeatureSize(1, 1, 2)).decorators(ImmutableList.of()).forceDirt().build()));

	public static final RegistryObject<ConfiguredFeature<TreeConfiguration, ?>> OLD_CHERRY_BEE = registerConfiguredFeature(
			"old_cherry_bee",
			() -> new ConfiguredFeature<>(Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
					BlockStateProvider.simple(VineryBlocks.OLD_CHERRY_LOG.get()), new FancyTrunkPlacer(4, 14, 2),
					SimpleStateProvider.simple(VineryBlocks.CHERRY_LEAVES.get().defaultBlockState()),
					new RandomSpreadFoliagePlacer(ConstantInt.of(3), ConstantInt.of(0), ConstantInt.of(2), 50),
					new TwoLayersFeatureSize(1, 1, 2)).decorators(ImmutableList.of(new BeehiveDecorator(0.5f)))
					.forceDirt().build()));

	public static final RegistryObject<ConfiguredFeature<TreeConfiguration, ?>> OLD_CHERRY_VARIANT = registerConfiguredFeature(
			"old_cherry_variant",
			() -> new ConfiguredFeature<>(Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
					BlockStateProvider.simple(VineryBlocks.OLD_CHERRY_LOG.get()), new FancyTrunkPlacer(4, 14, 2),
					SimpleStateProvider.simple(
							VineryBlocks.CHERRY_LEAVES.get().defaultBlockState().setValue(CherryLeaves.VARIANT, true)
									.setValue(CherryLeaves.HAS_CHERRIES, false)),
					new RandomSpreadFoliagePlacer(ConstantInt.of(3), ConstantInt.of(0), ConstantInt.of(2), 50),
					new TwoLayersFeatureSize(1, 1, 2)).decorators(ImmutableList.of()).forceDirt().build()));

	public static final RegistryObject<ConfiguredFeature<TreeConfiguration, ?>> OLD_CHERRY_VARIANT_WITH_BEE = registerConfiguredFeature(
			"old_cherry_variant_with_bee",
			() -> new ConfiguredFeature<>(Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
					BlockStateProvider.simple(VineryBlocks.OLD_CHERRY_LOG.get()), new FancyTrunkPlacer(4, 14, 2),
					SimpleStateProvider.simple(
							VineryBlocks.CHERRY_LEAVES.get().defaultBlockState().setValue(CherryLeaves.VARIANT, true)
									.setValue(CherryLeaves.HAS_CHERRIES, false)),
					new RandomSpreadFoliagePlacer(ConstantInt.of(3), ConstantInt.of(0), ConstantInt.of(2), 50),
					new TwoLayersFeatureSize(1, 1, 2)).decorators(ImmutableList.of(new BeehiveDecorator(0.5f)))
					.forceDirt().build()));

	public static void register(IEventBus eventBus) {
		FEATURES.register(eventBus);
		CONFIGURED_FEATURES.register(eventBus);
		PLACED_FEATURES.register(eventBus);
	}

	private static <T extends ConfiguredFeature<?, ?>> RegistryObject<T> registerConfiguredFeature(String path, Supplier<T> supplier) {
		return CONFIGURED_FEATURES.register(path, supplier);
	}
}

