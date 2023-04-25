package satisfyu.vinery.client;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.client.renderer.entity.ArmorStandRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GrassColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import satisfyu.vinery.Vinery;
import satisfyu.vinery.block.entity.chair.ChairRenderer;
import satisfyu.vinery.client.gui.AgingBarrelGui;
import satisfyu.vinery.client.gui.ApplePressGUI;
import satisfyu.vinery.client.gui.CookingPotGui;
import satisfyu.vinery.client.gui.OvenGUI;
import satisfyu.vinery.client.model.StrawHatModel;
import satisfyu.vinery.client.model.TraderMuleModel;
import satisfyu.vinery.client.render.block.*;
import satisfyu.vinery.client.render.entity.TraderMuleRenderer;
import satisfyu.vinery.client.render.entity.VineryBoatRenderer;
import satisfyu.vinery.client.render.entity.WanderingWinemakerRenderer;
import satisfyu.vinery.client.render.feature.StrawHatLayerRenderer;
import satisfyu.vinery.item.WinemakerArmorItem;
import satisfyu.vinery.registry.*;
import satisfyu.vinery.util.VineryIdentifier;

import java.util.List;

@Mod.EventBusSubscriber(modid = Vinery.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientModEvents {
	public static final ModelLayerLocation TRADER_MULE_LAYER_LOCATION = new ModelLayerLocation(
			new VineryIdentifier("trader_mule"), "main");

	@SubscribeEvent
	public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(VineryEntites.TRADER_MULE.get(), TraderMuleRenderer::new);
		event.registerEntityRenderer(VineryEntites.WANDERING_WINEMAKER.get(), WanderingWinemakerRenderer::new);
		event.registerEntityRenderer(VineryBlockEntityTypes.CHAIR.get(), ChairRenderer::new);
		LayerDefinition boatLayerDefiniton = BoatModel.createBodyModel(false);
		LayerDefinition chestBoatLayerDefiniton = BoatModel.createBodyModel(true);
		ForgeHooksClient.registerLayerDefinition(VineryBoatRenderer.createBoatModelname(), () -> boatLayerDefiniton);
		ForgeHooksClient.registerLayerDefinition(VineryBoatRenderer.createChestBoatModelname(),
				() -> chestBoatLayerDefiniton);
		event.registerEntityRenderer(VineryEntites.BOAT.get(), p_174010_ -> new VineryBoatRenderer(p_174010_, false));
		event.registerEntityRenderer(VineryEntites.CHEST_BOAT.get(),
				p_174010_ -> new VineryBoatRenderer(p_174010_, true));
	}

	@SubscribeEvent
	public static void onEntityRenderers(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(TRADER_MULE_LAYER_LOCATION, TraderMuleModel::createBodyLayer);
		event.registerLayerDefinition(StrawHatModel.LAYER_LOCATION, StrawHatModel::createBodyLayer);
	}

	@SubscribeEvent
	public static void registerBlockRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerBlockEntityRenderer(VineryBlockEntityTypes.WINE_BOX_BLOCK_ENTITY.get(), WineBoxRenderer::new);
		event.registerBlockEntityRenderer(VineryBlockEntityTypes.VINERY_SIGN_BLOCK_ENTITY.get(), SignRenderer::new);
		event.registerBlockEntityRenderer(VineryBlockEntityTypes.SHELF_ENTITY.get(), ShelfRenderer::new);
		event.registerBlockEntityRenderer(VineryBlockEntityTypes.NINE_WINE_RACK_ENTITY.get(), NineBottleRenderer::new);
		event.registerBlockEntityRenderer(VineryBlockEntityTypes.FOUR_WINE_RACK_ENTITY.get(), FourBottleRenderer::new);
		event.registerBlockEntityRenderer(VineryBlockEntityTypes.FLOWER_BOX_ENTITY.get(), FlowerBoxRenderer::new);
		event.registerBlockEntityRenderer(VineryBlockEntityTypes.TALL_FLOWER_POT_ENTITY.get(), TallFlowerPotRenderer::new);
	}

	@SubscribeEvent
	public static void attachRendererLayers(EntityRenderersEvent.AddLayers event) {
		PlayerRenderer renderer = event.getSkin("default");
		renderer.addLayer(new StrawHatLayerRenderer<>(renderer, event.getEntityModels()));
		ArmorStandRenderer armorStandRenderer = event.getRenderer(EntityType.ARMOR_STAND);
		armorStandRenderer.addLayer(new StrawHatLayerRenderer<>(armorStandRenderer, event.getEntityModels()));
	}

	@SubscribeEvent
	public static void onClientSetup(FMLClientSetupEvent event) {
		MenuScreens.register(VineryScreenHandlerTypes.OVEN_SCREEN_HANDLER.get(), OvenGUI::new);
		MenuScreens.register(VineryScreenHandlerTypes.APPLE_PRESS_SCREEN_HANDLER.get(), ApplePressGUI::new);
		MenuScreens.register(VineryScreenHandlerTypes.AGING_BARREL_SCREEN_HANDLER.get(), AgingBarrelGui::new);
		MenuScreens.register(VineryScreenHandlerTypes.COOKING_POT_SCREEN_HANDLER.get(), CookingPotGui::new);
	}

	@SubscribeEvent
	public static void colorEvent(RegisterColorHandlersEvent.Block event) {
		event.register(
				(pState, pLevel, pPos, pTintIndex) -> pLevel != null && pPos != null ? BiomeColors.getAverageGrassColor(
						pLevel, pPos) : GrassColor.get(0.5D, 1.0D), VineryBlocks.GRASS_SLAB.get());
	}

	@SubscribeEvent
	public static void colorEvent(RegisterColorHandlersEvent.Item event) {
		event.register((pStack, pTintIndex) -> GrassColor.get(0.5D, 1.0D), VineryBlocks.GRASS_SLAB.get());
	}

	public static void appendToolTip(List<Component> tooltip) {
		Player player = Minecraft.getInstance().player;
        if (player == null) { return; }
		ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);
		ItemStack chestplate = player.getItemBySlot(EquipmentSlot.CHEST);
		ItemStack leggings = player.getItemBySlot(EquipmentSlot.LEGS);
		ItemStack boots = player.getItemBySlot(EquipmentSlot.FEET);
		tooltip.add(Component.nullToEmpty(""));
		tooltip.add(Component.nullToEmpty(ChatFormatting.AQUA + I18n.get("vinery.tooltip.winemaker_armor")));
		tooltip.add(Component.nullToEmpty(
				(helmet != null && helmet.getItem() instanceof WinemakerArmorItem ? ChatFormatting.GREEN.toString()
						: ChatFormatting.GRAY.toString()) + "- [" + VineryItems.STRAW_HAT.get().getDescription()
						.getString() + "]"));
		tooltip.add(Component.nullToEmpty((chestplate != null && chestplate.getItem() instanceof WinemakerArmorItem
				? ChatFormatting.GREEN.toString() : ChatFormatting.GRAY.toString()) + "- ["
				+ VineryItems.VINEMAKER_APRON.get().getDescription().getString() + "]"));
		tooltip.add(Component.nullToEmpty(
				(leggings != null && leggings.getItem() instanceof WinemakerArmorItem ? ChatFormatting.GREEN.toString()
						: ChatFormatting.GRAY.toString()) + "- [" + VineryItems.VINEMAKER_LEGGINGS.get()
						.getDescription().getString() + "]"));
		tooltip.add(Component.nullToEmpty(
				(boots != null && boots.getItem() instanceof WinemakerArmorItem ? ChatFormatting.GREEN.toString()
						: ChatFormatting.GRAY.toString()) + "- [" + VineryItems.VINEMAKER_BOOTS.get().getDescription()
						.getString() + "]"));
		tooltip.add(Component.nullToEmpty(""));
		tooltip.add(Component.nullToEmpty(ChatFormatting.GRAY + I18n.get("vinery.tooltip.winemaker_armor2")));
		tooltip.add(Component.nullToEmpty(
				((helmet != null && helmet.getItem() instanceof WinemakerArmorItem && chestplate != null
						&& chestplate.getItem() instanceof WinemakerArmorItem && leggings != null
						&& leggings.getItem() instanceof WinemakerArmorItem && boots != null
						&& boots.getItem() instanceof WinemakerArmorItem) ? ChatFormatting.DARK_GREEN.toString()
						: ChatFormatting.GRAY.toString()) + I18n.get("vinery.tooltip.winemaker_armor3")));
	}
}