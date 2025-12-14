package dev.toapuro.mekanismunlimited.mixin.multiblock;

import dev.toapuro.mekanismunlimited.MBConfig;
import mekanism.common.lib.math.voxel.VoxelCuboid;
import mekanism.common.lib.multiblock.CuboidStructureValidator;
import mekanism.common.lib.multiblock.MultiblockData;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = CuboidStructureValidator.class, remap = false)
@Debug(export = true)
public class MixinCuboidStructureValidator<T extends MultiblockData> {
    @Mutable @Shadow @Final private VoxelCuboid maxBounds;

    @Inject(method = "<init>(Lmekanism/common/lib/math/voxel/VoxelCuboid;Lmekanism/common/lib/math/voxel/VoxelCuboid;)V", at = @At("TAIL"))
    public void init(CallbackInfo ci) {
        this.maxBounds = new VoxelCuboid(MBConfig.getMaxXSize(), MBConfig.getMaxYSize(), MBConfig.getMaxZSize());
    }
}
