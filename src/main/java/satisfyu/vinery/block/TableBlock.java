package satisfyu.vinery.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import satisfyu.vinery.util.VineryLineConnectingType;

public class TableBlock extends VineryLineConnectingBlock {
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	public static final VoxelShape TOP_SHAPE = Block.box(0.0, 13.0, 0.0, 16.0, 16.0, 16.0);

	public static final VoxelShape[] LEG_SHAPES = new VoxelShape[] {
			Block.box(1.0, 0.0, 1.0, 4.0, 13.0, 4.0), //north
			Block.box(12.0, 0.0, 1.0, 15.0, 13.0, 4.0), //east
			Block.box(12.0, 0.0, 12.0, 15.0, 13.0, 15.0), //south
			Block.box(1.0, 0.0, 12.0, 4.0, 13.0, 15.0) //west
	};

	public TableBlock(Properties settings) {
		super(settings);
		this.registerDefaultState(this.defaultBlockState().setValue(WATERLOGGED, false));
	}

	@Override
	public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
		Direction direction = pState.getValue(FACING);
		VineryLineConnectingType type = pState.getValue(LINE_CONNECTING_TYPE);
		if (type == VineryLineConnectingType.MIDDLE) {
			return TOP_SHAPE;
		}
		if ((direction == Direction.NORTH && type == VineryLineConnectingType.LEFT) || (direction == Direction.SOUTH
				&& type == VineryLineConnectingType.RIGHT)) {
			return Shapes.or(TOP_SHAPE, LEG_SHAPES[0], LEG_SHAPES[3]);
		}
		else if ((direction == Direction.NORTH && type == VineryLineConnectingType.RIGHT) || (
				direction == Direction.SOUTH && type == VineryLineConnectingType.LEFT)) {
			return Shapes.or(TOP_SHAPE, LEG_SHAPES[1], LEG_SHAPES[2]);
		}
		else if ((direction == Direction.EAST && type == VineryLineConnectingType.LEFT) || (direction == Direction.WEST
				&& type == VineryLineConnectingType.RIGHT)) {
			return Shapes.or(TOP_SHAPE, LEG_SHAPES[0], LEG_SHAPES[1]);
		}
		else if ((direction == Direction.EAST && type == VineryLineConnectingType.RIGHT) || (
				direction == Direction.WEST && type == VineryLineConnectingType.LEFT)) {
			return Shapes.or(TOP_SHAPE, LEG_SHAPES[2], LEG_SHAPES[3]);
		}
		return Shapes.or(TOP_SHAPE, LEG_SHAPES);
	}

	@Override
	public @Nullable BlockState getStateForPlacement(BlockPlaceContext pContext) {
		Level level = pContext.getLevel();
		BlockPos blockPos = pContext.getClickedPos();
		return super.getStateForPlacement(pContext).setValue(WATERLOGGED,
				level.getFluidState(blockPos).getFluidType() == Fluids.WATER.getFluidType());
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
		super.createBlockStateDefinition(pBuilder);
		pBuilder.add(WATERLOGGED);
	}

	@Override
	public FluidState getFluidState(BlockState pState) {
		return pState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(pState);
	}
}

