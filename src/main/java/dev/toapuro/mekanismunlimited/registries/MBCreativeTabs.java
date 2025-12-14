package dev.toapuro.mekanismunlimited.registries;

import dev.toapuro.mekanismunlimited.MekanismBorderless;
import dev.toapuro.mekanismunlimited.common.MBLang;
import mekanism.common.registration.impl.CreativeTabDeferredRegister;
import mekanism.common.registration.impl.CreativeTabRegistryObject;

public class MBCreativeTabs {
    public static final CreativeTabDeferredRegister CREATIVE_TABS = new CreativeTabDeferredRegister(MekanismBorderless.MODID);
    public static final CreativeTabRegistryObject MEKBORDERLESS;


    static {
        MEKBORDERLESS = CREATIVE_TABS.registerMain(MBLang.MEKANISM_BORDERLESS, MBBlocks.MAINTENANCE_PORT,
                (builder) -> builder.withSearchBar().displayItems((displayParameters, output) -> {
                    CreativeTabDeferredRegister.addToDisplay(MBBlocks.BLOCKS, output);
                }));
    }
}
