package dev.toapuro.mekanismborderless.mixin.multiblock;

import mekanism.api.providers.IBlockProvider;
import mekanism.common.tile.prefab.TileEntityMultiblock;
import mekanism.generators.common.content.fusion.FusionReactorMultiblockData;
import mekanism.generators.common.tile.fusion.TileEntityFusionReactorBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(value = TileEntityFusionReactorBlock.class, remap = false)
public abstract class MixinTileEntityFusionReactorBlock extends TileEntityMultiblock<FusionReactorMultiblockData> {

    public MixinTileEntityFusionReactorBlock(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
    }

    @ModifyConstant(method = "setInjectionRateFromPacket", constant = @Constant(intValue = 98))
    public int modifyMaxInjectionRate(int constant) {
        FusionReactorMultiblockData multiblock = getMultiblock();

        int rate = (multiblock.getVolume() / 125) * 99;
        if(rate % 2 == 1) {
            rate -= 1;
        }
        return rate;
    }
}
