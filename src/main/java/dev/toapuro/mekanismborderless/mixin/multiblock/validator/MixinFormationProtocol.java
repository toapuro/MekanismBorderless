package dev.toapuro.mekanismborderless.mixin.multiblock.validator;

import dev.toapuro.mekanismborderless.MBServerConfig;
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
    private static int explore(int original) {
        return MBServerConfig.getMaxStructureVolume().orElse(original);
    }
}
