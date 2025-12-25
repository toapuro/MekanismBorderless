package dev.toapuro.mekanismunlimited.mixin.multiblock.data;

import mekanism.common.lib.math.voxel.VoxelCuboid;
import mekanism.common.lib.multiblock.MultiblockData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = MultiblockData.class, remap = false)
public abstract class MixinMultiblockData {

    @Shadow public abstract int getVolume();

    @Shadow public abstract VoxelCuboid getBounds();

    @Inject(method = "setVolume", at = @At("HEAD"))
    public void setVolume(int volume, CallbackInfo ci) {
    }
}
