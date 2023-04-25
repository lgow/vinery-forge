package satisfyu.vinery.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import satisfyu.vinery.util.GrapevineType;

public class SavannaGrapeBush extends GrapeBush {
	public SavannaGrapeBush(Properties settings, GrapevineType type) {
		super(settings, type);
	}

	@Override
	public boolean canGrowPlace(LevelReader pLevel, BlockPos pPos, BlockState pState) {
		return pLevel.getRawBrightness(pPos, 0) >= 14;
	}
}
