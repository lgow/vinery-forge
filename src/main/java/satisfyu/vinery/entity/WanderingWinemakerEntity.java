package satisfyu.vinery.entity;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import satisfyu.vinery.registry.VineryBlocks;
import satisfyu.vinery.registry.VineryItems;

import java.util.Map;

public class WanderingWinemakerEntity extends WanderingTrader {
	public static final Int2ObjectMap<VillagerTrades.ItemListing[]> TRADES = new Int2ObjectOpenHashMap<>(
			Map.of(1, new VillagerTrades.ItemListing[] {
					new VillagerTrades.ItemsForEmeralds(VineryItems.RED_GRAPE.get(), 1, 1, 8, 1),
					new VillagerTrades.ItemsForEmeralds(VineryItems.WHITE_GRAPE.get(), 1, 1, 8, 1),
					new VillagerTrades.ItemsForEmeralds(VineryBlocks.CHERRY_SAPLING.get(), 3, 1, 8, 1),
					new VillagerTrades.ItemsForEmeralds(VineryBlocks.OLD_CHERRY_SAPLING.get(), 5, 1, 8, 1),
					new VillagerTrades.ItemsForEmeralds(VineryItems.RED_GRAPE.get(), 2, 1, 8, 1),
					new VillagerTrades.ItemsForEmeralds(VineryBlocks.RED_GRAPEJUICE_WINE_BOTTLE.get(), 2, 1, 8, 1),
					new VillagerTrades.ItemsForEmeralds(VineryBlocks.WHITE_GRAPEJUICE_WINE_BOTTLE.get(), 2, 1, 8, 1),
					new VillagerTrades.ItemsForEmeralds(VineryBlocks.COARSE_DIRT_SLAB.get(), 1, 1, 8, 1),
					new VillagerTrades.ItemsForEmeralds(VineryBlocks.GRASS_SLAB.get(), 1, 1, 8, 1),
					new VillagerTrades.ItemsForEmeralds(VineryBlocks.CHERRY_PLANKS.get(), 2, 1, 8, 1),
					new VillagerTrades.ItemsForEmeralds(VineryBlocks.CHERRY_WINE.get(), 1, 1, 8, 1)
			}));

	public WanderingWinemakerEntity(EntityType<? extends WanderingWinemakerEntity> entityType, Level world) {
		super(entityType, world);
	}

	@Override
	protected void updateTrades() {
		if (this.offers == null) {
			this.offers = new MerchantOffers();
		}
		this.addOffersFromItemListings(this.offers, TRADES.get(1), 8);
	}
}