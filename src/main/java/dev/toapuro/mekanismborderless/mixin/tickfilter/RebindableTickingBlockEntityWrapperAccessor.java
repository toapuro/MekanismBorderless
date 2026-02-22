package dev.toapuro.mekanismborderless.mixin.tickfilter;

import net.minecraft.world.level.block.entity.TickingBlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LevelChunk.RebindableTickingBlockEntityWrapper.class)
public interface RebindableTickingBlockEntityWrapperAccessor {

    @Accessor("ticker")
    TickingBlockEntity getTicker();
}
