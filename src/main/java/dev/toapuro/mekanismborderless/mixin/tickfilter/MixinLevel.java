package dev.toapuro.mekanismborderless.mixin.tickfilter;

import dev.toapuro.mekanismborderless.tickfilter.BlockEntityTickUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TickingBlockEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(Level.class)
public class MixinLevel {
    @Shadow @Final public boolean isClientSide;

    @Inject(method = "addBlockEntityTicker", at = @At("HEAD"), cancellable = true)
    public void addBlockEntityTicker(TickingBlockEntity ticking, CallbackInfo ci) {
        if(isClientSide && BlockEntityTickUtils.isEnabled()) {
            Optional<BlockEntity> result = BlockEntityTickUtils.getBlockEntity(ticking);
            if(result.isEmpty()) return;
            BlockEntity blockEntity = result.get();

            if (BlockEntityTickUtils.shouldSkipTick(blockEntity)) {
                ci.cancel();
            }
        }
    }
}
