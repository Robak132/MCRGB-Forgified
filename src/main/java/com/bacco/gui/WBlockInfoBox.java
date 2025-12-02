package com.bacco.gui;

import com.bacco.ColourVector;
import com.bacco.IItemBlockColourSaver;
import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.widget.WBox;
import io.github.cottonmc.cotton.gui.widget.data.CottonAxis;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;

import java.util.List;

public class WBlockInfoBox extends WBox {

    public WBlockInfoBox(CottonAxis axis, IItemBlockColourSaver item, MCRGBBaseGui gui) {
        super(axis);
        setInsets(Insets.ROOT_PANEL);
        for (int i = 0; i < item.getLength(); i++) {
            List<String> strings = item.getSpriteDetails(i).getStrings();
            List<Integer> colours = item.getSpriteDetails(i).getTextColours();
            if (!strings.isEmpty()) {
                for (int j = 0; j < strings.size(); j++) {
                    var text = Component.literal(strings.get(j));
                    MutableComponent text2 = (MutableComponent) Component.literal("⬛").toFlatList(Style.EMPTY.withColor(colours.get(j))).get(0);
                    if (j > 0) {
                        text2.append(text.toFlatList(Style.EMPTY.withColor(0x707070)).get(0));
                    } else {
                        text2 = (MutableComponent) text.toFlatList(Style.EMPTY.withColor(0x444444)).get(0);
                    }
                    Font textRenderer = Minecraft.getInstance().font;
                    int width = textRenderer.width(text2);
                    WClickableLabel newLabel = new WClickableLabel(text2, new ColourVector(colours.get(j)), gui);
                    newLabel.hoveredProperty();
                    add(newLabel, width, 1);
                    lineCount++;
                }
            }
        }

        setSize(10, this.getWidth());
    }

    /**
     * Constructs a box.
     *
     * @param axis the box axis
     * @throws NullPointerException if the axis is null
     */

    @Override
    public void paint(GuiGraphics context, int x, int y, int mouseX, int mouseY) {
        setBackgroundPainter(BackgroundPainter.VANILLA);
        super.paint(context, x, y, mouseX, mouseY);
    }
}