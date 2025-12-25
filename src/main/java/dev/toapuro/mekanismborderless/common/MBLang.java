package dev.toapuro.mekanismborderless.common;

import mekanism.api.text.ILangEntry;
import mekanism.common.Mekanism;
import net.minecraft.Util;
import org.jetbrains.annotations.NotNull;

public enum MBLang implements ILangEntry {
    MEKANISM_BORDERLESS("generic","modid");

    private final String key;
    MBLang(String type, String path) {
        this(Util.makeDescriptionId(type, Mekanism.rl(path)));
    }
    MBLang(String key) {
        this.key = key;
    }
    public @NotNull String getTranslationKey() {
        return this.key;
    }
}
