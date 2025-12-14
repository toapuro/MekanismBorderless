package dev.toapuro.mekanismunlimited.mixin.multiblock;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import dev.toapuro.mekanismunlimited.MBConfig;
import dev.toapuro.mekanismunlimited.block.attribute.AttributeCasing;
import dev.toapuro.mekanismunlimited.block.attribute.AttributeRepairing;
import dev.toapuro.mekanismunlimited.structure.IAdditionalFormation;
import dev.toapuro.mekanismunlimited.structure.IAdditionalRepairableMultiblock;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import mekanism.common.block.attribute.Attribute;
import mekanism.common.lib.math.voxel.VoxelCuboid;
import mekanism.common.lib.multiblock.CuboidStructureValidator;
import mekanism.common.lib.multiblock.FormationProtocol;
import mekanism.common.lib.multiblock.MultiblockData;
import mekanism.common.lib.multiblock.Structure;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(value = CuboidStructureValidator.class, remap = false)
@Debug(export = true)
public class MixinCuboidStructureValidator<T extends MultiblockData> {
    @Mutable @Shadow @Final private VoxelCuboid maxBounds;
    @Shadow protected Structure structure;

    @Inject(method = "<init>(Lmekanism/common/lib/math/voxel/VoxelCuboid;Lmekanism/common/lib/math/voxel/VoxelCuboid;)V", at = @At("TAIL"))
    public void init(CallbackInfo ci) {
        this.maxBounds = new VoxelCuboid(MBConfig.getMaxXSize(), MBConfig.getMaxYSize(), MBConfig.getMaxZSize());
    }

    @Inject(method = "validate", at = @At(value = "HEAD"))
    public void startValidate(FormationProtocol<T> ctx, Long2ObjectMap<ChunkAccess> chunkMap, CallbackInfoReturnable<FormationProtocol.FormationResult> cir) {
        MultiblockData multiblockData = structure.getMultiblockData();
        if(multiblockData instanceof IAdditionalRepairableMultiblock repairableMultiblock) {
            repairableMultiblock.mekborderless$setAdditionalRepairRateSup(() -> 0);
        }
        IAdditionalFormation additionalFormation = (IAdditionalFormation) ctx;
        additionalFormation.mekborderless$resetMaintenancePorts();
    }

    @ModifyExpressionValue(method = "validateNode", at = @At(value = "INVOKE", target = "Lmekanism/common/lib/multiblock/CuboidStructureValidator;getCasingType(Lnet/minecraft/world/level/block/state/BlockState;)Lmekanism/common/lib/multiblock/FormationProtocol$CasingType;"))
    public FormationProtocol.CasingType getCasingType(FormationProtocol.CasingType type,
                                                      @Local(argsOnly = true) FormationProtocol<T> ctx,
                                                      @Local(argsOnly = true) BlockPos pos,
                                                      @Local BlockState state) {
        AttributeRepairing attributeRepairing = Attribute.get(state, AttributeRepairing.class);

        if(attributeRepairing != null) {
            IAdditionalFormation additionalFormation = (IAdditionalFormation) ctx;
            List<BlockPos> maintenancePorts = additionalFormation.mekborderless$getMaintenancePorts();
            maintenancePorts.add(pos.immutable());
        }

        AttributeCasing attributeCasing = Attribute.get(state, AttributeCasing.class);
        if(attributeCasing != null) {
            return attributeCasing.casingType();
        }
        return type;
    }
}
