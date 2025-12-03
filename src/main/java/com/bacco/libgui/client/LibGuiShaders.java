package com.bacco.libgui.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public final class LibGuiShaders {
    private static @Nullable ShaderInstance tiledRectangle;

    @SubscribeEvent
    public static void onRegisterShaders(RegisterShadersEvent event) {
        try {
            ShaderInstance shaderInstance = new ShaderInstance(event.getResourceProvider(), new ResourceLocation(LibGuiCommon.MOD_ID, "tiled_rectangle"), DefaultVertexFormat.POSITION_COLOR_TEX);
            event.registerShader(shaderInstance, shader -> tiledRectangle = shader);
        } catch (IOException e) {
            throw new RuntimeException("Failed to register libgui:tiled_rectangle shader");
        }
    }

    public static ShaderInstance getTiledRectangle() {
        if (tiledRectangle == null) {
            throw new NullPointerException("Shader libgui: tiled_rectangle not initialised!");
        }
        return tiledRectangle;
    }
}
