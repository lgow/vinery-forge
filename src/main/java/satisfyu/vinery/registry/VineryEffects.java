package satisfyu.vinery.registry;

import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import satisfyu.vinery.Vinery;
import satisfyu.vinery.effect.EmptyEffect;
import satisfyu.vinery.effect.JellieEffect;

import java.util.function.Supplier;

public class VineryEffects {
	public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS,
			Vinery.MODID);

	public static final RegistryObject<MobEffect> EMPTY = registerEffekt("empty", EmptyEffect::new);

	public static final RegistryObject<MobEffect> JELLIE = registerEffekt("jellie", JellieEffect::new);

	public static void register(IEventBus eventBus) {
		MOB_EFFECTS.register(eventBus);
	}

	public static RegistryObject<MobEffect> registerEffekt(String name, Supplier<MobEffect> effect) {
		return MOB_EFFECTS.register(name, effect);
	}
}
