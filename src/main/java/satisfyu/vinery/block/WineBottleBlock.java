package satisfyu.vinery.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class WineBottleBlock extends StackableBlock {
	public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;

	private static final VoxelShape SHAPE = Shapes.box(0.125, 0, 0.125, 0.875, 0.875, 0.875);

	public WineBottleBlock(Properties settings) {
		super(settings);
		registerDefaultState(this.defaultBlockState().setValue(STACK, 1).setValue(FACING, Direction.NORTH));
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

	@Override
	@javax.annotation.Nullable
	public BlockState getStateForPlacement(BlockPlaceContext pContext) {
		BlockState blockstate = pContext.getLevel().getBlockState(pContext.getClickedPos());
		if (blockstate.is(this)) {
			return blockstate.setValue(STACK, Integer.valueOf(Math.min(3, blockstate.getValue(STACK) + 1)));
		}
		return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
	}
}
