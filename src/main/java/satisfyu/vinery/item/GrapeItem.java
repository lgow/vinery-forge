package satisfyu.vinery.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import satisfyu.vinery.registry.VineryItems;
import satisfyu.vinery.util.GrapevineType;

import java.util.List;
import java.util.Random;

/**
 * Namespace for grape items
 */
public class GrapeItem extends Item {
	private static final double CHANCE_OF_GETTING_SEEDS = 0.2;

	private final GrapevineType type;

	public GrapeItem(Properties settings, GrapevineType type) {
		super(settings);
		this.type = type;
	}

	public GrapevineType getType() {
		return type;
	}

	public void appendHoverText(ItemStack stack, @Nullable Level world, @NotNull List<Component> tooltip, TooltipFlag context) {
		tooltip.add(Component.translatable("item.vinery.grapevine.tooltip")
				.withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY));
	}

	public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving) {
		if (pEntityLiving instanceof Player player) {
			if (pStack.getItem() == this) {
				if (new Random().nextDouble() < CHANCE_OF_GETTING_SEEDS) {
					player.addItem(
							this.type == GrapevineType.WHITE ? VineryItems.WHITE_GRAPE_SEEDS.get().getDefaultInstance()
									: VineryItems.RED_GRAPE_SEEDS.get().getDefaultInstance());
				}
			}
		}
		return super.finishUsingItem(pStack, pLevel, pEntityLiving);
	}
}
