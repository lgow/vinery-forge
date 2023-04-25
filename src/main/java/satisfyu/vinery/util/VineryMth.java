package satisfyu.vinery.util;

import com.mojang.math.Quaternion;
import net.minecraft.util.Mth;

public class VineryMth {
	public static Quaternion getQuaterion(double angle, double x, double y, double z) {
		Quaternion quaternion = new Quaternion(0, 0, 0, 1);
		double s = Mth.sin((float) (angle * 0.5));
		quaternion.set((float) (x * s), (float) (y * s), (float) (z * s), (float) (cosFromSin(s, angle * 0.5F)));
		return quaternion;
	}

	public static double cosFromSin(double sin, double angle) {
		double cos = Mth.sqrt((float) (1.0 - sin * sin));
		double a = angle + Mth.PI * 0.5;
		double b = a - (int) (a / (Mth.PI * 2.0)) * (Mth.PI * 2);
		if (b < 0.0) { b = Mth.PI * 2 + b; }
		if (b >= Mth.PI) { return -cos; }
		return cos;
	}
}
