package com.bacco.mcrrgb;

import com.google.gson.Gson;

import static com.bacco.mcrrgb.MCRGBClient.readJson;
import static com.bacco.mcrrgb.MCRGBClient.writeJson;

public final class MCRGBConfig {

    public static MCRGBConfig instance = new MCRGBConfig();
    public boolean alwaysShowToolTips = false;
    public boolean sliderConstantUpdate = true;
    public boolean bypassOP = false;
    public String command = "give %p %i%c %q";

    public static void save(){
        Gson gson = new Gson();
        String blockColoursJson = gson.toJson(instance);
        writeJson(blockColoursJson, "./config/mcrgb/", "config.json");
    }

    public static void load(){
        Gson gson = new Gson();
        instance = gson.fromJson(readJson("./config/mcrgb/config.json"), MCRGBConfig.class);
        if(instance == null){
            instance = new MCRGBConfig();
            save();
        }

    }
}
