package satisfyu.vinery.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import satisfyu.vinery.registry.VineryBlockEntityTypes;

public class FourWineBottleStorageBlockEntity extends StorageBlockEntity {
	public FourWineBottleStorageBlockEntity(BlockPos pos, BlockState state) {
		super(VineryBlockEntityTypes.FOUR_WINE_RACK_ENTITY.get(), pos, state);
	}

	public FourWineBottleStorageBlockEntity(BlockPos pos, BlockState state, int size) {
		super(VineryBlockEntityTypes.FOUR_WINE_RACK_ENTITY.get(), pos, state);
		this.size = size;
		this.inventory = NonNullList.withSize(this.size, ItemStack.EMPTY);
	}
}
