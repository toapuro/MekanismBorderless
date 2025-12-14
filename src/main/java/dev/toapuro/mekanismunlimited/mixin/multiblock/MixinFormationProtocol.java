package dev.toapuro.mekanismunlimited.mixin.multiblock;

import com.google.common.primitives.Ints;
import com.llamalad7.mixinextras.sugar.Local;
import dev.toapuro.mekanismunlimited.MBConfig;
import dev.toapuro.mekanismunlimited.block.tile.ITileRepairBooster;
import dev.toapuro.mekanismunlimited.structure.IAdditionalFormation;
import dev.toapuro.mekanismunlimited.structure.IAdditionalRepairableMultiblock;
import mekanism.common.lib.multiblock.FormationProtocol;
import mekanism.common.lib.multiblock.IMultiblock;
import mekanism.common.lib.multiblock.IStructureValidator;
import mekanism.common.lib.multiblock.MultiblockData;
import mekanism.common.util.WorldUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(value = FormationProtocol.class, remap = false)
public class MixinFormationProtocol<T extends MultiblockData> implements IAdditionalFormation {

    @Shadow @Final private IMultiblock<T> pointer;
    @Unique
    private final List<BlockPos> mekborderless$maintenancePorts = new ArrayList<>();

    @ModifyArg(
            method = "explore(Lnet/minecraft/core/BlockPos;Ljava/util/function/Predicate;)I",
            at = @At(value = "INVOKE", target = "Lmekanism/common/lib/multiblock/FormationProtocol;explore(Lnet/minecraft/core/BlockPos;Ljava/util/function/Predicate;I)I"),
            index = 2
    )
    private static int explore(int maxCount) {
        int maxXSize = MBConfig.getMaxXSize();
        int maxYSize = MBConfig.getMaxYSize();
        int maxZSize = MBConfig.getMaxZSize();
        if(maxXSize == Integer.MAX_VALUE || maxYSize == Integer.MAX_VALUE || maxZSize == Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }
        long maxLong = (long) Ints.saturatedCast((long) maxXSize * (long) maxYSize) * (long) maxZSize;
        return Ints.saturatedCast(maxLong);
    }

    @Inject(method = "buildStructure", at = @At(value = "INVOKE", target = "Lmekanism/common/lib/multiblock/IStructureValidator;postcheck(Lmekanism/common/lib/multiblock/MultiblockData;Lit/unimi/dsi/fastutil/longs/Long2ObjectMap;)Lmekanism/common/lib/multiblock/FormationProtocol$FormationResult;"))
    public void buildStructure(IStructureValidator<T> validator, CallbackInfoReturnable<?> cir,
                               @Local T structure) {
        if(!(structure instanceof IAdditionalRepairableMultiblock repairableMultiblock)) {
            return;
        }
        repairableMultiblock.mekborderless$setAdditionalRepairRateSup(this::mekborderless$getAdditionalRepairRate);
    }

    @Unique
    public double mekborderless$getAdditionalRepairRate() {
        double repairRate = 0;
        for (BlockPos pos : mekborderless$maintenancePorts) {
            Level tileWorld = pointer.getTileWorld();
            BlockEntity tileEntity = WorldUtils.getTileEntity(tileWorld, pos);
            if(tileEntity instanceof ITileRepairBooster booster) {
                repairRate += booster.mekborderlss$getAdditionalRepairRate();
            }
        }
        return repairRate;
    }

    @Override
    public List<BlockPos> mekborderless$getMaintenancePorts() {
        return mekborderless$maintenancePorts;
    }

    @Override
    public void mekborderless$resetMaintenancePorts() {
        mekborderless$maintenancePorts.clear();
    }
}
