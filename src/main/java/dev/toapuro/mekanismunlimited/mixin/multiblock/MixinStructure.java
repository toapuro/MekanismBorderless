package dev.toapuro.mekanismunlimited.mixin.multiblock;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.toapuro.mekanismunlimited.util.DiagonalFormationProtocol;
import mekanism.common.lib.multiblock.IMultiblockBase;
import mekanism.common.lib.multiblock.Structure;
import mekanism.generators.common.content.fusion.FusionReactorMultiblockData;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Predicate;

@Mixin(value = Structure.class, remap = false)
public class MixinStructure {

    @WrapOperation(method = "validate", at = @At(value = "INVOKE", target = "Lmekanism/common/lib/multiblock/FormationProtocol;explore(Lnet/minecraft/core/BlockPos;Ljava/util/function/Predicate;)I"))
    private static int explore(BlockPos start, Predicate<BlockPos> checker, Operation<Integer> original,
                               @Local(argsOnly = true) IMultiblockBase node) {
        if(node.getDefaultData() instanceof FusionReactorMultiblockData) {
            return DiagonalFormationProtocol.explore(start, checker);
        }
        return original.call(start, checker);
    }
}
