package io.github.robak132.mcrgb_forge;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class ColourGroup {
    List<ColourVector> pixels = new ArrayList<>();
    int weight;
    String meanHex;
    ColourVector meanColour;

    public void addPixel(ColourVector colour) {
        pixels.add(colour);
    }
}
