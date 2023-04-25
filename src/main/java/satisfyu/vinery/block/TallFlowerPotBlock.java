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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import satisfyu.vinery.block.entity.FlowerBoxBlockEntity;
import satisfyu.vinery.block.entity.TallFlowerPotBlockEntity;
import satisfyu.vinery.registry.VineryBlockEntityTypes;
import satisfyu.vinery.util.VineryUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class TallFlowerPotBlock extends FacingBlock implements EntityBlock {
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final Supplier<VoxelShape> voxelShapeSupplier = () -> {
		VoxelShape shape = Shapes.empty();
		shape = Shapes.joinUnoptimized(shape, Shapes.box(0.78125, 0.421875, 0.21875, 0.875, 0.609375, 0.78125),
				BooleanOp.OR);
		shape = Shapes.joinUnoptimized(shape, Shapes.box(0.21875, 0, 0.21875, 0.78125, 0.46875, 0.78125), BooleanOp.OR);
		shape = Shapes.joinUnoptimized(shape, Shapes.box(0.125, 0.421875, 0.125, 0.875, 0.609375, 0.21875),
				BooleanOp.OR);
		shape = Shapes.joinUnoptimized(shape, Shapes.box(0.125, 0.421875, 0.78125, 0.875, 0.609375, 0.875),
				BooleanOp.OR);
		shape = Shapes.joinUnoptimized(shape, Shapes.box(0.125, 0.421875, 0.21875, 0.21875, 0.609375, 0.78125),
				BooleanOp.OR);
		return shape;
	};

	public static final Map<Direction, VoxelShape> SHAPE = Util.make(new HashMap<>(), map -> {
		for (Direction direction : Direction.Plane.HORIZONTAL.stream().toList()) {
			map.put(direction, VineryUtils.rotateShape(Direction.NORTH, direction, voxelShapeSupplier.get()));
		}
	});

	public TallFlowerPotBlock(Properties settings) {
		super(settings);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return SHAPE.get(state.getValue(FACING));
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (world.isClientSide() || hand == InteractionHand.OFF_HAND) return InteractionResult.SUCCESS;
		if (world.getBlockEntity(pos) instanceof TallFlowerPotBlockEntity pot){
			if (player.isCrouching()) { return InteractionResult.PASS; }
			ItemStack handStack = player.getItemInHand(hand);
			Item flower = pot.getFlower().getItem();
			if (handStack.isEmpty()) {
					player.addItem(flower.getDefaultInstance());
					pot.removeFlower();
					return InteractionResult.SUCCESS;
			}
			else if (handStack.is(ItemTags.TALL_FLOWERS)) {
				pot.addFlower(handStack.getItem().getDefaultInstance());
				if (!player.isCreative()) {
					handStack.shrink(1);
				}
				return InteractionResult.SUCCESS;
			}
		}
		return super.use(state, world, pos, player, hand, hit);
	}

	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
		return direction == Direction.DOWN && !state.canSurvive(world, pos) ? Blocks.AIR.defaultBlockState()
				: super.updateShape(state, direction, neighborState, world, pos, neighborPos);
	}

	@Override
	public boolean isPathfindable(BlockState state, BlockGetter world, BlockPos pos, PathComputationType type) {
		return false;
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		List<ItemStack> list = new ArrayList<>();
		list.add(new ItemStack(this));
		return list;
	}

	@Override
	public PushReaction getPistonPushReaction(BlockState pState) {
		return PushReaction.IGNORE;
	}

	@Override
	public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
		if (pState.getBlock() != pNewState.getBlock()) {
			if (pLevel.getBlockEntity(pPos) instanceof TallFlowerPotBlockEntity entity) {
				entity.drops();
				pLevel.updateNeighbourForOutputSignal(pPos, pNewState.getBlock());
			}
			super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
		}
	}

	@Nullable
	protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> pServerType, BlockEntityType<E> pClientType, BlockEntityTicker<? super E> pTicker) {
		return pClientType == pServerType ? (BlockEntityTicker<A>) pTicker : null;
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
		return new TallFlowerPotBlockEntity(pPos, pState);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, VineryBlockEntityTypes.TALL_FLOWER_POT_ENTITY.get(),
				(pLevel, pPos, pState, pBlockEntity) -> {
					TallFlowerPotBlockEntity blockEntity = ((TallFlowerPotBlockEntity) pLevel.getBlockEntity(pPos));
					if (blockEntity != null) {
						blockEntity.markUpdated();
					}
				});
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	@Override
	public void appendHoverText(ItemStack itemStack, BlockGetter world, List<Component> tooltip, TooltipFlag tooltipContext) {
		tooltip.add(Component.translatable("block.vinery.canbeplaced.tooltip")
				.withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY));
	}
}