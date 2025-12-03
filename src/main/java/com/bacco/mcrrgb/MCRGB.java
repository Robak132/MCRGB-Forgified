package com.bacco.mcrrgb;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod("mcrgb")
public class MCRGB {
    public static final String MODID = "mcrgb";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    public MCRGB() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        // Client setup is handled in MCRGBClient via @EventBusSubscriber(Dist.CLIENT)
        // Register forge event listeners if needed
        MinecraftForge.EVENT_BUS.register(this);
    }
}