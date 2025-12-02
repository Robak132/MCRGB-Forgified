package io.github.cottonmc.cotton.gui.impl.client;

import io.github.cottonmc.cotton.gui.impl.ScreenNetworkingImpl;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Jankson;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.JsonElement;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.JsonObject;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class LibGuiClient implements ClientModInitializer {
    public static final Logger logger = LogManager.getLogger();
    public static final Jankson jankson = Jankson.builder().build();
    public static volatile LibGuiConfig config;

    public static LibGuiConfig loadConfig() {
        try {
            Path file = FabricLoader.getInstance().getConfigDir().resolve("libgui.json5");

            if (Files.notExists(file)) saveConfig(new LibGuiConfig());

            JsonObject json;
            try (InputStream in = Files.newInputStream(file)) {
                json = jankson.load(in);
            }

            config = jankson.fromJson(json, LibGuiConfig.class);
        } catch (Exception e) {
            logger.error("[LibGui] Error loading config: {}", e.getMessage());
        }
        return config;
    }

    public static void saveConfig(LibGuiConfig config) {
        try {
            Path file = FabricLoader.getInstance().getConfigDir().resolve("libgui.json5");
            JsonElement json = jankson.toJson(config);
            String result = json.toJson(true, true);
            Files.writeString(file, result);
        } catch (Exception e) {
            logger.error("[LibGui] Error saving config: {}", e.getMessage());
        }
    }

    @Override
    public void onInitializeClient() {
        config = loadConfig();

        ClientPlayNetworking.registerGlobalReceiver(ScreenNetworkingImpl.SCREEN_MESSAGE_S2C, (client, networkHandler, buf, responseSender) -> {
            ScreenNetworkingImpl.handle(client, client.player, buf);
        });

        LibGuiShaders.register();
    }
}
