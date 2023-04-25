package satisfyu.vinery.util;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public enum EnumTallFlower implements StringRepresentable {
	NONE("none", Blocks.AIR), LILAC("lilac", Blocks.LILAC), PEONY("peony", Blocks.PEONY), ROSE_BUSH("rose_bush",
			Blocks.ROSE_BUSH);

	private final String name;

	private final Block flower;

	EnumTallFlower(String name, Block flower) {
		this.name = name;
		this.flower = flower;
	}

	@Override
	public String getSerializedName() {
		return this.name;
	}

	public Block getFlower() {
		return this.flower;
	}
}
