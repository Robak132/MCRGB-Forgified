package io.github.robak132.mcrgb_forge;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class SpriteDetails {
    private String name;
    private final List<SpriteColour> colours = new ArrayList<>();

    public void add(SpriteColour colour) {
        colours.add(colour);
    }

    /**
     * Builds user-friendly tooltip strings.
     */
    public List<String> getStrings() {
        List<String> strings = new ArrayList<>();
        strings.add(name + ":");

        for (SpriteColour sc : colours) {
            strings.add(sc.color().getHex() + "  " + sc.weight() + "%");
        }

        return strings;
    }

    /**
     * Returns ARGB ints for color preview squares.
     */
    public List<Integer> getTextColours() {
        List<Integer> list = new ArrayList<>();
        list.add(0xFFFFFF); // Title color

        for (SpriteColour sc : colours) {
            String hex = sc.color().getHex().substring(1);
            list.add(Integer.parseInt(hex, 16));
        }

        return list;
    }
}
