package dev.toapuro.mekanismunlimited;

import net.minecraftforge.common.ForgeConfigSpec;

public class MBClientConfig {
    public static ForgeConfigSpec SPEC;
    public static ForgeConfigSpec.BooleanValue FILTER_TICK;
    public static ForgeConfigSpec.ConfigValue<Float> FUSION_CORE_SIZE_MULTIPLIER;

    public static void initializeConfig() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        FILTER_TICK = builder
                .comment("Reduce multiblock client ticks (unstable)")
                .define("reduceOverhead", false);
        FUSION_CORE_SIZE_MULTIPLIER = builder
                .comment("Joke")
                .define("fusionCoreSizeMultiplier", 1.0f);

        SPEC = builder.build();
    }
}
