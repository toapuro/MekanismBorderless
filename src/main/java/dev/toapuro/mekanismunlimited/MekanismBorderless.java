package dev.toapuro.mekanismunlimited;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(MekanismBorderless.MODID)
public class MekanismBorderless {

    public static final String MODID = "mekanismborderless";

    @SuppressWarnings("removal")
    public MekanismBorderless() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        MBConfig.initializeConfig();

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, MBConfig.SPEC);
    }
}
