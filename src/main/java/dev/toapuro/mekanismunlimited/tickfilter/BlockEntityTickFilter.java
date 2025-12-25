package dev.toapuro.mekanismunlimited.tickfilter;

import dev.toapuro.mekanismunlimited.MBClientConfig;
import mekanism.common.lib.multiblock.IInternalMultiblock;
import mekanism.common.lib.multiblock.IStructuralMultiblock;
import mekanism.common.tile.prefab.TileEntityMultiblock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.util.Lazy;

import java.util.function.Supplier;

public class BlockEntityTickFilter {

    public static Supplier<Boolean> enabledPredicate = Lazy.of(() -> MBClientConfig.FILTER_TICK.get());

    public static boolean shouldSkipTick(BlockEntity blockEntity) {
        if(blockEntity instanceof TileEntityMultiblock<?>) {
            return false;
        }
        if(blockEntity instanceof IStructuralMultiblock structuralMultiblock) {
            if(structuralMultiblock.hasFormedMultiblock()) {
                return false;
            }
        }
        if(blockEntity instanceof IInternalMultiblock internalMultiblock) {
            return !internalMultiblock.hasFormedMultiblock();
        }

        return true;
    }

    public static boolean isEnabled() {
        return enabledPredicate.get();
    }
}
