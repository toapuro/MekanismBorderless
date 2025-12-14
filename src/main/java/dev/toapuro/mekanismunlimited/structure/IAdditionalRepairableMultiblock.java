package dev.toapuro.mekanismunlimited.structure;

import java.util.function.DoubleSupplier;

public interface IAdditionalRepairableMultiblock {
    DoubleSupplier mekborderless$getAdditionalRepairRateSup();
    void mekborderless$setAdditionalRepairRateSup(DoubleSupplier repairRate);
}
