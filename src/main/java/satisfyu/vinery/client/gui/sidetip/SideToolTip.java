package satisfyu.vinery.client.gui.sidetip;

import net.minecraft.network.chat.Component;

public class SideToolTip {
	private final int x;

	private final int y;

	private final int width;

	private final int height;

	private final Component text;

	public SideToolTip(int x, int y, int width, int height, Component text) {
		this.x = -SideTip.WIDTH + x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.text = text;
	}

	public boolean isMouseOver(int mouseX, int mouseY) {
		return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
	}

	public Component getText() {
		return this.text;
	}
}
