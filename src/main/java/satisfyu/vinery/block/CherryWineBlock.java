package satisfyu.vinery.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CherryWineBlock extends WineBottleBlock {
	public CherryWineBlock(Properties settings) {
		super(settings);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, STACK);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		VoxelShape shape = Shapes.empty();
		shape = Shapes.or(shape, Shapes.box(0.1875, 0, 0.1875, 0.8125, 0.875, 0.8125));
		return shape;
	}
}
