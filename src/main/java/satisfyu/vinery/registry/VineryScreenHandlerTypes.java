package satisfyu.vinery.registry;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import satisfyu.vinery.Vinery;
import satisfyu.vinery.client.gui.handler.ApplePressMenu;
import satisfyu.vinery.client.gui.handler.CookingPotMenu;
import satisfyu.vinery.client.gui.handler.AgingBarrelMenu;
import satisfyu.vinery.client.gui.handler.OvenMenu;

import java.util.function.Supplier;

public class VineryScreenHandlerTypes {
	public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES,
			Vinery.MODID);

	private static <T extends MenuType<?>> RegistryObject<T> create(String name, Supplier<T> type) {
		return MENU_TYPES.register(name, type);
	}	public static final RegistryObject<MenuType<OvenMenu>> OVEN_SCREEN_HANDLER = create("stove_gui_handler",
			() -> new MenuType<>(OvenMenu::new));

	public static void register(IEventBus eventBus) {
		MENU_TYPES.register(eventBus);
	}	public static final RegistryObject<MenuType<AgingBarrelMenu>> AGING_BARREL_SCREEN_HANDLER = create(
			"fermentation_barrel_gui_handler", () -> new MenuType<>(AgingBarrelMenu::new));

	public static final RegistryObject<MenuType<CookingPotMenu>> COOKING_POT_SCREEN_HANDLER = create(
			"cooking_pot_gui_handler", () -> new MenuType<>(CookingPotMenu::new));

	public static final RegistryObject<MenuType<ApplePressMenu>> APPLE_PRESS_SCREEN_HANDLER = create(
			"wine_press_gui_handler", () -> new MenuType<>(ApplePressMenu::new));




}
