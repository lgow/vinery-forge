package satisfyu.vinery.block;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.List;

public class ChenetBlock extends Block {
	public static final IntegerProperty STACK = IntegerProperty.create("stack", 1, 2);

	private static final VoxelShape SHAPE = Shapes.box(0.44375, 0.45, 0.44375, 0.55625, 0.675, 0.55625);

	public ChenetBlock(Properties settings) {
		super(settings);
		registerDefaultState(this.defaultBlockState().setValue(STACK, 1));
	}

	@Override
	@Nullable
	public BlockState getStateForPlacement(BlockPlaceContext pContext) {
		BlockState blockstate = pContext.getLevel().getBlockState(pContext.getClickedPos());
		if (blockstate.is(this)) {
			return blockstate.setValue(STACK, 2);
		}
		return super.getStateForPlacement(pContext);
	}

	@Override
	public boolean canBeReplaced(BlockState pState, BlockPlaceContext pUseContext) {
		return !pUseContext.isSecondaryUseActive() && pUseContext.getItemInHand().is(this.asItem()) && pState.getValue(
				STACK) < 2 || super.canBeReplaced(pState, pUseContext);
	}

	@Override
	public boolean propagatesSkylightDown(BlockState state, BlockGetter world, BlockPos pos) {
		return true;
	}

	public boolean skipRendering(BlockState state, BlockState stateFrom, Direction direction) {
		return stateFrom.is(this) || super.skipRendering(state, stateFrom, direction);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(STACK);
	}

	@Override
	public void appendHoverText(ItemStack itemStack, BlockGetter world, List<Component> tooltip, TooltipFlag tooltipContext) {
		tooltip.add(Component.translatable("block.vinery.breadblock.tooltip")
				.withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY));
	}
}

