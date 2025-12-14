package dev.toapuro.mekanismunlimited.registries;

import dev.toapuro.mekanismunlimited.block.attribute.AttributeCasing;
import dev.toapuro.mekanismunlimited.block.attribute.AttributeRepairing;
import dev.toapuro.mekanismunlimited.block.tile.TileEntityReactorMaintenancePort;
import mekanism.common.MekanismLang;
import mekanism.common.block.attribute.AttributeMultiblock;
import mekanism.common.block.attribute.AttributeStateFacing;
import mekanism.common.config.MekanismConfig;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.lib.multiblock.FormationProtocol;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class MBBlockTypes {

    public static final BlockTypeTile<TileEntityReactorMaintenancePort> MAINTENANCE_PORT;

    static {
        MAINTENANCE_PORT = BlockTypeTile.BlockTileBuilder
                .createBlock(() -> MBTIleEntityTypes.MAINTENANCE_HATCH, MekanismLang.DESCRIPTION_STRUCTURAL_GLASS)
                .with(
                        AttributeMultiblock.STRUCTURAL,
                        new AttributeStateFacing(BlockStateProperties.HORIZONTAL_FACING, AttributeStateFacing.FacePlacementType.PLAYER_LOCATION),
                        new AttributeCasing(FormationProtocol.CasingType.VALVE),
                        AttributeRepairing.REPAIRABLE)
                .withEnergyConfig(MekanismConfig.usage.precisionSawmill, MekanismConfig.storage.precisionSawmill) // TODO: 独自のものに
                .build();
    }
}
