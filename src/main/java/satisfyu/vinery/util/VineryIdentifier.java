package satisfyu.vinery.util;

import net.minecraft.resources.ResourceLocation;
import satisfyu.vinery.Vinery;

/**
 * Namespace for Vinery mod
 */
public class VineryIdentifier extends ResourceLocation {
	public VineryIdentifier(String path) {
		super(Vinery.MODID, path);
	}

	public static String asString(String path) {
		return (Vinery.MODID + ":" + path);
	}
}
