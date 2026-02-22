package dev.toapuro.mekanismborderless.mixin.accessor;

import mekanism.common.lib.math.voxel.VoxelCuboid;
import mekanism.common.lib.multiblock.CuboidStructureValidator;
import mekanism.common.lib.multiblock.FormationProtocol;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = CuboidStructureValidator.class, remap = false)
public interface CuboidStructureValidatorAccessor {

    @Accessor("maxBounds")
    VoxelCuboid getMaxBounds();

    @Accessor("cuboid")
    void setCuboid(VoxelCuboid cuboid);

    @Invoker("getStructureRequirement")
    FormationProtocol.StructureRequirement invokeGetStructureRequirement(BlockPos pos);
}
