package dev.toapuro.mekanismunlimited;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MekanismBorderless.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MBConfig {
    /**
     * -1 for Integer.MAX_VALUE
     * Default:
     * structureMaxXSize = 1290
     * structureMaxYSize = 1290
     * structureMaxZSize = 1290
     */
    private static ForgeConfigSpec.ConfigValue<Integer> CONFIG_MAX_X_SIZE;
    private static ForgeConfigSpec.ConfigValue<Integer> CONFIG_MAX_Y_SIZE;
    private static ForgeConfigSpec.ConfigValue<Integer> CONFIG_MAX_Z_SIZE;

    public static ForgeConfigSpec SPEC;

    public static void initializeConfig() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.comment("-1 represents Integer.MAX_VALUE");
        CONFIG_MAX_X_SIZE = builder.comment("Max structure X size").define("structureMaxXSize", 1290);
        CONFIG_MAX_Y_SIZE = builder.comment("Max structure Y size").define("structureMaxYSize", 1290);
        CONFIG_MAX_Z_SIZE = builder.comment("Max structure Z size").define("structureMaxZSize", 1290);

        SPEC = builder.build();
    }

    public static int getMaxXSize() {
        int max = CONFIG_MAX_X_SIZE.get();
        return max != -1 ? max : Integer.MAX_VALUE;
    }

    public static int getMaxYSize() {
        int max = CONFIG_MAX_Y_SIZE.get();
        return max != -1 ? max : Integer.MAX_VALUE;
    }

    public static int getMaxZSize() {
        int max = CONFIG_MAX_Z_SIZE.get();
        return max != -1 ? max : Integer.MAX_VALUE;
    }
}
