package satisfyu.vinery.block;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import satisfyu.vinery.block.entity.StoragePotBlockEntity;

import javax.annotation.Nullable;
import java.util.List;

public class StoragePotBlock extends BaseEntityBlock {
	public static final DirectionProperty FACING = BlockStateProperties.FACING;

	public static final BooleanProperty OPEN = BlockStateProperties.OPEN;

	private final SoundEvent openSound;

	private final SoundEvent closeSound;

	public StoragePotBlock(Properties settings, SoundEvent openSound, SoundEvent closeSound) {
		super(settings);
		this.openSound = openSound;
		this.closeSound = closeSound;
		this.registerDefaultState(
				this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(OPEN, Boolean.valueOf(false)));
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		VoxelShape shape = Shapes.empty();
		shape = Shapes.or(shape, Shapes.box(0.9375, 0, 0, 1, 0.625, 1));
		shape = Shapes.or(shape, Shapes.box(0, 0, 0, 0.0625, 0.625, 1));
		shape = Shapes.or(shape, Shapes.box(0.0625, 0, 0, 0.9375, 0.625, 0.0625));
		shape = Shapes.or(shape, Shapes.box(0.0625, 0, 0.9375, 0.9375, 0.625, 1));
		shape = Shapes.or(shape, Shapes.box(0.0625, 0, 0.0625, 0.9375, 0.0625, 0.9375));
		return shape;
	}

	public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
		BlockEntity blockentity = pLevel.getBlockEntity(pPos);
		if (blockentity instanceof StoragePotBlockEntity) {
			((StoragePotBlockEntity) blockentity).recheckOpen();
		}
	}

	public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
		if (pLevel.isClientSide) {
			return InteractionResult.SUCCESS;
		}
		else {
			BlockEntity blockentity = pLevel.getBlockEntity(pPos);
			if (blockentity instanceof StoragePotBlockEntity) {
				pPlayer.openMenu((StoragePotBlockEntity) blockentity);
				pPlayer.awardStat(Stats.OPEN_BARREL);
				PiglinAi.angerNearbyPiglins(pPlayer, true);
			}
			return InteractionResult.CONSUME;
		}
	}

	public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
		if (!pState.is(pNewState.getBlock())) {
			BlockEntity blockentity = pLevel.getBlockEntity(pPos);
			if (blockentity instanceof Container) {
				Containers.dropContents(pLevel, pPos, (Container) blockentity);
				pLevel.updateNeighbourForOutputSignal(pPos, this);
			}
			super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
		}
	}

	@Nullable
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
		return new StoragePotBlockEntity(pPos, pState);
	}

	public RenderShape getRenderShape(BlockState pState) {
		return RenderShape.MODEL;
	}

	public void playSound(Level world, BlockPos pos, boolean open) {
		world.playSound(null, pos, open ? openSound : closeSound, SoundSource.BLOCKS, 1.0f, 1.0f);
	}

	public void appendHoverText(ItemStack itemStack, BlockGetter world, List<Component> tooltip, TooltipFlag tooltipContext) {
		if (Screen.hasShiftDown()) {
			tooltip.add(Component.translatable("block.vinery.storage.tooltip.shift")
					.withStyle(ChatFormatting.LIGHT_PURPLE, ChatFormatting.ITALIC));
		}
	}

	public boolean hasAnalogOutputSignal(BlockState pState) {
		return true;
	}

	public int getAnalogOutputSignal(BlockState pBlockState, Level pLevel, BlockPos pPos) {
		return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(pLevel.getBlockEntity(pPos));
	}

	public BlockState rotate(BlockState pState, Rotation pRotation) {
		return pState.setValue(FACING, pRotation.rotate(pState.getValue(FACING)));
	}

	public BlockState mirror(BlockState pState, Mirror pMirror) {
		return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
	}

	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
		pBuilder.add(FACING, OPEN);
	}

	public BlockState getStateForPlacement(BlockPlaceContext pContext) {
		return this.defaultBlockState().setValue(FACING, pContext.getNearestLookingDirection().getOpposite());
	}
}



