package com.bacco.gui;

import com.bacco.ColourVector;
import com.bacco.MCRGBClient;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WSprite;
import io.github.cottonmc.cotton.gui.widget.WTextField;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class MCRGBBaseGui extends LightweightGuiDescription {

    WGridPanel root = new WGridPanel();
    WGridPanel mainPanel = new WGridPanel();

    WSavedPalettesArea savedPalettesArea;

    WTextField hexInput = new WTextField(Component.literal("#FFFFFF"));

    ColourVector inputColour = new ColourVector(255, 255, 255);

    Minecraft client;
    MCRGBClient mcrgbClient;

    WSprite colourDisplay = new WSprite(ResourceLocation.tryBuild("mcrgb", "rect.png"));

    MCRGBBaseGui() {
    }

    int getColour() {
        String hex = inputColour.getHex().replace("#", "");
        return Integer.parseInt(hex, 16);
    }

    void setColour(ColourVector colour) {
        inputColour = colour;
        hexInput.setText(inputColour.getHex());
        colourDisplay.setOpaqueTint(inputColour.asInt());
    }
}
