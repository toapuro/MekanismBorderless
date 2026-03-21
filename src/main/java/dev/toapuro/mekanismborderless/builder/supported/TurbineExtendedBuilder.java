package dev.toapuro.mekanismborderless.builder.supported;

import dev.toapuro.mekanismborderless.builder.ExtendedMultiblockBuilder;
import mekanism.common.command.builders.StructureBuilder;
import mekanism.common.lib.math.voxel.VoxelCuboid;
import mekanism.common.lib.multiblock.CuboidStructureValidator;
import mekanism.common.lib.multiblock.FormationProtocol;
import mekanism.common.registries.MekanismBlocks;
import mekanism.generators.common.GeneratorsLang;
import mekanism.generators.common.registries.GeneratorsBlocks;
import mekanism.generators.common.tile.turbine.TileEntityTurbineRotor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.function.Supplier;

public class TurbineExtendedBuilder extends ExtendedMultiblockBuilder {
    public TurbineExtendedBuilder(StructureBuilder builder, Supplier<CuboidStructureValidator<?>> validatorSupplier) {
        super(builder, validatorSupplier);
    }

    @Override
    protected FormationProtocol.FormationResult precheck(Level level, BlockPos startPos, VoxelCuboid cuboid) {
        if(cuboid.length() % 2 == 0 || cuboid.width() % 2 == 0) {
            return FormationProtocol.FormationResult.fail(GeneratorsLang.TURBINE_INVALID_EVEN_LENGTH);
        }
        return FormationProtocol.FormationResult.SUCCESS;
    }

    @Override
    protected void buildAdditional(Level level, BlockPos startPos, VoxelCuboid cuboid) {
        this.buildColumn(level, startPos, new BlockPos(cuboid.length() / 2, 1, cuboid.width() / 2), cuboid.height() - 4,
                GeneratorsBlocks.TURBINE_ROTOR.getBlock(), TileEntityTurbineRotor.class, (rotor) -> rotor.blades = 2);
        this.buildInnerLayer(level, cuboid, cuboid.height() - 3, MekanismBlocks.PRESSURE_DISPERSER.getBlock());
        level.setBlockAndUpdate(startPos.offset(cuboid.length() / 2, cuboid.height() - 3, cuboid.width() / 2), GeneratorsBlocks.ROTATIONAL_COMPLEX.getBlock().defaultBlockState());
        this.buildInnerLayer(level, cuboid, cuboid.height() - 2, GeneratorsBlocks.SATURATING_CONDENSER.getBlock());
        this.buildPlane(level, startPos, 5, 5, cuboid.length()-4, cuboid.width()-4, cuboid.height() - 2, GeneratorsBlocks.ELECTROMAGNETIC_COIL.getBlock());
        this.buildPlane(level, startPos, 1, 1, cuboid.length(), cuboid.width(), 0, GeneratorsBlocks.TURBINE_CASING.getBlock());
    }
}
