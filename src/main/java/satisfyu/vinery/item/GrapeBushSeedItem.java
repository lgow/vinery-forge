package satisfyu.vinery.item;

import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.level.block.Block;
import satisfyu.vinery.util.GrapevineType;

public class GrapeBushSeedItem extends ItemNameBlockItem {
	private final GrapevineType type;

	public GrapeBushSeedItem(Block block, Properties settings, GrapevineType type) {
		super(block, settings);
		this.type = type;
	}

	public GrapevineType getType() {
		return type;
	}
}
