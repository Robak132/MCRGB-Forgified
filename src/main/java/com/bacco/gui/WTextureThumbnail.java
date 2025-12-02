package com.bacco.gui;

import io.github.cottonmc.cotton.gui.widget.WSprite;
import io.github.cottonmc.cotton.gui.widget.data.InputResult;
import net.minecraft.resources.ResourceLocation;

public class WTextureThumbnail extends WSprite {

    int index;
    BlockInfoGui bigui;

    public WTextureThumbnail(ResourceLocation image, float u1, float v1, float u2, float v2, int i, BlockInfoGui gui) {
        super(image, u1, v1, u2, v2);
        this.index = i;
        this.bigui = gui;
    }

    @Override
    public InputResult onClick(int x, int y, int button) {
        bigui.ChangeSprite(index);
        return super.onClick(x, y, button);
    }
}
