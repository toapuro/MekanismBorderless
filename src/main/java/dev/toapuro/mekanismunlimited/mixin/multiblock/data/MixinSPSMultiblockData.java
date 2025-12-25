package dev.toapuro.mekanismunlimited.mixin.multiblock.data;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import mekanism.common.config.MekanismConfig;
import mekanism.common.content.sps.SPSMultiblockData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.LongSupplier;

@Mixin(value = SPSMultiblockData.class, remap = false)
public abstract class MixinSPSMultiblockData extends MixinMultiblockData {

    @Unique
    private long mekborderless$additionalInputCapacity;
    @Unique
    private long mekborderless$additionalOutputCapacity;

    @ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Lmekanism/common/capabilities/chemical/multiblock/MultiblockChemicalTankBuilder;output(Lmekanism/common/lib/multiblock/MultiblockData;Ljava/util/function/LongSupplier;Ljava/util/function/Predicate;Lmekanism/api/chemical/attribute/ChemicalAttributeValidator;Lmekanism/api/IContentsListener;)Lmekanism/api/chemical/IChemicalTank;"), index = 1)
    public LongSupplier createOutputTank(LongSupplier capacity) {
        return () -> capacity.getAsLong() + mekborderless$additionalOutputCapacity;
    }

    @ModifyReturnValue(method = "getMaxInputGas", at = @At("RETURN"))
    public long getMaxInputGas(long original) {
        return original + mekborderless$additionalInputCapacity;
    }

    @Override
    public void setVolume(int volume, CallbackInfo ci) {
        mekborderless$additionalInputCapacity = (volume - 343) / 343 * MekanismConfig.general.spsInputPerAntimatter.get() * 2L;
        mekborderless$additionalOutputCapacity = (volume - 343) / 343 * MekanismConfig.general.spsOutputTankCapacity.get();
    }
}
