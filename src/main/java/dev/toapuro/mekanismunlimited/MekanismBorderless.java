package dev.toapuro.mekanismunlimited;

import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod(MekanismBorderless.MODID)
public class MekanismBorderless {

    public static final String MODID = "mekanismborderless";

    @SuppressWarnings("removal")
    public MekanismBorderless() {
        MBClientConfig.initializeConfig();

        ModLoadingContext loadingContext = ModLoadingContext.get();
        loadingContext.registerConfig(ModConfig.Type.CLIENT, MBClientConfig.SPEC);
        loadingContext.registerConfig(ModConfig.Type.SERVER, MBServerConfig.getSpec());
    }
}
