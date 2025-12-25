package dev.toapuro.mekanismborderless.mixin.multiblock;

import mekanism.api.providers.IBlockProvider;
import mekanism.generators.common.tile.fusion.TileEntityFusionReactorController;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = TileEntityFusionReactorController.class, remap = false)
public abstract class MixinTileEntityFusionReactorController extends MixinTileEntityMultiblock {

    public MixinTileEntityFusionReactorController(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
    }
}
