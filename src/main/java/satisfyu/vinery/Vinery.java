package satisfyu.vinery;

import dev.architectury.registry.fuel.FuelRegistry;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import satisfyu.vinery.mixin.AxeItemAccess;
import satisfyu.vinery.registry.*;
import satisfyu.vinery.util.VineryIdentifier;
import satisfyu.vinery.world.VineryConfiguredFeatures;

import java.util.IdentityHashMap;
import java.util.Map;

@Mod(Vinery.MODID)
public class Vinery {
	public static final String MODID = "vinery";

	public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

	public static final CreativeModeTab TAB = new CreativeModeTab("vinery.creative_tab") {
		@Override
		public @NotNull ItemStack makeIcon() {
			return new ItemStack(VineryItems.RED_GRAPE.get());
		}
	};

	public Vinery() {
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		VineryBlocks.register(modEventBus);
		VineryItems.register(modEventBus);
		VineryEffects.register(modEventBus);
		VineryEntites.register(modEventBus);
		VineryBlockEntityTypes.register(modEventBus);
		VineryScreenHandlerTypes.register(modEventBus);
		VineryRecipeTypes.register(modEventBus);
		VinerySoundEvents.register(modEventBus);
		VineryVillagers.register(modEventBus);
		VineryConfiguredFeatures.register(modEventBus);
		modEventBus.addListener(this::commonSetup);
		// Register ourselves for server and other game events we are interested in
		MinecraftForge.EVENT_BUS.register(this);
	}

	private void commonSetup(final FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			VineryMessages.register();
			VineryVillagers.registerPOIs();
			FuelRegistry.register(300, VineryBlocks.CHERRY_FENCE.get(), VineryBlocks.CHERRY_FENCE_GATE.get(),
					VineryBlocks.STACKABLE_LOG.get(), VineryBlocks.FERMENTATION_BARREL.get());
		});
		Map<Block, Block> strippables = new IdentityHashMap<>(AxeItemAccess.getStrippables());
		strippables.put(VineryBlocks.CHERRY_LOG.get(), VineryBlocks.STRIPPED_CHERRY_LOG.get());
		strippables.put(VineryBlocks.CHERRY_WOOD.get(), VineryBlocks.STRIPPED_CHERRY_WOOD.get());
		strippables.put(VineryBlocks.OLD_CHERRY_LOG.get(), VineryBlocks.STRIPPED_OLD_CHERRY_LOG.get());
		strippables.put(VineryBlocks.OLD_CHERRY_WOOD.get(), VineryBlocks.STRIPPED_OLD_CHERRY_WOOD.get());
		AxeItemAccess.setStripables(strippables);
	}
}

