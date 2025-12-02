package io.github.cottonmc.cotton.gui.impl.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import io.github.cottonmc.cotton.gui.impl.LibGuiCommon;
import net.fabricmc.fabric.api.client.rendering.v1.CoreShaderRegistrationCallback;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public final class LibGuiShaders {
    private static @Nullable ShaderInstance tiledRectangle;

    static void register() {
        // Register our core shaders.
        // The tiled rectangle shader is used for performant tiled texture rendering.
        CoreShaderRegistrationCallback.EVENT.register(
            context -> context.register(new ResourceLocation(LibGuiCommon.MOD_ID, "tiled_rectangle"),
                DefaultVertexFormat.POSITION,
                program -> tiledRectangle = program));
    }

    private static ShaderInstance assertPresent(ShaderInstance program, String name) {
        if (program == null) {
            throw new NullPointerException("Shader libgui:" + name + " not initialised!");
        }

        return program;
    }

    public static ShaderInstance getTiledRectangle() {
        return assertPresent(tiledRectangle, "tiled_rectangle");
    }
}
