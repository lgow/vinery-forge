package satisfyu.vinery.util;

import net.minecraft.util.StringRepresentable;

public enum GrapevineType implements StringRepresentable {
	NONE, RED, WHITE {
	};

	@Override
	public String toString() {
		return this.getSerializedName();
	}

	@Override
	public String getSerializedName() {
		return switch (this) {
			case NONE -> "none";
			case RED -> "red";
			case WHITE -> "white";
		};
	}
}
