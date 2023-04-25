package satisfyu.vinery.block;

import com.mojang.datafixers.util.Pair;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import satisfyu.vinery.block.entity.FourWineBottleStorageBlockEntity;
import satisfyu.vinery.block.entity.StorageBlockEntity;
import satisfyu.vinery.item.DrinkBlockItem;
import satisfyu.vinery.util.VineryUtils;

import java.util.Optional;

public class FourBottleStorageBlock extends FacingBlock implements EntityBlock {
	public BlockEntity blockEntity;

	public FourBottleStorageBlock(Properties settings) {
		super(settings);
	}

	private static void add(Level level, BlockPos blockPos, Player player, StorageBlockEntity shelfBlockEntity, ItemStack itemStack, int i) {
		if (!level.isClientSide) {
			SoundEvent soundEvent = SoundEvents.WOOD_PLACE;
			shelfBlockEntity.setStack(i, itemStack.split(1));
			level.playSound(null, blockPos, soundEvent, SoundSource.BLOCKS, 1.0F, 1.0F);
			if (player.isCreative()) {
				itemStack.grow(1);
			}
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
		if (blockEntity instanceof FourWineBottleStorageBlockEntity shelfBlockEntity) {
			Optional<Pair<Float, Float>> optional = VineryUtils.getRelativeHitCoordinatesForBlockFace(hit,
					state.getValue(FacingBlock.FACING), unAllowedDirections());
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
		return 4;
	}

	public Direction[] unAllowedDirections() {
		return new Direction[] { Direction.DOWN, Direction.UP };
	}

	public boolean canInsertStack(ItemStack stack) {
		return stack.getItem() instanceof DrinkBlockItem;
	}

	public void setBlockEntity(BlockEntity blockEntity) {
		this.blockEntity = blockEntity;
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new FourWineBottleStorageBlockEntity(pos, state, size());
	}

	public int getSection(Float x, Float y) {
		if (x > 0.375 && x < 0.625) {
			if (y >= 0.55) {
				return 0;
			}
			else if (y <= 0.45) {
				return 3;
			}
		}
		else if (y > 0.35 && y < 0.65) {
			if (x < 0.4) {
				return 1;
			}
			else if (x > 0.65) {
				return 2;
			}
		}
		return Integer.MIN_VALUE;
	}
}
