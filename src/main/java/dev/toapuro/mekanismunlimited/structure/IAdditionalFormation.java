package dev.toapuro.mekanismunlimited.structure;

import net.minecraft.core.BlockPos;

import java.util.List;

public interface IAdditionalFormation {
    List<BlockPos> mekborderless$getMaintenancePorts();
    void mekborderless$resetMaintenancePorts();
}
