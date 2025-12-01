package com.bacco;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.function.Function;

public class ClothConfigIntegration {
    public static Screen getConfigScreen(Screen parent) {
        return Internal.getConfigScreen();
    }

    private static class Internal {
        private static final Function<Boolean, Component> alwaysShowToolTipsTextSupplier = bool -> {
            if (bool) return Component.translatable("options.mcrgb.all_contexts");
            else return Component.translatable("options.mcrgb.picker_only");
        };
        private static final Function<Boolean, Component> sliderConstantUpdateTextSupplier = bool -> {
            if (bool) return Component.translatable("options.mcrgb.while_scrolling");
            else return Component.translatable("options.mcrgb.after_scrolling");
        };
        protected static Screen getConfigScreen() {
            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(Minecraft.getInstance().screen)
                    .setTitle(Component.translatable("title.mcrgb.config"))
                    .setDoesConfirmSave(true);

            ConfigCategory configs = builder.getOrCreateCategory(Component.translatable("options.mcrgb.category.configs"));
            ConfigEntryBuilder entryBuilder = builder.entryBuilder();

            configs.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.mcrgb.always_show_in_tooltips"), MCRGBConfig.instance.alwaysShowToolTips)
                    .setDefaultValue(false)
                    .setYesNoTextSupplier(alwaysShowToolTipsTextSupplier)
                    .setSaveConsumer(newValue -> MCRGBConfig.instance.alwaysShowToolTips = newValue)
                    .setTooltip(Component.translatable("tooltip.mcrgb.always_show_in_tooltips"))
                    .build());

            configs.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.mcrgb.slider_constant_update"), MCRGBConfig.instance.sliderConstantUpdate)
                    .setDefaultValue(true)
                    .setYesNoTextSupplier(sliderConstantUpdateTextSupplier)
                    .setSaveConsumer(newValue -> MCRGBConfig.instance.sliderConstantUpdate = newValue)
                    .setTooltip(Component.translatable("tooltip.mcrgb.slider_constant_update"))
                    .build());

            configs.addEntry(entryBuilder.startStrField(Component.translatable("option.mcrgb.give_command"),MCRGBConfig.instance.command)
                    .setDefaultValue("give %p %i%c %q")
                    .setSaveConsumer(newValue -> MCRGBConfig.instance.command = newValue)
                    .setTooltip(Component.translatable("tooltip.mcrgb.give_command"))
                    .build());

            configs.addEntry(entryBuilder.startBooleanToggle(Component.translatable("Bypass Operator Check"),MCRGBConfig.instance.bypassOP)
                    .setDefaultValue(false)
                    .setSaveConsumer(newValue -> MCRGBConfig.instance.bypassOP = newValue)
                    .setTooltip(Component.translatable("tooltip.mcrgb.bypass_op"))
                    .build());


            builder.setSavingRunnable(MCRGBConfig::save);


            return builder.build();
        }
    }
}

