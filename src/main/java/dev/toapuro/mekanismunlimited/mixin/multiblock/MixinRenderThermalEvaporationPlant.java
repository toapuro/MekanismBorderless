package dev.toapuro.mekanismunlimited.mixin.multiblock;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import mekanism.client.render.data.RenderData;
import mekanism.client.render.tileentity.RenderThermalEvaporationPlant;
import mekanism.common.content.evaporation.EvaporationMultiblockData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = RenderThermalEvaporationPlant.class, remap = false)
public class MixinRenderThermalEvaporationPlant {

    @WrapOperation(method = "render(Lmekanism/common/tile/multiblock/TileEntityThermalEvaporationController;Lmekanism/common/content/evaporation/EvaporationMultiblockData;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IILnet/minecraft/util/profiling/ProfilerFiller;)V",
            at = @At(value = "INVOKE", target = "Lmekanism/client/render/data/RenderData$Builder;dimensions(III)Lmekanism/client/render/data/RenderData$Builder;"))
    public <DATA_TYPE extends RenderData> RenderData.Builder<DATA_TYPE> render(RenderData.Builder<?> instance, int width, int height, int length, Operation<RenderData.Builder<DATA_TYPE>> original,
                                                                               @Local(argsOnly = true) EvaporationMultiblockData multiblock) {
        return original.call(instance, multiblock.width() - 2, height, multiblock.length() - 2);
    }
}
