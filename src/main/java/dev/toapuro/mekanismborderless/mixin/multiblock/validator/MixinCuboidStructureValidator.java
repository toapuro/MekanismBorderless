package dev.toapuro.mekanismborderless.mixin.multiblock.validator;

import dev.toapuro.mekanismborderless.MBServerConfig;
import mekanism.common.lib.math.voxel.VoxelCuboid;
import mekanism.common.lib.multiblock.CuboidStructureValidator;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = CuboidStructureValidator.class, remap = false)
@Debug(export = true)
public class MixinCuboidStructureValidator {

    @Mutable @Shadow @Final private VoxelCuboid maxBounds;

    @Inject(method = "<init>(Lmekanism/common/lib/math/voxel/VoxelCuboid;Lmekanism/common/lib/math/voxel/VoxelCuboid;)V", at = @At("TAIL"))
    public void init(CallbackInfo ci) {
        VoxelCuboid structureBound = MBServerConfig.getStructureCuboid(this.getClass());
        if(structureBound == null) return;

        BlockPos maxPos = structureBound.getMaxPos();
        this.maxBounds = new VoxelCuboid(maxPos.getX(), maxPos.getY(), maxPos.getZ());
    }
}
