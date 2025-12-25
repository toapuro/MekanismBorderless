package dev.toapuro.mekanismunlimited.mixin.multiblock.validator;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.toapuro.mekanismunlimited.mixin.accessor.CuboidStructureValidatorAccessor;
import mekanism.common.content.evaporation.EvaporationMultiblockData;
import mekanism.common.content.evaporation.EvaporationValidator;
import mekanism.common.lib.math.voxel.VoxelCuboid;
import mekanism.common.lib.multiblock.CuboidStructureValidator;
import mekanism.common.lib.multiblock.Structure;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Set;

@Mixin(value = EvaporationValidator.class, remap = false)
public abstract class MixinEvaporationValidator extends CuboidStructureValidator<EvaporationMultiblockData> {

    @WrapOperation(method = "precheck", at = @At(value = "INVOKE", target = "Lmekanism/common/lib/multiblock/StructureHelper;fetchCuboid(Lmekanism/common/lib/multiblock/Structure;Lmekanism/common/lib/math/voxel/VoxelCuboid;Lmekanism/common/lib/math/voxel/VoxelCuboid;Ljava/util/Set;I)Lmekanism/common/lib/math/voxel/VoxelCuboid;"))
    public VoxelCuboid precheck(Structure structure, VoxelCuboid minBounds, VoxelCuboid maxBounds, Set<VoxelCuboid.CuboidSide> sides, int tolerance, Operation<VoxelCuboid> original) {
        CuboidStructureValidatorAccessor self = (CuboidStructureValidatorAccessor) this;
        return original.call(structure, minBounds, self.getMaxBounds(), sides, Integer.MAX_VALUE);
    }
}
