package dev.toapuro.mekanismborderless.mixin.multiblock.validator;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.toapuro.mekanismborderless.mixin.accessor.CuboidStructureValidatorAccessor;
import dev.toapuro.mekanismborderless.util.MBStructureHelper;
import mekanism.common.content.sps.SPSMultiblockData;
import mekanism.common.content.sps.SPSValidator;
import mekanism.common.lib.math.voxel.VoxelCuboid;
import mekanism.common.lib.multiblock.CuboidStructureValidator;
import mekanism.common.lib.multiblock.FormationProtocol.StructureRequirement;
import mekanism.common.lib.multiblock.Structure;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

@Mixin(value = SPSValidator.class, remap = false)
@Debug(export = true)
public abstract class MixinSPSValidator extends CuboidStructureValidator<SPSMultiblockData> {

    @Inject(method = "getStructureRequirement", at = @At("HEAD"), cancellable = true)
    private void replaceStructureRequirement(BlockPos pos, CallbackInfoReturnable<StructureRequirement> cir) {
        VoxelCuboid.WallRelative relative = this.cuboid.getWallRelative(pos);
        int size = cuboid.length();

        Direction side = this.cuboid.getSide(pos);
        BlockPos relativePos = pos.subtract(this.cuboid.getMinPos());

        if (side != null) {
            if(!relative.isWall()) return;

            Structure.Axis axis = Structure.Axis.get(side);

            Structure.Axis h = axis.horizontal();
            Structure.Axis v = axis.vertical();

            int hPos = Math.min(h.getCoord(relativePos), size-1);
            int vPos = Math.min(v.getCoord(relativePos), size-1);

            byte requirement = MBStructureHelper.getCircleStructureRequirement(hPos, vPos, size, size - 3);

            cir.setReturnValue(StructureRequirement.REQUIREMENTS[requirement]);
        }
    }

    @ModifyReturnValue(method = "precheck", at = @At("RETURN"))
    public boolean precheck(boolean original) {
        if(cuboid != null) {
            int xSize = cuboid.length();
            int ySize = cuboid.height();
            int zSize = cuboid.width();
            if (xSize != ySize || ySize != zSize) {
                return false;
            }
            if(xSize % 2 == 0) {
                return false;
            }
        }

        return original;
    }

    @Redirect(method = "precheck", at = @At(value = "INVOKE", target = "Lmekanism/common/lib/multiblock/StructureHelper;fetchCuboid(Lmekanism/common/lib/multiblock/Structure;Lmekanism/common/lib/math/voxel/VoxelCuboid;Lmekanism/common/lib/math/voxel/VoxelCuboid;Ljava/util/Set;I)Lmekanism/common/lib/math/voxel/VoxelCuboid;"))
    public VoxelCuboid precheck(Structure structure, VoxelCuboid minBounds, VoxelCuboid maxBounds, Set<VoxelCuboid.CuboidSide> sides, int tolerance) {
        CuboidStructureValidatorAccessor self = (CuboidStructureValidatorAccessor) this;
        return MBStructureHelper.fetchCuboid(structure, minBounds, self.getMaxBounds(), Integer.MAX_VALUE);
    }
}
