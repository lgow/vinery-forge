package satisfyu.vinery.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import satisfyu.vinery.client.gui.handler.AgingBarrelMenu;
import satisfyu.vinery.client.gui.sidetip.RecipeHandledGUI;
import satisfyu.vinery.client.gui.sidetip.SideToolTip;
import satisfyu.vinery.util.VineryIdentifier;

public class AgingBarrelGui extends RecipeHandledGUI<AgingBarrelMenu> {
	private static final ResourceLocation BACKGROUND;

	private static final ResourceLocation SIDE_TIP;

	private static final int FRAMES = 1;

	static {
		BACKGROUND = new VineryIdentifier("textures/gui/barrel_gui.png");
		SIDE_TIP = new VineryIdentifier("textures/gui/fermentation_barrel_recipe_book.png");
	}

	public AgingBarrelGui(AgingBarrelMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
		super(pMenu, pPlayerInventory, pTitle, BACKGROUND, SIDE_TIP, FRAMES);
	}

	@Override
	public void renderProgressArrow(PoseStack pPoseStack, int posX, int posY) {
		int progress = this.menu.getScaledProgress(23);
		this.blit(pPoseStack, posX + 94, posY + 37, 177, 17, progress, 10);
	}

	@Override
	protected void init() {
		super.init();
		titleLabelX += 20;
	}

	@Override
	public void addToolTips() {
		super.addToolTips();
		addToolTip(new SideToolTip(16, 18, 48, 47, Component.translatable("tooltip.vinery.aging_barrel")));
		addToolTip(new SideToolTip(96, 32, 17, 17, Component.translatable("block.vinery.wine_bottle")));
		addToolTip(new SideToolTip(54, 120, 34, 30, Component.translatable("tooltip.vinery.wine")));
	}
}
