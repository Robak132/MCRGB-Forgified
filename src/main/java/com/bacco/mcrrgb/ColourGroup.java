package com.bacco.mcrrgb;

import com.bacco.libgui.widget.data.ColourVector;

import java.util.ArrayList;
import java.util.List;

public class ColourGroup {
    public List<ColourVector> pixels = new ArrayList<>();
    public int weight;
    public String meanHex;
    public ColourVector meanColour;
}
