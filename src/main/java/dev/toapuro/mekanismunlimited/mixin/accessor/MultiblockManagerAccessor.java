package dev.toapuro.mekanismunlimited.mixin.accessor;

import mekanism.common.lib.multiblock.MultiblockManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Set;

@Mixin(value = MultiblockManager.class, remap = false)
public interface MultiblockManagerAccessor {

    @Accessor("managers")
    static Set<MultiblockManager<?>> getStaticManagers() {
        throw new UnsupportedOperationException();
    }
}
