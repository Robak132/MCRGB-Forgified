package com.bacco.mcrgb_forge;

import com.bacco.libgui.widget.data.ColourVector;

import java.util.ArrayList;

public class Palette {
    ArrayList<ColourVector> colourList = new ArrayList<>();

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
