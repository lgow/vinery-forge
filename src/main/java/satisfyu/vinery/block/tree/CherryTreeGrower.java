package satisfyu.vinery.block.tree;

import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.jetbrains.annotations.Nullable;
import satisfyu.vinery.world.VineryConfiguredFeatures;

public class CherryTreeGrower extends AbstractTreeGrower {
	@Nullable
	@Override
	protected Holder<? extends ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource p_222910_, boolean p_222911_) {
		if (p_222910_.nextBoolean()) {
			return VineryConfiguredFeatures.CHERRY.getHolder().get();
		}
		return VineryConfiguredFeatures.CHERRY_VARIANT.getHolder().get();
	}
}
