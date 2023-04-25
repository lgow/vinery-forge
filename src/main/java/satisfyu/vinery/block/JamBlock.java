package satisfyu.vinery.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class JamBlock extends StackableBlock {
	public JamBlock(Properties settings) {
		super(settings);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		VoxelShape shape = Shapes.empty();
		shape = Shapes.or(shape, Shapes.box(0.1875, 0, 0.1875, 0.8125, 0.875, 0.8125));
		return shape;
	}
	//    @Override
	//    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
	//        final ItemStack stack = player.getItemInHand(hand);
	//        if (stack.getItem() == this.asItem()) {
	//            if (state.getValue(STACK) < 3) {
	//                world.setBlock(pos, state.setValue(STACK, state.getValue(STACK) + 1), Block.UPDATE_ALL);
	//                if (!player.isCreative()) stack.shrink(1);
	//                return InteractionResult.SUCCESS;
	//            }
	//        }
	//        return super.use(state, world, pos, player, hand, hit);
	//    }
}
