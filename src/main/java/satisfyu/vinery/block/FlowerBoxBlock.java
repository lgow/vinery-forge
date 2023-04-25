package satisfyu.vinery.block;

import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import satisfyu.vinery.block.entity.FlowerBoxBlockEntity;
import satisfyu.vinery.registry.VineryBlockEntityTypes;
import satisfyu.vinery.util.VineryUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class FlowerBoxBlock extends FacingBlock implements EntityBlock {
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

	public static final Supplier<VoxelShape> voxelShapeSupplier = () -> {
		VoxelShape shape = Shapes.empty();
		shape = Shapes.join(shape, Shapes.box(0.9375, 0, 0.5625, 1, 0.375, 1), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0, 0, 0.5625, 0.0625, 0.375, 1), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.0625, 0, 0.5625, 0.9375, 0.375, 0.625), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.0625, 0, 0.9375, 0.9375, 0.375, 1), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.0625, 0, 0.625, 0.9375, 0.3125, 0.9375), BooleanOp.OR);
		return shape;
	};

	public static final Map<Direction, VoxelShape> SHAPE = Util.make(new HashMap<>(), map -> {
		for (Direction direction : Direction.Plane.HORIZONTAL.stream().toList()) {
			map.put(direction, VineryUtils.rotateShape(Direction.NORTH, direction, voxelShapeSupplier.get()));
		}
	});

	private int index = 1;

	public FlowerBoxBlock(Block content, Properties settings) {
		super(settings);
	}

	@Nullable
	protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> pServerType, BlockEntityType<E> pClientType, BlockEntityTicker<? super E> pTicker) {
		return pClientType == pServerType ? (BlockEntityTicker<A>) pTicker : null;
	}

	@Override
	public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
		return SHAPE.get(pState.getValue(FACING));
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (world.isClientSide) {
			return InteractionResult.SUCCESS;
		}
		FlowerBoxBlockEntity blockEntity = (FlowerBoxBlockEntity) world.getBlockEntity(pos);
		if (blockEntity == null || player.isCrouching()) {
			return InteractionResult.PASS;
		}
		Direction facing = state.getValue(FACING);
		boolean left = (facing.getAxis() == Direction.Axis.X) ? (hit.getLocation().z - pos.getZ() > 0.5D)
				: (hit.getLocation().x - pos.getX() > 0.5D);
		left = (facing == Direction.NORTH || facing == Direction.WEST) != left;
		ItemStack handStack = player.getItemInHand(hand);
		boolean hasSmallFlower = handStack.is(ItemTags.SMALL_FLOWERS);
		if (handStack.isEmpty()) {
			if (blockEntity.hasTwoFlowers()) {
				ItemStack[] itemStacks = blockEntity.removeAllFlowers();
				player.addItem(itemStacks[0]);
				player.addItem(itemStacks[1]);
			}
			else {
				ItemStack flowerStack = blockEntity.removeFlower(left ? 0 : 1);
				if (!flowerStack.isEmpty()) {
					player.addItem(flowerStack);
					return InteractionResult.SUCCESS;
				}
				flowerStack = blockEntity.removeFlower(left ? 1 : 0);
				if (!flowerStack.isEmpty()) {
					player.addItem(flowerStack);
					return InteractionResult.SUCCESS;
				}
			}
		}
		else if (hasSmallFlower) {
			if (blockEntity.isSlotEmpty(left ? 0 : 1)) {
				blockEntity.flowers.setStackInSlot(left ? 0 : 1, new ItemStack(handStack.getItem()));
				blockEntity.addFlower(new ItemStack(handStack.getItem()), left ? 0 : 1, player);
				if (!player.isCreative()) { handStack.shrink(1); }
				return InteractionResult.SUCCESS;
			}
			if (blockEntity.isSlotEmpty(left ? 1 : 0)) {
				blockEntity.flowers.setStackInSlot(left ? 1 : 0, new ItemStack(handStack.getItem()));
				blockEntity.addFlower(new ItemStack(handStack.getItem()), left ? 1 : 0, player);
				handStack.shrink(1);
				return InteractionResult.SUCCESS;
			}
		}
		return super.use(state, world, pos, player, hand, hit);
	}

	private void switchIndex() {
		if (index == 1) {
			index = 2;
		}
		if (index == 2) {
			index = 1;
		}
	}

	@Override
	public PushReaction getPistonPushReaction(BlockState pState) {
		return PushReaction.IGNORE;
	}

	@Override
	public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
		if (pState.getBlock() != pNewState.getBlock()) {
			if (pLevel.getBlockEntity(pPos) instanceof FlowerBoxBlockEntity entity) {
				entity.drops();
				pLevel.updateNeighbourForOutputSignal(pPos, pNewState.getBlock());
			}
			super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
		}
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
		return new FlowerBoxBlockEntity(pPos, pState);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, VineryBlockEntityTypes.FLOWER_BOX_ENTITY.get(),
				(pLevel, pPos, pState, pBlockEntity) -> {
					FlowerBoxBlockEntity flowerBoxBlockEntity = ((FlowerBoxBlockEntity) pLevel.getBlockEntity(pPos));
					if (flowerBoxBlockEntity != null) {
						flowerBoxBlockEntity.markUpdated();
					}
				});
	}

	@Override
	public void appendHoverText(ItemStack pStack, @Nullable BlockGetter pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
		pTooltip.add(Component.translatable("block.vinery.canbeplaced.tooltip")
				.withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY));
	}
}