package com.bacco.event;

import com.bacco.ColourVector;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

import com.bacco.MCRGBClient;
import com.bacco.gui.ColourGui;
import com.bacco.gui.ColourScreen;

import static net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.END_CLIENT_TICK;


public class KeyInputHandler {
    public static final String KEY_CATEGORY_MCRGB = "key.category.mcrgb.mcrgb";
    public static final String KEY_COLOUR_INV_OPEN = "key.mcrgb.colour_inv_open";

    public static KeyMapping colourInvKey;


    public static void registerKeyInputs(MCRGBClient mcrgbClient){
        END_CLIENT_TICK.register(client ->{
            if (colourInvKey.consumeClick()){
                if (client.screen == null) {
                    client.setScreen(new ColourScreen(new ColourGui(client, mcrgbClient, new ColourVector(0xFFFFFFFF))));
				} else {
					client.setScreen(null);
				}
            }
        });
    }

    public static void register(MCRGBClient mcrgbClient){
        colourInvKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
            KEY_COLOUR_INV_OPEN,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_I,
            KEY_CATEGORY_MCRGB
            ));
            registerKeyInputs(mcrgbClient);
    }
}
