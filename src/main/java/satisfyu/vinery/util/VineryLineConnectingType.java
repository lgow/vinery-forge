package satisfyu.vinery.util;

import net.minecraft.util.StringRepresentable;

public enum VineryLineConnectingType implements StringRepresentable {
	NONE("none"), MIDDLE("middle"), LEFT("left"), RIGHT("right");

	private final String name;

	VineryLineConnectingType(String pName) {
		this.name = pName;
	}

	public String toString() {
		return this.name;
	}

	public String getSerializedName() {
		return this.name;
	}
}
