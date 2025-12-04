package com.bacco.mcrgb_forge;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod("mcrgb_forge")
public class MCRGB {
    public static final String MCRGB_MOD_ID = "mcrgb_forge";
    public static final Logger LOGGER = LoggerFactory.getLogger(MCRGB_MOD_ID);

    public MCRGB() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        MinecraftForge.EVENT_BUS.register(this);
    }
}