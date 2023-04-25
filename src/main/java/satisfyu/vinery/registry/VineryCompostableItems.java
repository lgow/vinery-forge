package satisfyu.vinery.registry;

import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.ComposterBlock;

public class VineryCompostableItems {
	public static void init() {
		registerCompostableItem(VineryItems.WHITE_GRAPE.get(), 0.4F);
		registerCompostableItem(VineryItems.WHITE_GRAPE_SEEDS.get(), 0.4F);
		registerCompostableItem(VineryItems.RED_GRAPE.get(), 0.4F);
		registerCompostableItem(VineryItems.RED_GRAPE_SEEDS.get(), 0.4F);
		registerCompostableItem(VineryBlocks.CHERRY_LEAVES.get(), 0.4F);
		registerCompostableItem(VineryBlocks.GRAPEVINE_LEAVES.get(), 0.4F);
		registerCompostableItem(VineryItems.CHERRY.get(), 0.4F);
		registerCompostableItem(VineryBlocks.OLD_CHERRY_SAPLING.get(), 0.4F);
		registerCompostableItem(VineryBlocks.CHERRY_SAPLING.get(), 0.4F);
		registerCompostableItem(VineryItems.APPLE_MASH.get(), 0.4F);
		registerCompostableItem(VineryItems.STRAW_HAT.get(), 0.4F);
	}

	public static void registerCompostableItem(ItemLike item, float chance) {
		if (item.asItem() != Items.AIR) {
			ComposterBlock.COMPOSTABLES.put(item.asItem(), chance);
		}
	}
}
