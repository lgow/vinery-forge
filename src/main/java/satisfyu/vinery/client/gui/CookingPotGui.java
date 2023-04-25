package satisfyu.vinery.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import satisfyu.vinery.client.gui.handler.CookingPotMenu;
import satisfyu.vinery.client.gui.sidetip.RecipeHandledGUI;
import satisfyu.vinery.client.gui.sidetip.SideToolTip;
import satisfyu.vinery.util.VineryIdentifier;

public class CookingPotGui extends RecipeHandledGUI<CookingPotMenu> {
	private static final ResourceLocation BACKGROUND;

	private static final ResourceLocation SIDE_TIP;

	private static final int FRAMES = 1;

	static {
		BACKGROUND = new VineryIdentifier("textures/gui/pot_gui.png");
		SIDE_TIP = new VineryIdentifier("textures/gui/cooking_pot_recipe_book.png");
	}

	public CookingPotGui(CookingPotMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
		super(pMenu, pPlayerInventory, pTitle, BACKGROUND, SIDE_TIP, FRAMES);
	}

	@Override
	public void renderProgressArrow(PoseStack pPoseStack, int posX, int posY) {
		int progress = this.menu.getScaledProgress(18);
		this.blit(pPoseStack, posX + 95, posY + 14, 178, 15, progress, 30);
	}

	@Override
	public void renderBurnIcon(PoseStack pPoseStack, int posX, int posY) {
		if (menu.isBeingBurned()) {
			this.blit(pPoseStack, posX + 124, posY + 56, 176, 0, 17, 15);
		}
	}

	@Override
	protected void init() {
		super.init();
		titleLabelX += 20;
	}

	@Override
	public void addToolTips() {
		super.addToolTips();
		final int normalWidthAndHeight = 18;
		final int firstRow = 15;
		final int secondRow = 41;
		final int containerRow = 67;
		final int resultRow = 101;
		final int firstLine = 19;
		addToolTip(new SideToolTip(firstRow, firstLine, normalWidthAndHeight, normalWidthAndHeight,
				Component.translatable("item.minecraft.sweet_berries")));
		addToolTip(new SideToolTip(secondRow, firstLine, normalWidthAndHeight, normalWidthAndHeight,
				Component.translatable("item.minecraft.sugar")));
		addToolTip(new SideToolTip(containerRow, firstLine, normalWidthAndHeight, normalWidthAndHeight,
				Component.translatable("block.vinery.cherry_jar")));
		addToolTip(new SideToolTip(resultRow, firstLine, normalWidthAndHeight, normalWidthAndHeight,
				Component.translatable("block.vinery.sweetberry_jam")));
		final int secondLine = 44;
		addToolTip(new SideToolTip(firstRow, secondLine, normalWidthAndHeight, normalWidthAndHeight,
				Component.translatable("item.vinery.cherry")));
		addToolTip(new SideToolTip(secondRow, secondLine, normalWidthAndHeight, normalWidthAndHeight,
				Component.translatable("item.minecraft.sugar")));
		addToolTip(new SideToolTip(containerRow, secondLine, normalWidthAndHeight, normalWidthAndHeight,
				Component.translatable("block.vinery.cherry_jar")));
		addToolTip(new SideToolTip(resultRow, secondLine, normalWidthAndHeight, normalWidthAndHeight,
				Component.translatable("block.vinery.cherry_jam")));
		final int thirdLine = 69;
		addToolTip(new SideToolTip(firstRow, thirdLine, normalWidthAndHeight, normalWidthAndHeight,
				Component.translatable("item.vinery.apple_mash")));
		addToolTip(new SideToolTip(secondRow, thirdLine, normalWidthAndHeight, normalWidthAndHeight,
				Component.translatable("item.minecraft.sugar")));
		addToolTip(new SideToolTip(containerRow, thirdLine, normalWidthAndHeight, normalWidthAndHeight,
				Component.translatable("block.vinery.cherry_jar")));
		addToolTip(new SideToolTip(resultRow, thirdLine, normalWidthAndHeight, normalWidthAndHeight,
				Component.translatable("block.vinery.apple_jam")));
		final int fourthLine = 94;
		addToolTip(new SideToolTip(firstRow, fourthLine, normalWidthAndHeight, normalWidthAndHeight,
				Component.translatable("item.vinery.red_grape")));
		addToolTip(new SideToolTip(secondRow, fourthLine, normalWidthAndHeight, normalWidthAndHeight,
				Component.translatable("item.minecraft.sugar")));
		addToolTip(new SideToolTip(containerRow, fourthLine, normalWidthAndHeight, normalWidthAndHeight,
				Component.translatable("block.vinery.cherry_jar")));
		addToolTip(new SideToolTip(resultRow, fourthLine, normalWidthAndHeight, normalWidthAndHeight,
				Component.translatable("block.vinery.grape_jam")));
		final int fifthLine = 119;
		addToolTip(new SideToolTip(firstRow, fifthLine, normalWidthAndHeight, normalWidthAndHeight,
				Component.translatable("item.minecraft.apple")));
		addToolTip(new SideToolTip(secondRow, fifthLine, normalWidthAndHeight, normalWidthAndHeight,
				Component.translatable("item.minecraft.sugar")));
		addToolTip(new SideToolTip(containerRow, fifthLine, normalWidthAndHeight, normalWidthAndHeight,
				Component.translatable("item.minecraft.bowl")));
		addToolTip(new SideToolTip(resultRow, fifthLine, normalWidthAndHeight, normalWidthAndHeight,
				Component.translatable("item.vinery.applesauce")));
	}
}