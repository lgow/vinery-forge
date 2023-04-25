package satisfyu.vinery.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import satisfyu.vinery.block.GrapeBush;
import satisfyu.vinery.registry.VineryBlocks;
import satisfyu.vinery.registry.VineryItems;
import satisfyu.vinery.util.GrapevineType;

@Mixin(Fox.FoxEatBerriesGoal.class)
public abstract class FoxEntityEatSweetBerriesGoalMixin extends MoveToBlockGoal {

    /*@Final
    @Shadow
    //Fox field_17975;   // Synthetic field
     */

	public FoxEntityEatSweetBerriesGoalMixin(PathfinderMob mob, double speed, int range) {
		super(mob, speed, range);
	}

	private static ItemStack getGrapeFor(GrapevineType type) {
		return switch (type) {
			case NONE, RED -> new ItemStack(VineryBlocks.RED_GRAPE_BUSH.get());
			case WHITE -> new ItemStack(VineryItems.WHITE_GRAPE.get());
		};
	}

	@Inject(method = "isValidTarget", at = @At("HEAD"), cancellable = true)
	private void isTargetPos(LevelReader world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
		BlockState state = world.getBlockState(pos);
		if (state.getBlock() instanceof GrapeBush) {
			cir.setReturnValue(state.getValue(GrapeBush.AGE) >= 2);
		}
	}

	@Inject(method = "pickSweetBerries", at = @At("TAIL"))
	private void eatGrapes(CallbackInfo ci) {
		Fox f = (Fox) mob;
		final BlockState state = f.level.getBlockState(this.blockPos);
		if (state.getBlock() instanceof GrapeBush bush) {
			pickGrapes(state, bush.getType());
		}
	}

	private void pickGrapes(BlockState state, GrapevineType type) {
		Fox f = (Fox) mob;
		final int age = state.getValue(GrapeBush.AGE);
		state.setValue(GrapeBush.AGE, 1);
		int j = 1 + f.level.random.nextInt(2) + (age == 3 ? 1 : 0);
		ItemStack itemStack = f.getItemBySlot(EquipmentSlot.MAINHAND);
		ItemStack grape = getGrapeFor(type);
		if (itemStack.isEmpty()) {
			f.setItemSlot(EquipmentSlot.MAINHAND, grape);
			--j;
		}
		if (j > 0) {
			Block.popResource(f.level, this.blockPos, new ItemStack(grape.getItem(), j));
		}
		f.playSound(SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, 1.0F, 1.0F);
		f.level.setBlock(this.blockPos, state.setValue(GrapeBush.AGE, 1), 2);
	}
}
