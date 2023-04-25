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
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import satisfyu.vinery.block.entity.FourWineBottleStorageBlockEntity;
import satisfyu.vinery.block.entity.NineWineBottleStorageBlockEntity;
import satisfyu.vinery.block.entity.WineBoxEntity;
import satisfyu.vinery.item.DrinkBlockItem;
import satisfyu.vinery.registry.VineryBlocks;

import java.util.ArrayList;
import java.util.List;

public class WineRackBlock extends BaseEntityBlock {
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

	protected final int maxStorage;

	private final int modelPostFix;

	public WineRackBlock(Properties settings, int maxStorage, int modelPostFix) {
		super(settings);
		this.maxStorage = maxStorage;
		this.modelPostFix = modelPostFix;
		this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH));
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (world.isClientSide) { return InteractionResult.SUCCESS; }
		WineBoxEntity blockEntity = (WineBoxEntity) world.getBlockEntity(pos);
		if (blockEntity != null) {
			if(maxStorage == 9){
				world.setBlockAndUpdate(pos, VineryBlocks.NINE_BOTTLE_STORAGE.get().defaultBlockState());
				if(world.getBlockEntity(pos) instanceof NineWineBottleStorageBlockEntity nineEntity){
					nineEntity.setInventory(blockEntity.getInvStackList());
				}
			} else if(maxStorage == 4){
				world.setBlockAndUpdate(pos, VineryBlocks.FOUR_BOTTLE_STORAGE.get().defaultBlockState());
				if(world.getBlockEntity(pos) instanceof FourWineBottleStorageBlockEntity fourEntity){
					fourEntity.setInventory(blockEntity.getInvStackList());
				}
			}
		}
		return super.use(state, world, pos, player, hand, hit);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		List<ItemStack> list = new ArrayList<>();
		list.add(new ItemStack(this.asItem()));
		WineBoxEntity blockEntity = (WineBoxEntity) builder.getParameter(LootContextParams.BLOCK_ENTITY);
		if (blockEntity != null) {
			list.addAll(blockEntity.getInvStackList());
		}
		return list;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rotation) {
		return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
	}

	@Override
	public BlockState mirror(BlockState state, Mirror mirror) {
		return state.rotate(mirror.getRotation(state.getValue(FACING)));
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new WineBoxEntity(pos, state);
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	public int getModelPostFix() {
		return modelPostFix;
	}

	@Override
	public void appendHoverText(ItemStack itemStack, BlockGetter world, List<Component> tooltip, TooltipFlag tooltipContext) {
		tooltip.add(Component.translatable("block.vinery.winerack.tooltip.shift_1")
				.withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY));
		if (Screen.hasShiftDown()) {
			tooltip.add(Component.translatable("block.vinery.winerack.tooltip.shift_2"));
		}
		else {
			tooltip.add(Component.translatable("block.vinery.breadblock.tooltip.tooltip_shift"));
		}
	}
}