package io.github.robak132.mcrgb_forge;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;


@Data
public class SpriteDetails {
    String name;
    List<ColourVector> colourInfo = new ArrayList<>();
    List<Integer> weights = new ArrayList<>();

    public List<String> getStrings() {
        List<String> strings = new ArrayList<>();
        strings.add(name + ":");
        for (int i = 0; i < colourInfo.size(); i++) {
            strings.add(colourInfo.get(i).getHex() + "  " + weights.get(i) + "%");
        }
        return strings;
    }

    public List<Integer> getTextColours() {
        List<Integer> colours = new ArrayList<>();
        colours.add(0xffffff);
        for (ColourVector colourVector : colourInfo) {
            String hex = colourVector.getHex();
            hex = hex.replace("#", "");
            int hexint = Integer.parseInt(hex, 16);
            colours.add(hexint);
        }
        return colours;
    }

    public void addColour(ColourVector colour) {
        colourInfo.add(colour);
    }

    public void addWeight(int weight) {
        weights.add(weight);
    }

    public Integer getWeight(int index) {
        return weights.get(index);
    }
}
