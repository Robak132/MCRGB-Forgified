package io.github.robak132.mcrgb_forge.client.gui.widgets;

import io.github.robak132.libgui_forge.widget.WSprite;
import io.github.robak132.libgui_forge.widget.data.InputResult;
import io.github.robak132.mcrgb_forge.client.gui.BlockGuiDescription;
import net.minecraft.resources.ResourceLocation;

public class WTextureThumbnail extends WSprite {

    int index;
    BlockGuiDescription bigui;

    public WTextureThumbnail(ResourceLocation image, float u1, float v1, float u2, float v2, int i, BlockGuiDescription gui) {
        super(image, u1, v1, u2, v2);
        this.index = i;
        this.bigui = gui;
    }

    @Override
    public InputResult onClick(int x, int y, int button) {
        bigui.changeSprite(index);
        return super.onClick(x, y, button);
    }
}
