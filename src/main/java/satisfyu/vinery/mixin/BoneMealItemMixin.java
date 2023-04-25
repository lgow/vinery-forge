package satisfyu.vinery.mixin;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import satisfyu.vinery.item.WinemakerArmorItem;

@Mixin(BoneMealItem.class)
public abstract class BoneMealItemMixin {
	@Inject(method = "useOn", at = @At("RETURN"))
	public void useOnBlock(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
		if (cir.getReturnValue() == InteractionResult.CONSUME) {
			Player player = context.getPlayer();
			ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);
			ItemStack chestplate = player.getItemBySlot(EquipmentSlot.CHEST);
			ItemStack leggings = player.getItemBySlot(EquipmentSlot.LEGS);
			ItemStack boots = player.getItemBySlot(EquipmentSlot.FEET);
			if (helmet != null && helmet.getItem() instanceof WinemakerArmorItem && chestplate != null
					&& chestplate.getItem() instanceof WinemakerArmorItem && leggings != null
					&& leggings.getItem() instanceof WinemakerArmorItem && boots != null
					&& boots.getItem() instanceof WinemakerArmorItem) {
				context.getItemInHand().grow(1);
			}
		}
	}
}