package dev.toapuro.mekanismunlimited.mixin.multiblock;

import com.google.common.primitives.Ints;
import mekanism.common.lib.multiblock.MultiblockData;
import mekanism.generators.common.config.MekanismGeneratorsConfig;
import mekanism.generators.common.content.fission.FissionReactorMultiblockData;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.LongSupplier;

@Mixin(value = FissionReactorMultiblockData.class, remap = false)
@Debug(export = true)
public abstract class MixinFissionReactorMultiblockData extends MultiblockData {

    @Shadow private int cooledCoolantCapacity;

    @Unique private long mekborderless$cooledCoolantCapacityLong;

    @ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Lmekanism/common/capabilities/chemical/multiblock/MultiblockChemicalTankBuilder;input(Lmekanism/common/lib/multiblock/MultiblockData;Ljava/util/function/LongSupplier;Ljava/util/function/Predicate;Lmekanism/api/IContentsListener;)Lmekanism/api/chemical/IChemicalTank;"), index = 1)
    private LongSupplier init(LongSupplier capacity) {
        return () -> this.mekborderless$cooledCoolantCapacityLong;
    }

    @Inject(method = "setVolume", at = @At("TAIL"))
    public void modifyCooledCoolantCapacity(int volume, CallbackInfo ci) {
        this.mekborderless$cooledCoolantCapacityLong =
                (long) volume * (long) MekanismGeneratorsConfig.generators.fissionCooledCoolantPerTank.get();
        this.cooledCoolantCapacity = Ints.saturatedCast(mekborderless$cooledCoolantCapacityLong);
    }

    public MixinFissionReactorMultiblockData(BlockEntity tile) {
        super(tile);
    }
}
