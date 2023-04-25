package satisfyu.vinery.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.Nullable;
import satisfyu.vinery.util.VineryLineConnectingType;

public class VineryLineConnectingBlock extends Block {
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

	public static final EnumProperty<VineryLineConnectingType> LINE_CONNECTING_TYPE = EnumProperty.create("type",
			VineryLineConnectingType.class);

	public VineryLineConnectingBlock(Properties pProperties) {
		super(pProperties);
		this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH)
				.setValue(LINE_CONNECTING_TYPE, VineryLineConnectingType.NONE));
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext pContext) {
		Direction facing = pContext.getHorizontalDirection();
		BlockState blockState = this.defaultBlockState().setValue(FACING, facing);
		Level level = pContext.getLevel();
		BlockPos clickedPos = pContext.getClickedPos();
		return switch (facing) {
			case EAST -> blockState.setValue(LINE_CONNECTING_TYPE,
					getType(blockState, level.getBlockState(clickedPos.south()),
							level.getBlockState(clickedPos.north())));
			case SOUTH -> blockState.setValue(LINE_CONNECTING_TYPE,
					getType(blockState, level.getBlockState(clickedPos.west()),
							level.getBlockState(clickedPos.east())));
			case WEST -> blockState.setValue(LINE_CONNECTING_TYPE,
					getType(blockState, level.getBlockState(clickedPos.north()),
							level.getBlockState(clickedPos.south())));
			default -> blockState.setValue(LINE_CONNECTING_TYPE,
					getType(blockState, level.getBlockState(clickedPos.east()),
							level.getBlockState(clickedPos.west())));
		};
	}

	@Override
	public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pBlock, BlockPos pFromPos, boolean pIsMoving) {
		if (pLevel.isClientSide) {
			return;
		}
		Direction facing = pState.getValue(FACING);
		VineryLineConnectingType type;
		switch (facing) {
			case EAST -> type = getType(pState, pLevel.getBlockState(pPos.south()), pLevel.getBlockState(pPos.north()));
			case SOUTH -> type = getType(pState, pLevel.getBlockState(pPos.west()), pLevel.getBlockState(pPos.east()));
			case WEST -> type = getType(pState, pLevel.getBlockState(pPos.north()), pLevel.getBlockState(pPos.south()));
			default -> type = getType(pState, pLevel.getBlockState(pPos.east()), pLevel.getBlockState(pPos.west()));
		}
		if (pState.getValue(LINE_CONNECTING_TYPE) != type) {
			pState = pState.setValue(LINE_CONNECTING_TYPE, type);
		}
		pLevel.setBlock(pPos, pState, 3);
	}

	private VineryLineConnectingType getType(BlockState state, BlockState left, BlockState right) {
		boolean shapeLeftSame = left.getBlock() == state.getBlock() && left.getValue(FACING) == state.getValue(FACING);
		boolean shapeRightSame = right.getBlock() == state.getBlock() && right.getValue(FACING) == state.getValue(
				FACING);
		if (shapeLeftSame && shapeRightSame) {
			return VineryLineConnectingType.MIDDLE;
		}
		else if (shapeLeftSame) {
			return VineryLineConnectingType.LEFT;
		}
		else if (shapeRightSame) {
			return VineryLineConnectingType.RIGHT;
		}
		return VineryLineConnectingType.NONE;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
		pBuilder.add(FACING, LINE_CONNECTING_TYPE);
	}

	@Override
	public BlockState rotate(BlockState state, LevelAccessor level, BlockPos pos, Rotation direction) {
		return state.setValue(FACING, direction.rotate(state.getValue(FACING)));
	}

	@Override
	public BlockState mirror(BlockState pState, Mirror pMirror) {
		return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
	}
}
