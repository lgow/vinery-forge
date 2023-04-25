package satisfyu.vinery.util;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class VineryTags {
	public static class Items {
		public static final TagKey<Item> CHERRY_LOGS = tag("cherry_logs");

		public static final TagKey<Item> JAMS = tag("jams");

		public static final TagKey<Item> IGNORE_BLOCK_ITEM = tag("ignore_block_item");

		private static TagKey<Item> tag(String name) {
			return ItemTags.create(new VineryIdentifier(name));
		}
	}

	public static class Blocks {
		public static final TagKey<Block> ALLOWS_COOKING_ON_POT = tag("allows_cooking_on_pot");

		public static final TagKey<Block> CAN_NOT_CONNECT = tag("can_not_connect");

		private static TagKey<Block> tag(String name) {
			return BlockTags.create(new VineryIdentifier(name));
		}
	}
}
