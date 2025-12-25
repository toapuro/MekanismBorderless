package dev.toapuro.mekanismunlimited.mixin.multiblock;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.toapuro.mekanismunlimited.MBClientConfig;
import mekanism.common.lib.math.voxel.VoxelCuboid;
import mekanism.generators.client.render.RenderFusionReactor;
import mekanism.generators.common.content.fusion.FusionReactorMultiblockData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(value = RenderFusionReactor.class, remap = false)
public class MixinRenderFusionReactor {

    @ModifyArg(method = "render(Lmekanism/generators/common/tile/fusion/TileEntityFusionReactorController;Lmekanism/generators/common/content/fusion/FusionReactorMultiblockData;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IILnet/minecraft/util/profiling/ProfilerFiller;)V",
            at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(DDD)V", remap = true), index = 1)
    public double translateY(double original,
                             @Local(argsOnly = true) PoseStack matrix,
                             @Local(argsOnly = true) FusionReactorMultiblockData multiblock) {
        VoxelCuboid bounds = multiblock.getBounds();

        int sizeY = bounds.height();
        double centerY = sizeY / 2d;
        return -centerY + 1d;
    }

    @ModifyVariable(method = "render(Lmekanism/generators/common/tile/fusion/TileEntityFusionReactorController;Lmekanism/generators/common/content/fusion/FusionReactorMultiblockData;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IILnet/minecraft/util/profiling/ProfilerFiller;)V",
            at = @At("STORE"), name = "scale")
    public float modifyScale(float original,
                             @Local(argsOnly = true) FusionReactorMultiblockData multiblock) {
        return original * (multiblock.getBounds().length() / 5f) * MBClientConfig.FUSION_CORE_SIZE_MULTIPLIER.get();
    }
}
