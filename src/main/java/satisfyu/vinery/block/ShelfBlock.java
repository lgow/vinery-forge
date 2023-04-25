package satisfyu.vinery.block;

import com.mojang.datafixers.util.Pair;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import satisfyu.vinery.block.entity.ShelfEntity;
import satisfyu.vinery.block.entity.StorageBlockEntity;
import satisfyu.vinery.util.VineryTags;
import satisfyu.vinery.util.VineryUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class ShelfBlock extends FacingBlock implements EntityBlock {
	private static final Supplier<VoxelShape> voxelShapeSupplier = () -> {
		VoxelShape shape = Shapes.empty();
		shape = Shapes.joinUnoptimized(shape, Shapes.box(0, 0.25, 0.5, 1, 0.3125, 1), BooleanOp.OR);
		shape = Shapes.joinUnoptimized(shape, Shapes.box(0.125, 0.1875, 0.625, 0.1875, 0.25, 0.9375), BooleanOp.OR);
		shape = Shapes.joinUnoptimized(shape, Shapes.box(0.8125, 0.1875, 0.625, 0.875, 0.25, 0.9375), BooleanOp.OR);
		shape = Shapes.joinUnoptimized(shape, Shapes.box(0.125, 0.0625, 0.9375, 0.1875, 0.25, 1), BooleanOp.OR);
		shape = Shapes.joinUnoptimized(shape, Shapes.box(0.8125, 0.0625, 0.9375, 0.875, 0.25, 1), BooleanOp.OR);
		return shape;
	};

	public static final Map<Direction, VoxelShape> SHAPE = Util.make(new HashMap<>(), map -> {
		for (Direction direction : Direction.Plane.HORIZONTAL) {
			map.put(direction, VineryUtils.rotateShape(Direction.NORTH, direction, voxelShapeSupplier.get()));
		}
	});

	public BlockEntity blockEntity;

	public ShelfBlock(Properties settings) {
		super(settings);
	}

	private static void add(Level level, BlockPos blockPos, Player player, StorageBlockEntity shelfBlockEntity, ItemStack itemStack, int i) {
		if (!level.isClientSide) {
			SoundEvent soundEvent = SoundEvents.WOOD_PLACE;
			shelfBlockEntity.setStack(i, itemStack.split(1));
			level.playSound(null, blockPos, soundEvent, SoundSource.BLOCKS, 1.0F, 1.0F);
			if (!player.isCreative()) { itemStack.shrink(1); }
			level.gameEvent(player, GameEvent.BLOCK_CHANGE, blockPos);
		}
	}

	private static void remove(Level level, BlockPos blockPos, Player player, StorageBlockEntity shelfBlockEntity, int i) {
		if (!level.isClientSide) {
			ItemStack itemStack = shelfBlockEntity.removeStack(i);
			SoundEvent soundEvent = SoundEvents.WOOD_PLACE;
			level.playSound(null, blockPos, soundEvent, SoundSource.BLOCKS, 1.0F, 1.0F);
			if (!player.getInventory().add(itemStack)) {
				player.drop(itemStack, false);
			}
			level.gameEvent(player, GameEvent.BLOCK_CHANGE, blockPos);
		}
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof ShelfEntity shelfBlockEntity) {
			Optional<Pair<Float, Float>> optional = VineryUtils.getRelativeHitCoordinatesForBlockFace(hit,
					state.getValue(FACING), unAllowedDirections());
			if (optional.isEmpty()) {
				return InteractionResult.PASS;
			}
			else {
				Pair<Float, Float> ff = optional.get();
				int i = getSection(ff.getFirst(), ff.getSecond());
				if (i == Integer.MIN_VALUE) { return InteractionResult.PASS; }
				if (!shelfBlockEntity.getInventory().get(i).isEmpty()) {
					remove(world, pos, player, shelfBlockEntity, i);
					return InteractionResult.sidedSuccess(world.isClientSide);
				}
				else {
					ItemStack stack = player.getItemInHand(hand);
					if (!stack.isEmpty() && canInsertStack(stack)) {
						add(world, pos, player, shelfBlockEntity, stack, i);
						return InteractionResult.sidedSuccess(world.isClientSide);
					}
					else {
						return InteractionResult.CONSUME;
					}
				}
			}
		}
		else {
			return InteractionResult.PASS;
		}
	}

	@Override
	public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
		if (!state.is(newState.getBlock())) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof StorageBlockEntity shelf) {
				if (world instanceof ServerLevel) {
					Containers.dropContents(world, pos, shelf.getInventory());
				}
				world.updateNeighbourForOutputSignal(pos, this);
			}
			super.onRemove(state, world, pos, newState, moved);
		}
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	public int size() {
		return 9;
	}

	public Direction[] unAllowedDirections() {
		return new Direction[] { Direction.DOWN };
	}

	public boolean canInsertStack(ItemStack stack) {
		return !(stack.getItem() instanceof BlockItem) || stack.is(VineryTags.Items.IGNORE_BLOCK_ITEM);
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new ShelfEntity(pos, state, size());
	}

	public int getSection(Float f, Float y) {
		int nSection;
		float oneS = (float) 1 / 9;
		if (f < oneS) {
			nSection = 0;
		}
		else if (f < oneS * 2) {
			nSection = 1;
		}
		else if (f < oneS * 3) {
			nSection = 2;
		}
		else if (f < oneS * 4) {
			nSection = 3;
		}
		else if (f < oneS * 5) {
			nSection = 4;
		}
		else if (f < oneS * 6) {
			nSection = 5;
		}
		else if (f < oneS * 7) {
			nSection = 6;
		}
		else if (f < oneS * 8) {
			nSection = 7;
		}
		else { nSection = 8; }
		return 8 - nSection;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return SHAPE.get(state.getValue(FACING));
	}
}
