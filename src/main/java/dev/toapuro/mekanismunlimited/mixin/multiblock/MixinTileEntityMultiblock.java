package dev.toapuro.mekanismunlimited.mixin.multiblock;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import dev.toapuro.mekanismunlimited.util.timer.TileTickTracker;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.lib.multiblock.IMultiblockBase;
import mekanism.common.lib.multiblock.Structure;
import mekanism.common.tile.base.TileEntityMekanism;
import mekanism.common.tile.prefab.TileEntityMultiblock;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = TileEntityMultiblock.class, remap = false)
public class MixinTileEntityMultiblock extends TileEntityMekanism {

    @Unique
    private final TileTickTracker mekborderless$TileTickTracker = new TileTickTracker();

    public MixinTileEntityMultiblock(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
    }

    @WrapWithCondition(method = "onUpdateServer()V", at = @At(value = "INVOKE", target = "Lmekanism/common/lib/multiblock/Structure;tick(Lnet/minecraft/world/level/block/entity/BlockEntity;Z)V"))
    public <TILE extends BlockEntity & IMultiblockBase> boolean preStructureTick(Structure instance, TILE tile, boolean tryValidate) {
        return mekborderless$TileTickTracker.claimTick();
    }

    @ModifyReturnValue(method = "getReducedUpdateTag", at = @At("RETURN"))
    public CompoundTag modifyReducedUpdateTag(CompoundTag original) {
        return original;
    }

}
