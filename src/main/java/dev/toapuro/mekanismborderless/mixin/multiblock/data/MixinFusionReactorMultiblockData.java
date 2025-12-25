package dev.toapuro.mekanismborderless.mixin.multiblock.data;

import com.google.common.primitives.Ints;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mekanism.api.heat.IHeatCapacitor;
import mekanism.api.math.FloatingLongSupplier;
import mekanism.common.capabilities.heat.VariableHeatCapacitor;
import mekanism.common.lib.math.voxel.VoxelCuboid;
import mekanism.generators.common.content.fusion.FusionReactorMultiblockData;
import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.IntSupplier;
import java.util.function.LongSupplier;

@Mixin(value = FusionReactorMultiblockData.class, remap = false)
public abstract class MixinFusionReactorMultiblockData extends MixinMultiblockData {

    @Shadow public IHeatCapacitor heatCapacitor;

    @ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Lmekanism/common/capabilities/chemical/multiblock/MultiblockChemicalTankBuilder;input(Lmekanism/common/lib/multiblock/MultiblockData;Ljava/util/function/LongSupplier;Ljava/util/function/Predicate;Lmekanism/api/IContentsListener;)Lmekanism/api/chemical/IChemicalTank;"), index = 1)
    public LongSupplier modifyInputTank(LongSupplier capacity) {
        return () -> capacity.getAsLong() * (getVolume() / 125);
    }

    @ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Lmekanism/common/capabilities/chemical/multiblock/MultiblockChemicalTankBuilder;output(Lmekanism/common/lib/multiblock/MultiblockData;Ljava/util/function/LongSupplier;Ljava/util/function/Predicate;Lmekanism/api/IContentsListener;)Lmekanism/api/chemical/IChemicalTank;"), index = 1)
    public LongSupplier modifyOutputTank(LongSupplier capacity) {
        return () -> capacity.getAsLong() * (getVolume() / 125);
    }

    @ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Lmekanism/common/capabilities/energy/VariableCapacityEnergyContainer;output(Lmekanism/api/math/FloatingLongSupplier;Lmekanism/api/IContentsListener;)Lmekanism/common/capabilities/energy/VariableCapacityEnergyContainer;"), index = 0)
    public FloatingLongSupplier modifyEnergyTank(FloatingLongSupplier maxEnergy) {
        return () -> maxEnergy.get().multiply(getVolume() / 125);
    }

    @ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Lmekanism/common/capabilities/fluid/VariableCapacityFluidTank;input(Lmekanism/common/lib/multiblock/MultiblockData;Ljava/util/function/IntSupplier;Ljava/util/function/Predicate;Lmekanism/api/IContentsListener;)Lmekanism/common/capabilities/fluid/VariableCapacityFluidTank;"), index = 1)
    public IntSupplier modifyFluidInputTank(IntSupplier capacity) {
        return () -> Ints.saturatedCast((long) capacity.getAsInt() * (getVolume() / 125L));
    }

    @Override
    public void setVolume(int volume, CallbackInfo ci) {
        super.setVolume(volume, ci);
        if(heatCapacitor instanceof VariableHeatCapacitor variableHeatCapacitor) {
            double heatCapacity = variableHeatCapacitor.getHeatCapacity();
            variableHeatCapacitor.setHeatCapacity(heatCapacity * (1 + (double) volume / 123), false);
        }
    }

    @ModifyConstant(method = "computerSetInjectionRate", constant = @Constant(intValue = 98))
    public int modifyMaxInjectionRate(int constant) {
        int rate = (getVolume() / 125) * 99;
        if(rate % 2 == 1) {
            rate -= 1;
        }
        return rate;
    }

    @WrapOperation(method = "kill", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z", remap = true))
    public boolean shouldKill(Entity entity, DamageSource damageSource, float amount, Operation<Boolean> original) {
        VoxelCuboid bounds = this.getBounds();
        BlockPos minPos = bounds.getMinPos();
        BlockPos sizePos = bounds.getMaxPos().subtract(bounds.getMinPos());
        BlockPos centerPos = minPos.offset(sizePos.getX() / 2, sizePos.getY() / 2, sizePos.getZ() / 2);

        int size = bounds.getMaxPos().getX() - bounds.getMinPos().getX();
        int radius = size/2;

        BlockPos relativePos = entity.blockPosition().subtract(centerPos);

        // manhattan distance
        int dst = Math.abs(relativePos.getX())
                + Math.abs(relativePos.getY())
                + Math.abs(relativePos.getZ());
        if(dst < radius * 2 - 1) {
            return original.call(entity, damageSource, amount);
        }
        return false;
    }
}
