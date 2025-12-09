package io.github.robak132.mcrgb_forge.gui;

import io.github.robak132.mcrgb_forge.ColourVector;
import io.github.robak132.libgui_forge.widget.WSprite;
import io.github.robak132.libgui_forge.widget.data.InputResult;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.resources.ResourceLocation;


public class WColourPreviewIcon extends WSprite {

    int colour = 0xFFFFFF;
    MCRGBBaseGui gui;
    @Setter
    @Getter
    boolean interactable = true;

    public WColourPreviewIcon(ResourceLocation image, MCRGBBaseGui gui) {
        super(image);
        this.gui = gui;
    }

    @Override
    public InputResult onClick(int x, int y, int button) {
        switch (button) {
            case 0:
                if (!interactable) return InputResult.PROCESSED;
                colour = gui.getColour();
                setOpaqueTint(colour);
                break;
            case 1:
                if (!interactable) return InputResult.PROCESSED;
                colour = 0xFFFFFF;
                setOpaqueTint(colour);
                break;
            case 2:
                gui.setColour(new ColourVector(colour));
                break;
        }
        return InputResult.PROCESSED;
    }

    public void setColour(int colour) {
        this.colour = colour;
        setOpaqueTint(colour);
    }

}
