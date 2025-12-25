package dev.toapuro.mekanismborderless.mixin.multiblock.validator;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import dev.toapuro.mekanismborderless.mixin.accessor.CuboidStructureValidatorAccessor;
import dev.toapuro.mekanismborderless.util.MBStructureHelper;
import mekanism.common.lib.math.voxel.VoxelCuboid;
import mekanism.common.lib.multiblock.CuboidStructureValidator;
import mekanism.common.lib.multiblock.FormationProtocol;
import mekanism.common.lib.multiblock.Structure;
import mekanism.generators.common.content.fusion.FusionReactorMultiblockData;
import mekanism.generators.common.content.fusion.FusionReactorValidator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

@Mixin(value = FusionReactorValidator.class, remap = false)
public abstract class MixinFusionReactorValidator extends CuboidStructureValidator<FusionReactorMultiblockData> {

    @Inject(method = "getStructureRequirement", at = @At("HEAD"), cancellable = true)
    private void replaceStructureRequirement(BlockPos pos, CallbackInfoReturnable<FormationProtocol.StructureRequirement> cir) {
        VoxelCuboid.WallRelative relative = this.cuboid.getWallRelative(pos);
        int size = cuboid.length();
        int medium = size / 2;

        Direction side = this.cuboid.getSide(pos);
        BlockPos relativePos = pos.subtract(this.cuboid.getMinPos());

        Structure.Axis axis = side != null ? Structure.Axis.get(side) : Structure.Axis.Y;

        Structure.Axis h = axis.horizontal();
        Structure.Axis v = axis.vertical();

        int axisPos = Math.min(axis.getCoord(relativePos), size-1);
        int hPos = Math.min(h.getCoord(relativePos), size-1);
        int vPos = Math.min(v.getCoord(relativePos), size-1);

        if (side != null) {
            if(!relative.isWall()) {
                cir.setReturnValue(FormationProtocol.StructureRequirement.IGNORED);
                return;
            }

            byte requirement = MBStructureHelper.getCircleStructureRequirement(hPos, vPos, size, size / 2);

            cir.setReturnValue(FormationProtocol.StructureRequirement.REQUIREMENTS[requirement]);
        } else {
            int width = medium - Math.abs(medium - axisPos);

            byte requirement = MBStructureHelper.getCircleStructureRequirement(hPos, vPos, size, size / 2 + width);

            if(requirement == 1) {
                cir.setReturnValue(FormationProtocol.StructureRequirement.OTHER);
            }
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

    @ModifyVariable(method = "validateFrame", at = @At(value = "STORE"), name = "isControllerPos")
    public boolean validate(boolean isControllerPos, @Local(argsOnly = true) BlockPos pos) {
        int xSize = cuboid.length();
        int medium = xSize / 2;

        return pos.getY() == this.cuboid.getMaxPos().getY() &&
                pos.getX() == this.cuboid.getMinPos().getX() + medium &&
                pos.getZ() == this.cuboid.getMinPos().getZ() + medium;
    }
}
