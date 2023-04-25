package satisfyu.vinery.client.gui.sidetip;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;

public class SideTip extends GuiComponent implements Widget, GuiEventListener {
	public static final int WIDTH = 147;

	private final int height;

	private final ResourceLocation TEXTURE;

	private final int textureWidth;

	private final int textureHeight;

	private final int vOffset;

	private final int frameTicks = 40;

	private final int frames;

	public boolean visible = false;

	protected boolean hovered;

	private int x;

	private int y;

	private int currentTick;

	public SideTip(int x, int y, int width, int height, int vOffset, ResourceLocation texture, int textureWidth, int textureHeight, int frames) {
		this.TEXTURE = texture;
		this.x = x;
		this.y = y;
		this.height = height;
		this.textureWidth = textureWidth;
		this.textureHeight = textureHeight;
		this.vOffset = vOffset;
		this.frames = frames;
	}

	@Override
	public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
		if (this.visible) {
			this.hovered = pMouseX >= this.x && pMouseY >= this.y && pMouseX < this.x + WIDTH
					&& pMouseY < this.y + this.height;
			RenderSystem.setShader(GameRenderer::getPositionTexShader);
			RenderSystem.setShaderTexture(0, TEXTURE);
			RenderSystem.enableDepthTest();
			int offsetFactor = currentTick / frameTicks;
			blit(pPoseStack, this.x, this.y, 0, vOffset * (offsetFactor % frames), WIDTH, this.height,
					this.textureWidth, this.textureHeight);
		}
	}

	public void tick() {
		currentTick++;
		currentTick = currentTick % (frames * frameTicks);
	}

	public void setPos(int x, int y) {
		this.x = x;
		this.y = y;
	}
}
