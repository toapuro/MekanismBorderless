package dev.toapuro.mekanismborderless.mixin.tickfilter;

import dev.toapuro.mekanismborderless.MBClientConfig;
import dev.toapuro.mekanismborderless.tickfilter.BlockEntityTickFilter;
import mekanism.common.tile.base.TileEntityMekanism;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelChunk.BoundTickingBlockEntity.class)
public class MixinBoundTickingBlockEntity<T extends BlockEntity> {

    @Shadow @Final private T blockEntity;

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void tick(CallbackInfo ci) {
        if (!(blockEntity instanceof TileEntityMekanism)) {
            return;
        }

        Level level = blockEntity.getLevel();
        if(level == null || !level.isClientSide) {
            return;
        }
        if(!MBClientConfig.FILTER_TICK.get()) {
            return;
        }

        if (BlockEntityTickFilter.isEnabled() && BlockEntityTickFilter.shouldSkipTick(blockEntity)) {
            ci.cancel();
        }
    }
}
