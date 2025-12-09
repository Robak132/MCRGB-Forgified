package io.github.robak132.mcrgb_forge;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class Palette {
    List<ColourVector> colourList = new ArrayList<>();

    public void setColour(int i, ColourVector colour){
        colourList.set(i,colour);
    }

    public ColourVector getColour(int i){
        return colourList.get(i);
    }

    public void addColour(ColourVector colour){
        colourList.add(colour);
    }
}
