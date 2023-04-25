package satisfyu.vinery.block.tree;

import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.jetbrains.annotations.Nullable;
import satisfyu.vinery.world.VineryConfiguredFeatures;

public class OldCherryTreeGrower extends AbstractTreeGrower {
	@Nullable
	@Override
	protected Holder<? extends ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource p_222910_, boolean p_222911_) {
		if (p_222910_.nextBoolean()) {
			if (p_222911_) {
				return VineryConfiguredFeatures.OLD_CHERRY_BEE.getHolder().get();
			}
			return VineryConfiguredFeatures.OLD_CHERRY.getHolder().get();
		}
		else if (p_222911_) { return VineryConfiguredFeatures.OLD_CHERRY_VARIANT_WITH_BEE.getHolder().get(); }
		return VineryConfiguredFeatures.OLD_CHERRY_VARIANT.getHolder().get();
	}
}
