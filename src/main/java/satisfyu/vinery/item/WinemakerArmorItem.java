package satisfyu.vinery.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import satisfyu.vinery.client.ClientModEvents;

import java.util.List;

public class WinemakerArmorItem extends ArmorItem {
	public WinemakerArmorItem(ArmorMaterial material, EquipmentSlot slot, Properties settings) {
		super(material, slot, settings);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level world, @NotNull List<Component> tooltip, TooltipFlag context) {
		if (world == null) { return; }
		if (!world.isClientSide()) { return; }
		ClientModEvents.appendToolTip(tooltip);
	}
}