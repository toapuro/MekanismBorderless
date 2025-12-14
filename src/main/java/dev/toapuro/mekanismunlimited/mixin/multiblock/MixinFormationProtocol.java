package dev.toapuro.mekanismunlimited.mixin.multiblock;

import com.google.common.primitives.Ints;
import dev.toapuro.mekanismunlimited.MBConfig;
import mekanism.common.lib.multiblock.FormationProtocol;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(value = FormationProtocol.class, remap = false)
public class MixinFormationProtocol {

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
}
