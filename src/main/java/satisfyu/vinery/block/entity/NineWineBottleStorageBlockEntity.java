package satisfyu.vinery.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import satisfyu.vinery.registry.VineryBlockEntityTypes;

public class NineWineBottleStorageBlockEntity extends StorageBlockEntity {
	public NineWineBottleStorageBlockEntity(BlockPos pos, BlockState state) {
		super(VineryBlockEntityTypes.NINE_WINE_RACK_ENTITY.get(), pos, state);
	}

	public NineWineBottleStorageBlockEntity(BlockPos pos, BlockState state, int size) {
		super(VineryBlockEntityTypes.NINE_WINE_RACK_ENTITY.get(), pos, state);
		this.size = size;
		this.inventory = NonNullList.withSize(this.size, ItemStack.EMPTY);
	}
}
