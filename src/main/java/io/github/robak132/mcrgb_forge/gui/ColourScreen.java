package io.github.robak132.mcrgb_forge.gui;

import io.github.robak132.libgui_forge.GuiDescription;
import io.github.robak132.libgui_forge.client.CottonClientScreen;

public class ColourScreen extends CottonClientScreen {

    public ColourScreen(GuiDescription description) {
        super(description);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
