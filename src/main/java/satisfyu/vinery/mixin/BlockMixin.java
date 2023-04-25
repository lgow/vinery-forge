package satisfyu.vinery.mixin;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import satisfyu.vinery.Vinery;
import satisfyu.vinery.util.VineryTags;

@Mixin(Block.class)
public class BlockMixin {
	@Inject(method = "isExceptionForConnection", at = @At("HEAD"), cancellable = true)
	private static void checkCannotConnect(BlockState state, CallbackInfoReturnable<Boolean> cir) {
		if (state.is(VineryTags.Blocks.CAN_NOT_CONNECT)) { cir.setReturnValue(true); }
	}
}
