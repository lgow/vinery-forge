package satisfyu.vinery.event;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import satisfyu.vinery.Vinery;
import satisfyu.vinery.entity.WanderingWinemakerEntity;
import satisfyu.vinery.registry.VineryBlocks;
import satisfyu.vinery.registry.VineryEntites;
import satisfyu.vinery.registry.VineryItems;
import satisfyu.vinery.registry.VineryVillagers;
import satisfyu.vinery.util.VineryIdentifier;

import java.util.List;

public class ModEvents {
	@Mod.EventBusSubscriber(modid = Vinery.MODID)
	public static class ForgeEvents {
		@SubscribeEvent
		public static void addCustomTrades(VillagerTradesEvent event) {
			if (event.getType().equals(VineryVillagers.WINEMAKER.get())) {
				Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
				List<VillagerTrades.ItemListing> level1 = trades.get(1);
				level1.add(new VineryVillagers.BuyForOneEmeraldFactory(VineryItems.RED_GRAPE.get(), 5, 4, 5));
				level1.add(new VineryVillagers.BuyForOneEmeraldFactory(VineryItems.WHITE_GRAPE.get(), 5, 4, 5));
				level1.add(new VineryVillagers.SellItemFactory(VineryItems.RED_GRAPE_SEEDS.get(), 2, 1, 5));
				level1.add(new VineryVillagers.SellItemFactory(VineryItems.WHITE_GRAPE_SEEDS.get(), 2, 1, 5));
				List<VillagerTrades.ItemListing> level2 = trades.get(2);
				level2.add(new VineryVillagers.SellItemFactory(VineryBlocks.WINE_BOTTLE.get(), 1, 2, 7));
				List<VillagerTrades.ItemListing> level3 = trades.get(3);
				level3.add(new VineryVillagers.SellItemFactory(VineryBlocks.COOKING_POT.get(), 3, 1, 10));
				level3.add(new VineryVillagers.SellItemFactory(VineryBlocks.FLOWER_BOX.get(), 3, 1, 10));
				level3.add(new VineryVillagers.SellItemFactory(VineryBlocks.WHITE_GRAPE_CRATE.get(), 7, 1, 10));
				level3.add(new VineryVillagers.SellItemFactory(VineryBlocks.RED_GRAPE_CRATE.get(), 7, 1, 10));
				List<VillagerTrades.ItemListing> level4 = trades.get(4);
				level4.add(new VineryVillagers.SellItemFactory(VineryBlocks.BASKET.get(), 4, 1, 10));
				level4.add(new VineryVillagers.SellItemFactory(VineryBlocks.TALL_FLOWER_POT.get(), 5, 1, 10));
				level4.add(new VineryVillagers.SellItemFactory(VineryBlocks.WINDOW.get(), 12, 1, 10));
				level4.add(new VineryVillagers.SellItemFactory(VineryBlocks.CHERRY_BEAM.get(), 6, 1, 10));
				List<VillagerTrades.ItemListing> level5 = trades.get(5);
				level5.add(new VineryVillagers.SellItemFactory(VineryBlocks.WINE_BOX.get(), 10, 1, 10));
				level5.add(new VineryVillagers.SellItemFactory(VineryBlocks.KING_DANIS_WINE.get(), 4, 1, 10));
				level5.add(new VineryVillagers.SellItemFactory(VineryItems.GLOVES.get(), 12, 1, 15));
			}
		}

		@SubscribeEvent
		public static void lootTableLoadEvent(LootTableLoadEvent event) {
			final ResourceLocation resourceLocation = new VineryIdentifier("inject/seeds");
			ResourceLocation id = event.getTable().getLootTableId();
			if (Blocks.GRASS.getLootTable().equals(id) || Blocks.TALL_GRASS.getLootTable().equals(id)
					|| Blocks.FERN.getLootTable().equals(id)) {
				event.getTable().addPool(
						LootPool.lootPool().add(LootTableReference.lootTableReference(resourceLocation).setWeight(1))
								.build());
			}
		}
	}

	@Mod.EventBusSubscriber(modid = Vinery.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class ModEventBusEvents {
		@SubscribeEvent
		public static void entityAttributeEvent(EntityAttributeCreationEvent event) {
			event.put(VineryEntites.TRADER_MULE.get(),
					Llama.createAttributes().add(Attributes.MOVEMENT_SPEED, 0.2f).build());
			event.put(VineryEntites.WANDERING_WINEMAKER.get(), WanderingWinemakerEntity.createMobAttributes().build());
		}
	}
}
