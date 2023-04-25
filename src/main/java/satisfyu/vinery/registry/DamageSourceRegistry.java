package satisfyu.vinery.registry;

import net.minecraft.world.damagesource.DamageSource;
import satisfyu.vinery.Vinery;

public class DamageSourceRegistry extends DamageSource {
	public static final DamageSource STOVE_BLOCK = (new DamageSourceRegistry("stove_block")).setIsFire();

	protected DamageSourceRegistry(String name) {
		super(Vinery.MODID + "." + name);
	}
}