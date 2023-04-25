package satisfyu.vinery.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import satisfyu.vinery.client.gui.handler.OvenMenu;
import satisfyu.vinery.client.gui.sidetip.RecipeHandledGUI;
import satisfyu.vinery.client.gui.sidetip.SideToolTip;
import satisfyu.vinery.util.VineryIdentifier;

public class OvenGUI extends RecipeHandledGUI<OvenMenu> {
	private static final ResourceLocation BACKGROUND;

	private static final ResourceLocation SIDE_TIP;

	private static final int FRAMES = 1;

	static {
		BACKGROUND = new VineryIdentifier("textures/gui/stove_gui.png");
		SIDE_TIP = new VineryIdentifier("textures/gui/oven_recipe_book.png");
	}

	public OvenGUI(OvenMenu handler, Inventory inventory, Component title) {
		super(handler, inventory, title, BACKGROUND, SIDE_TIP, FRAMES);
	}

	@Override
	public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
		this.renderBackground(matrices);
		super.render(matrices, mouseX, mouseY, delta);
		this.renderTooltip(matrices, mouseX, mouseY);
	}

	public void renderProgressArrow(PoseStack matrices, int posX, int posY) {
		int progress = this.menu.getScaledProgress(18);
		this.blit(matrices, posX + 93, posY + 32, 178, 20, progress, 25); //Position Arrow
	}

	public void renderBurnIcon(PoseStack matrices, int posX, int posY) {
		if (this.menu.isBeingBurned()) {
			this.blit(matrices, posX + 62, posY + 49, 176, 0, 17, 15); //fire
		}
	}

	@Override
	protected void init() {
		super.init();
		titleLabelX = (imageWidth - font.width(title)) / 2;
	}

	@Override
	public void addToolTips() {
		super.addToolTips();
		final int normalWidthAndHeight = 18;
		final int firstRow = 18;
		final int secondRow = 42;
		final int containerRow = 66;
		final int resultRow = 102;
		final int firstLine = 19;
		addToolTip(new SideToolTip(firstRow, firstLine, normalWidthAndHeight, normalWidthAndHeight,
				Component.translatable("item.vinery.apple_mash")));
		addToolTip(new SideToolTip(secondRow, firstLine, normalWidthAndHeight, normalWidthAndHeight,
				Component.translatable("item.vinery.dough")));
		addToolTip(new SideToolTip(containerRow, firstLine, normalWidthAndHeight, normalWidthAndHeight,
				Component.translatable("item.minecraft.milk_bucket")));
		addToolTip(new SideToolTip(resultRow, firstLine, normalWidthAndHeight, normalWidthAndHeight,
				Component.translatable("block.vinery.apple_pie")));
		final int secondLine = 43;
		addToolTip(new SideToolTip(firstRow, secondLine, normalWidthAndHeight, normalWidthAndHeight,
				Component.translatable("item.minecraft.sugar")));
		addToolTip(new SideToolTip(secondRow, secondLine, normalWidthAndHeight, normalWidthAndHeight,
				Component.translatable("item.vinery.dough")));
		addToolTip(new SideToolTip(containerRow, secondLine, normalWidthAndHeight, normalWidthAndHeight,
				Component.translatable("item.minecraft.cocoa_beans")));
		addToolTip(new SideToolTip(resultRow, secondLine, normalWidthAndHeight, normalWidthAndHeight,
				Component.translatable("item.vinery.chocolate_bread")));
		final int thirdLine = 67;
		addToolTip(new SideToolTip(firstRow, thirdLine, normalWidthAndHeight, normalWidthAndHeight,
				Component.translatable("item.vinery.dough")));
		addToolTip(new SideToolTip(secondRow, thirdLine, normalWidthAndHeight, normalWidthAndHeight,
				Component.translatable("item.vinery.dough")));
		addToolTip(new SideToolTip(containerRow, thirdLine, normalWidthAndHeight, normalWidthAndHeight,
				Component.translatable("item.minecraft.water_bucket")));
		addToolTip(new SideToolTip(resultRow, thirdLine, normalWidthAndHeight, normalWidthAndHeight,
				Component.translatable("block.vinery.crusty_bread")));
		final int fourthLine = 91;
		addToolTip(new SideToolTip(firstRow, fourthLine, normalWidthAndHeight, normalWidthAndHeight,
				Component.translatable("item.minecraft.sweet_berries")));
		addToolTip(new SideToolTip(secondRow, fourthLine, normalWidthAndHeight, normalWidthAndHeight,
				Component.translatable("item.vinery.dough")));
		addToolTip(new SideToolTip(containerRow, fourthLine, normalWidthAndHeight, normalWidthAndHeight,
				Component.translatable("item.minecraft.sugar")));
		addToolTip(new SideToolTip(resultRow, fourthLine, normalWidthAndHeight, normalWidthAndHeight,
				Component.translatable("item.vinery.donut")));
		final int fifthLine = 115;
		addToolTip(new SideToolTip(firstRow, fifthLine, normalWidthAndHeight, normalWidthAndHeight,
				Component.translatable("item.minecraft.milk_bucket")));
		addToolTip(new SideToolTip(secondRow, fifthLine, normalWidthAndHeight, normalWidthAndHeight,
				Component.translatable("item.minecraft.sugar")));
		addToolTip(new SideToolTip(containerRow, fifthLine, normalWidthAndHeight, normalWidthAndHeight,
				Component.translatable("item.vinery.dough")));
		addToolTip(new SideToolTip(resultRow, fifthLine, normalWidthAndHeight, normalWidthAndHeight,
				Component.translatable("item.vinery.milk_bread")));
		final int sixthLine = 139;
		addToolTip(new SideToolTip(secondRow, sixthLine, normalWidthAndHeight, normalWidthAndHeight,
				Component.translatable("item.minecraft.sugar")));
		addToolTip(new SideToolTip(containerRow, sixthLine, normalWidthAndHeight, normalWidthAndHeight,
				Component.translatable("item.vinery.dough")));
		addToolTip(new SideToolTip(resultRow, sixthLine, normalWidthAndHeight, normalWidthAndHeight,
				Component.translatable("item.vinery.toast")));
	}
}
