package dev.toapuro.mekanismunlimited.mixin.accessor;

import mekanism.common.lib.math.voxel.VoxelCuboid;
import mekanism.common.lib.multiblock.CuboidStructureValidator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = CuboidStructureValidator.class, remap = false)
public interface CuboidStructureValidatorAccessor {

    @Accessor("maxBounds")
    VoxelCuboid getMaxBounds();
}
