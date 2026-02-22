package dev.toapuro.mekanismborderless.tickfilter;

import dev.toapuro.mekanismborderless.MBClientConfig;
import dev.toapuro.mekanismborderless.mixin.tickfilter.BoundTickingBlockEntityAccessor;
import dev.toapuro.mekanismborderless.mixin.tickfilter.RebindableTickingBlockEntityWrapperAccessor;
import mekanism.common.lib.multiblock.IInternalMultiblock;
import mekanism.common.lib.multiblock.IStructuralMultiblock;
import mekanism.common.tile.prefab.TileEntityMultiblock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TickingBlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.common.util.Lazy;

import java.util.Optional;
import java.util.function.Supplier;

public class BlockEntityTickUtils {

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

    public static Optional<BlockEntity> getBlockEntity(TickingBlockEntity tickingBlock) {
        if(tickingBlock instanceof LevelChunk.RebindableTickingBlockEntityWrapper wrapper) {
            return getBlockEntity(((RebindableTickingBlockEntityWrapperAccessor) wrapper).getTicker());
        }
        if(tickingBlock instanceof LevelChunk.BoundTickingBlockEntity<?> boundTIcking) {
            BlockEntity blockEntity = ((BoundTickingBlockEntityAccessor) boundTIcking).getBlockEntity();
            return Optional.of(blockEntity);
        }

        return Optional.empty();
    }

    public static boolean isEnabled() {
        return enabledPredicate.get();
    }
}
