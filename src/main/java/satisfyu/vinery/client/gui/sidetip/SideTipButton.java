package satisfyu.vinery.client.gui.sidetip;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.resources.ResourceLocation;
import satisfyu.vinery.util.VineryIdentifier;

public class SideTipButton extends Button {
	private final ResourceLocation location;

	int u;

	int v;

	int hoveredVOffset;

	int textureWidth;

	int textureHeight;

	public SideTipButton(int x, int y, Button.OnPress onPress) {
		this(x, y, 0, 0, 18, new VineryIdentifier("textures/gui/recipe_book.png"), 20, 36, onPress);
	}

	public SideTipButton(int x, int y, int u, int v, int hoveredVOffset, ResourceLocation location, int textureWidth, int textureHeight, Button.OnPress onPress) {
		super(x, y, 20, 18, CommonComponents.EMPTY, onPress, NO_TOOLTIP);
		this.u = u;
		this.v = v;
		this.hoveredVOffset = hoveredVOffset;
		this.location = location;
		this.textureWidth = textureWidth;
		this.textureHeight = textureHeight;
	}

	@Override
	public void renderButton(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, location);
		int i = v;
		if (this.isHoveredOrFocused()) {
			i += hoveredVOffset;
		}
		RenderSystem.enableDepthTest();
		blit(pPoseStack, x, y, u, i, width, height, textureWidth, textureHeight);
		if (this.isHovered) {
			this.renderToolTip(pPoseStack, pMouseX, pMouseY);
		}
	}

	public void setPos(int x, int y) {
		this.x = x;
		this.y = y;
	}
}
