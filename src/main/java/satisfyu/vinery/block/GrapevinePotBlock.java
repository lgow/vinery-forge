package satisfyu.vinery.block;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import satisfyu.vinery.item.GrapeItem;
import satisfyu.vinery.registry.VineryBlocks;
import satisfyu.vinery.registry.VinerySoundEvents;
import satisfyu.vinery.util.GrapevineType;

import java.util.List;

public class GrapevinePotBlock extends Block {
	private static final VoxelShape FILLING_SHAPE = Shapes.or(Block.box(15.0, 0.0, 0.0, 16.0, 10.0, 16.0),
			Block.box(0.0, 0.0, 0.0, 1.0, 10.0, 16.0), Block.box(1.0, 0.0, 0.0, 15.0, 10.0, 1.0),
			Block.box(1.0, 0.0, 15.0, 15.0, 10.0, 16.0), Block.box(0, 0, 0, 16.0, 6, 0.0),
			Block.box(0, 0, 16, 16, 6, 16), Block.box(16, 0, 0, 16, 6, 16), Block.box(0, 0, 0, 0, 5, 16),
			Block.box(1, 1, 1, 15, 1, 15));

	private static final VoxelShape SMASHING_SHAPE = Shapes.or(FILLING_SHAPE,
			Block.box(0.0, 0.0, 0.0, 16.0, 4.0, 16.0));

	private static final int MAX_STAGE = 6;

	private static final int MAX_STORAGE = 9;

	private static final IntegerProperty STAGE = IntegerProperty.create("stage", 0, MAX_STAGE);

	private static final IntegerProperty STORAGE = IntegerProperty.create("storage", 0, MAX_STORAGE);

	private static final int DECREMENT_PER_WINE_BOTTLE = 3;

	private static final EnumProperty<GrapevineType> GRAPEVINE_TYPE = EnumProperty.create("type", GrapevineType.class);

	public GrapevinePotBlock(Properties settings) {
		super(settings);
		this.registerDefaultState(this.defaultBlockState().setValue(STAGE, 0).setValue(STORAGE, 0));
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		if (state.getValue(STAGE) < 3) {
			return super.getCollisionShape(state, world, pos, context);
		}
		else {
			return SMASHING_SHAPE;
		}
	}

	@Override
	public void fallOn(Level world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
		super.fallOn(world, state, pos, entity, fallDistance);
		if (entity instanceof LivingEntity) {
			final int activeStage = state.getValue(STAGE);
			if (activeStage >= 3) {
				if (activeStage < MAX_STAGE) {
					world.setBlock(pos, state.setValue(STAGE, activeStage + 1), Block.UPDATE_ALL);
				}
				world.playSound(null, pos, VinerySoundEvents.BLOCK_GRAPEVINE_POT_SQUEEZE.get(), SoundSource.BLOCKS,
						1.0F, 1.0F);
			}
		}
	}

	private boolean canTakeWine(BlockState state, ItemStack stackInHand) {
		final int storage = state.getValue(STORAGE);
		final int stage = state.getValue(STAGE);
		if (canTakeWine(storage) && stage == MAX_STAGE) {
			return stackInHand.is(VineryBlocks.WINE_BOTTLE.get().asItem());
		}
		else {
			return false;
		}
	}

	private boolean canTakeWine(int storage) {
		return switch (storage) {
			case 3, 6, 9 -> true;
			default -> false;
		};
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		final ItemStack stack = player.getItemInHand(hand);
		if (state.getValue(STAGE) > 3 || state.getValue(STORAGE) >= MAX_STORAGE) {
			if (stack.getItem() instanceof GrapeItem) {
				return InteractionResult.PASS;
			}
		}
		if (stack.getItem() instanceof GrapeItem grape) {
			if (!player.isCreative()) { stack.shrink(1); }
			final int stage = state.getValue(STAGE);
			final int storage = state.getValue(STORAGE);
			boolean playSound = false;
			// Go to stage 1
			if (stage == 0) {
				world.setBlock(pos, this.defaultBlockState().setValue(STAGE, 1).setValue(STORAGE, 1)
						.setValue(GRAPEVINE_TYPE, grape.getType()), Block.UPDATE_ALL);
				playSound = true;
			}
			// Fill storage
			if (!isFilled(state)) {
				final BlockState newState = world.getBlockState(pos); // Possibly new state
				world.setBlock(pos, newState.setValue(STORAGE, storage + 1), Block.UPDATE_ALL);
				playSound = true;
			}
			// Try to update stage
			final BlockState newState = world.getBlockState(pos); // Updated state
			final int newStage = newState.getValue(STAGE);
			final int newStorage = newState.getValue(STORAGE);
			switch (newStorage) {
				case 3, 6, 9 -> {
					if (newStage < 3) {
						world.setBlock(pos, newState.setValue(STAGE, newStage + 1), Block.UPDATE_ALL);
					}
				}
			}
			if (playSound) {
				world.playSound(player, pos, SoundEvents.CORAL_BLOCK_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
			}
			return InteractionResult.SUCCESS;
		}
		else if (stack.is(VineryBlocks.WINE_BOTTLE.get().asItem())) {
			if (canTakeWine(state, stack)) {
				final ItemStack output = switch (state.getValue(GRAPEVINE_TYPE)) {
					case NONE, RED -> new ItemStack(VineryBlocks.RED_GRAPEJUICE_WINE_BOTTLE.get());
					case WHITE -> new ItemStack(VineryBlocks.WHITE_GRAPEJUICE_WINE_BOTTLE.get());
				};
				int storage = state.getValue(STORAGE);
				int newStorage = (storage - DECREMENT_PER_WINE_BOTTLE);
				if (newStorage == 0) {
					world.setBlock(pos, world.getBlockState(pos).setValue(STORAGE, 0).setValue(STAGE, 0),
							Block.UPDATE_ALL);
				}
				else {
					world.setBlock(pos, world.getBlockState(pos).setValue(STORAGE, newStorage), Block.UPDATE_ALL);
				}
				if (!player.isCreative()) { stack.shrink(1); }
				if (!player.getInventory().add(output)) {
					player.drop(output, false, false);
				}
				return InteractionResult.SUCCESS;
			}
		}
		return InteractionResult.PASS;
	}

	private boolean isFilled(BlockState state) {
		return state.getValue(STORAGE) >= MAX_STORAGE;
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

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(STAGE, STORAGE, GRAPEVINE_TYPE);
	}

	@Override
	public void appendHoverText(ItemStack itemStack, BlockGetter world, List<Component> tooltip, TooltipFlag tooltipContext) {
		tooltip.add(Component.translatable("block.vinery.grapevinepotblock.tooltip")
				.withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY));
	}
}

