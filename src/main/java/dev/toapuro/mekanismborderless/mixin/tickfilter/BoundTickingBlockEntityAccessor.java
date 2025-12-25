package dev.toapuro.mekanismborderless.mixin.tickfilter;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LevelChunk.BoundTickingBlockEntity.class)
public interface BoundTickingBlockEntityAccessor {
    @Accessor("blockEntity")
    BlockEntity getBlockEntity();
}
