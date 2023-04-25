package satisfyu.vinery.compat.rei.press;

import com.google.common.collect.Lists;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.network.chat.Component;
import satisfyu.vinery.registry.VineryBlocks;

import java.util.List;

public class WinePressCategory implements DisplayCategory<WinePressDisplay> {
	@Override
	public CategoryIdentifier<WinePressDisplay> getCategoryIdentifier() {
		return WinePressDisplay.WINE_PRESS_DISPLAY;
	}

	@Override
	public Component getTitle() {
		return Component.translatable("rei.vinery.wine_press_category");
	}

	@Override
	public Renderer getIcon() {
		return EntryStacks.of(VineryBlocks.WINE_PRESS.get());
	}

	@Override
	public List<Widget> setupDisplay(WinePressDisplay display, Rectangle bounds) {
		Point startPoint = new Point(bounds.getCenterX() - 41, bounds.y + 18);
		List<Widget> widgets = Lists.newArrayList();
		widgets.add(Widgets.createRecipeBase(bounds));
		widgets.add(Widgets.createResultSlotBackground(new Point(startPoint.x + 61, startPoint.y)));
		widgets.add(Widgets.createArrow(new Point(startPoint.x + 24, startPoint.y)).animationDurationTicks(72));
		widgets.add(Widgets.createSlot(new Point(startPoint.x + 61, startPoint.y + 1)).entries(
				display.getOutputEntries().get(0)).disableBackground().markOutput());
		widgets.add(Widgets.createSlot(new Point(startPoint.x + 1, startPoint.y + 1))
				.entries(display.getInputEntries().get(0)).markInput());
		return widgets;
	}

	@Override
	public int getDisplayHeight() {
		return 49;
	}
}
