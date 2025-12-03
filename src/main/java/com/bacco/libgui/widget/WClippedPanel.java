package com.bacco.libgui.widget;

import net.minecraft.client.gui.GuiGraphics;

import com.bacco.libgui.client.Scissors;

/**
 * A panel that is clipped to only render widgets inside its bounds.
 */
public class WClippedPanel extends WPanel {
		@Override
	public void paint(GuiGraphics context, int x, int y, int mouseX, int mouseY) {
		if (getBackgroundPainter()!=null) getBackgroundPainter().paintBackground(context, x, y, this);

		Scissors.push(x, y, width, height);
		for(WWidget child : children) {
			child.paint(context, x + child.getX(), y + child.getY(), mouseX-child.getX(), mouseY-child.getY());
		}
		Scissors.pop();
	}
}
