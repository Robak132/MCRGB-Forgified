package io.github.robak132.mcrgb_forge;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class ColourGroup {

    private final List<ColourVector> pixels = new ArrayList<>();
    private int weight;
    private String meanHex;
    private ColourVector meanColour;

    public void addPixel(ColourVector colour) {
        pixels.add(colour);
    }

    /**
     * Computes the mean color, hex value, and weight (%) for this group.
     *
     * @param totalPixelCount total number of non-transparent pixels in the sprite
     */
    public void computeProperties(int totalPixelCount) {
        if (pixels.isEmpty()) {
            this.meanColour = new ColourVector(0, 0, 0);
            this.meanHex = "#000000";
            this.weight = 0;
            return;
        }

        ColourVector sum = new ColourVector(0, 0, 0);
        for (ColourVector c : pixels) {
            sum.add(c);
        }

        this.meanColour = sum.div(pixels.size());
        this.meanHex = meanColour.getHex();
        this.weight = (int)((pixels.size() / (float)totalPixelCount) * 100);
    }

    /**
     * Converts this ColourGroup into a SpriteColour summary.
     * @param totalPixels total non-transparent pixels in sprite
     */
    public SpriteColour toSpriteColour(int totalPixels) {
        computeProperties(totalPixels);
        return new SpriteColour(meanColour, weight);
    }
}
