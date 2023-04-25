package satisfyu.vinery.client.gui.sidetip;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.ArrayList;
import java.util.List;

public class RecipeHandledGUI <T extends RecipeGUIHandler> extends AbstractContainerScreen<T> {
	private final SideTipButton SIDE_TIP_BUTTON = new SideTipButton(this.leftPos, this.topPos, new ShowHideTips());

	private final ResourceLocation BACKGROUND;

	private final SideTip SIDE_TIP;

	private final List<SideToolTip> sideToolTips = new ArrayList<>();

	public RecipeHandledGUI(T pMenu, Inventory pPlayerInventory, Component pTitle, ResourceLocation background, ResourceLocation sideTip, int frames) {
		super(pMenu, pPlayerInventory, pTitle);
		this.BACKGROUND = background;
		this.SIDE_TIP = new SideTip(this.leftPos, this.topPos, 147, 180, 256, sideTip, 256, 256 * frames, frames);
		addToolTips();
	}

	@Override
	public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
		renderBackground(pPoseStack);
		SIDE_TIP.setPos(this.leftPos - SideTip.WIDTH, this.topPos);
		SIDE_TIP_BUTTON.setPos(this.leftPos + 4, this.topPos + 25);
		addRenderableOnly(SIDE_TIP);
		addRenderableWidget(SIDE_TIP_BUTTON);
		super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
		renderTooltips(pPoseStack, pMouseX, pMouseY);
		renderTooltip(pPoseStack, pMouseX, pMouseY);
	}

	private void renderTooltips(PoseStack pPoseStack, int mouseX, int mouseY) {
		for (SideToolTip sideToolTip : sideToolTips) {
			if (sideToolTip.isMouseOver(mouseX - this.leftPos, mouseY - this.topPos)) {
				renderTooltip(pPoseStack, sideToolTip.getText(), mouseX, mouseY);
			}
		}
	}

	@Override
	protected void containerTick() {
		super.containerTick();
		SIDE_TIP.tick();
	}

	@Override
	protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, BACKGROUND);
		final int posX = this.leftPos;
		final int posY = this.topPos;
		this.blit(pPoseStack, posX, posY, 0, 0, this.imageWidth - 1, this.imageHeight);
		renderProgressArrow(pPoseStack, posX, posY);
		renderBurnIcon(pPoseStack, posX, posY);
	}

	public void renderProgressArrow(PoseStack pPoseStack, int posX, int posY) { }

	public void renderBurnIcon(PoseStack pPoseStack, int posX, int posY) { }

	public void addToolTips() {
		addToolTip(
				new SideToolTip(SideTip.WIDTH + 4, 25, 20, 18, Component.translatable("tooltip.vinery.recipe_book")));
	}

	public void addToolTip(SideToolTip sideToolTip) {
		this.sideToolTips.add(sideToolTip);
	}

	class ShowHideTips implements Button.OnPress {
		@Override
		public void onPress(Button pButton) {
			SIDE_TIP.visible = !SIDE_TIP.visible;
		}
	}
}
