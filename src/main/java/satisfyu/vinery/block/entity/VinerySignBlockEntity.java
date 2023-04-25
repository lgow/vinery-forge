package satisfyu.vinery.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import satisfyu.vinery.registry.VineryBlockEntityTypes;

public class VinerySignBlockEntity extends SignBlockEntity {
	public VinerySignBlockEntity(BlockPos pPos, BlockState pBlockState) {
		super(pPos, pBlockState);
	}

	@Override
	public BlockEntityType<?> getType() {
		return VineryBlockEntityTypes.VINERY_SIGN_BLOCK_ENTITY.get();
	}
}
