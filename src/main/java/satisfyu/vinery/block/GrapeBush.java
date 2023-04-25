package satisfyu.vinery.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import satisfyu.vinery.registry.VineryItems;
import satisfyu.vinery.util.GrapevineType;

public class GrapeBush extends BushBlock implements BonemealableBlock {
	public static final IntegerProperty AGE = BlockStateProperties.AGE_3;

	private static final VoxelShape SMALL_SHAPE = Block.box(3.0, 0.0, 3.0, 13.0, 8.0, 13.0);

	private static final VoxelShape LARGE_SHAPE = Block.box(1.0, 0.0, 1.0, 15.0, 16.0, 15.0);

	public final GrapevineType type;

	private final int chance;

	public GrapeBush(Properties pProperties, GrapevineType type) {
		this(pProperties, type, 5);
	}

	public GrapeBush(Properties settings, GrapevineType type, int chance) {
		super(settings);
		this.chance = chance;
		this.type = type;
	}

	@Override
	public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
		if (pState.getValue(AGE) == 0) {
			return SMALL_SHAPE;
		}
		else {
			return pState.getValue(AGE) < 3 ? LARGE_SHAPE : super.getShape(pState, pLevel, pPos, pContext);
		}
	}

	public GrapevineType getType() {
		return type;
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		int i = state.getValue(AGE);
		boolean bl = i == 3;
		if (!bl && player.getItemInHand(hand).is(Items.BONE_MEAL)) {
			return InteractionResult.PASS;
		}
		else if (i > 1) {
			int x = world.random.nextInt(2);
			popResource(world, pos, new ItemStack(
					this.type == GrapevineType.RED ? VineryItems.RED_GRAPE.get() : VineryItems.WHITE_GRAPE.get(),
					x + (bl ? 1 : 0)));
			world.playSound(null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F,
					0.8F + world.random.nextFloat() * 0.4F);
			world.setBlock(pos, state.setValue(AGE, 1), 2);
			return InteractionResult.sidedSuccess(world.isClientSide);
		}
		else {
			return super.use(state, world, pos, player, hand, hit);
		}
	}

	@Override
	public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
		int i = pState.getValue(AGE);
		if (i < 3 && pRandom.nextInt(chance) == 0 && canGrowPlace(pLevel, pPos, pState)) {
			BlockState blockState = pState.setValue(AGE, Integer.valueOf(i + 1));
			pLevel.setBlock(pPos, blockState, 2);
			pLevel.gameEvent(GameEvent.BLOCK_CHANGE, pPos, GameEvent.Context.of(blockState));
		}
	}

	@Override
	public boolean isRandomlyTicking(BlockState pState) {
		return pState.getValue(AGE) < 3;
	}

	@Override
	public boolean isBonemealSuccess(Level pLevel, RandomSource pRandom, BlockPos pPos, BlockState pState) {
		return true;
	}

	public boolean canGrowPlace(LevelReader pLevel, BlockPos pPos, BlockState pState) {
		return pLevel.getRawBrightness(pPos, 0) > 9;
	}

	@Override
	public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
		return canGrowPlace(pLevel, pPos, pState) && this.mayPlaceOn(pLevel.getBlockState(pPos.below()), pLevel, pPos);
	}

	@Override
	protected boolean mayPlaceOn(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
		return pState.isSolidRender(pLevel, pPos);
	}

	@Override
	public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
		return switch (this.type) {
			case NONE, RED -> new ItemStack(VineryItems.RED_GRAPE_SEEDS.get());
			case WHITE -> new ItemStack(VineryItems.WHITE_GRAPE_SEEDS.get());
		};
	}

	@Override
	public boolean isValidBonemealTarget(BlockGetter pLevel, BlockPos pPos, BlockState pState, boolean pIsClient) {
		return pState.getValue(AGE) < 3;
	}

	@Override
	public void performBonemeal(ServerLevel pLevel, RandomSource pRandom, BlockPos pPos, BlockState pState) {
		int i = Math.min(3, pState.getValue(AGE) + 1);
		pLevel.setBlock(pPos, pState.setValue(AGE, i), 2);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
		pBuilder.add(AGE);
	}
}
