package satisfyu.vinery.block;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import satisfyu.vinery.block.entity.WineBoxEntity;

import java.util.List;

public class WineBoxBlock extends WineRackBlock {
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

	public static final BooleanProperty OPEN = BlockStateProperties.OPEN;

	public static final BooleanProperty HAS_BOTTLE = BooleanProperty.create("has_bottle");

	private static final VoxelShape SHAPE_S = makeShapeS();

	private static final VoxelShape SHAPE_E = makeShapeE();

	public WineBoxBlock(Properties settings) {
		super(settings, 1, 0);
		this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH).setValue(OPEN, false)
				.setValue(HAS_BOTTLE, false));
	}

	private static VoxelShape makeShapeS() {
		VoxelShape shape = Shapes.empty();
		shape = Shapes.joinUnoptimized(shape, Shapes.box(0.0625, 0, 0.3125, 0.09375, 0.3125, 0.6875), BooleanOp.OR);
		shape = Shapes.joinUnoptimized(shape, Shapes.box(0.90625, 0, 0.3125, 0.9375, 0.3125, 0.6875), BooleanOp.OR);
		shape = Shapes.joinUnoptimized(shape, Shapes.box(0.09375, 0, 0.3125, 0.90625, 0.3125, 0.34375), BooleanOp.OR);
		shape = Shapes.joinUnoptimized(shape, Shapes.box(0.09375, 0.03125, 0.34375, 0.90625, 0.09375, 0.65625),
				BooleanOp.OR);
		shape = Shapes.joinUnoptimized(shape, Shapes.box(0.09375, 0, 0.65625, 0.90625, 0.3125, 0.6875), BooleanOp.OR);
		return shape;
	}

	private static VoxelShape makeShapeE() {
		VoxelShape shape = Shapes.empty();
		shape = Shapes.joinUnoptimized(shape, Shapes.box(0.3125, 0, 0.0625, 0.6875, 0.3125, 0.09375), BooleanOp.OR);
		shape = Shapes.joinUnoptimized(shape, Shapes.box(0.3125, 0, 0.90625, 0.6875, 0.3125, 0.9375), BooleanOp.OR);
		shape = Shapes.joinUnoptimized(shape, Shapes.box(0.65625, 0, 0.09375, 0.6875, 0.3125, 0.90625), BooleanOp.OR);
		shape = Shapes.joinUnoptimized(shape, Shapes.box(0.34375, 0.03125, 0.09375, 0.65625, 0.09375, 0.90625),
				BooleanOp.OR);
		shape = Shapes.joinUnoptimized(shape, Shapes.box(0.3125, 0, 0.09375, 0.34375, 0.3125, 0.90625), BooleanOp.OR);
		return shape;
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (world.isClientSide) { return InteractionResult.SUCCESS; }
		final ItemStack stack = player.getItemInHand(hand);
		if (player.isShiftKeyDown() && stack.isEmpty()) {
			world.setBlock(pos, state.setValue(OPEN, !state.getValue(OPEN)), UPDATE_ALL);
		}
		else if (state.getValue(OPEN) && !player.isShiftKeyDown() && stack.isEmpty()) {
			WineBoxEntity blockEntity = (WineBoxEntity) world.getBlockEntity(pos);
			if (blockEntity != null && blockEntity.getNonEmptySlotCount() > 0) {
				player.setItemInHand(hand, blockEntity.getFirstNonEmptyStack().copy());
				blockEntity.removeFirstNonEmptyStack();
				((ServerPlayer) player).connection.send(blockEntity.getUpdatePacket());
				return InteractionResult.SUCCESS;
			}
		}
		else if (state.getValue(OPEN) && !player.isShiftKeyDown() && !stack.isEmpty()) {
			return super.use(state, world, pos, player, hand, hit);
		}
		return InteractionResult.PASS;
	}

	@Override
	public boolean propagatesSkylightDown(BlockState state, BlockGetter world, BlockPos pos) {
		return true;
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, OPEN, HAS_BOTTLE);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return switch (state.getValue(FACING)) {
			case WEST, EAST -> SHAPE_E;
			default -> SHAPE_S;
		};
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rotation) {
		return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
	}

	@Override
	public BlockState mirror(BlockState state, Mirror mirror) {
		return state.rotate(mirror.getRotation(state.getValue(FACING)));
	}

	@Override
	public void appendHoverText(ItemStack itemStack, BlockGetter world, List<Component> tooltip, TooltipFlag tooltipContext) {
		tooltip.add(Component.translatable("block.vinery.canbeplaced.tooltip")
				.withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY));
		if (Screen.hasShiftDown()) {
			tooltip.add(Component.translatable("block.vinery.winebox.tooltip.shift_1"));
			tooltip.add(Component.translatable("block.vinery.winebox.tooltip.shift_2"));
			tooltip.add(Component.translatable("block.vinery.winebox.tooltip.shift_3"));
		}
		else {
			tooltip.add(Component.translatable("block.vinery.breadblock.tooltip.tooltip_shift"));
		}
	}
}