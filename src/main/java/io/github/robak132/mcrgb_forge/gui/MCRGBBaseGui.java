package io.github.robak132.mcrgb_forge.gui;

import static io.github.robak132.mcrgb_forge.MCRGB.MOD_ID;

import io.github.robak132.libgui_forge.LightweightGuiDescription;
import io.github.robak132.libgui_forge.widget.WGridPanel;
import io.github.robak132.libgui_forge.widget.WSprite;
import io.github.robak132.libgui_forge.widget.WTextField;
import io.github.robak132.mcrgb_forge.ColourVector;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public abstract class MCRGBBaseGui extends LightweightGuiDescription {

    static final int SLOTS_HEIGHT = 7;
    static final int SLOTS_WIDTH = 9;
    public final WGridPanel root = new WGridPanel();
    public final WGridPanel mainPanel = new WGridPanel();
    public final WSavedPalettesArea savedPalettesArea = new WSavedPalettesArea(this, 9, 7);
    public final WTextField hexInput = new WTextField(Component.literal("#FFFFFF"));
    public ColourVector inputColour = new ColourVector(255, 255, 255);
    public final WSprite colourDisplay = new WSprite(ResourceLocation.fromNamespaceAndPath(MOD_ID, "rect.png"));

    public int getColour() {
        String hex = inputColour.getHex().replace("#", "");
        return Integer.parseInt(hex, 16);
    }

    public void setColour(ColourVector colour) {
        inputColour = colour;
        hexInput.setText(inputColour.getHex());
        colourDisplay.setOpaqueTint(inputColour.asInt());
    }
}
