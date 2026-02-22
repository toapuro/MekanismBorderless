package dev.toapuro.mekanismborderless.builder.supported;

import dev.toapuro.mekanismborderless.builder.ExtendedMultiblockBuilder;
import mekanism.common.command.builders.StructureBuilder;
import mekanism.common.lib.math.voxel.VoxelCuboid;
import mekanism.common.lib.multiblock.CuboidStructureValidator;
import mekanism.common.registries.MekanismBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.function.Supplier;

public class BoilerExtendedBuilder extends ExtendedMultiblockBuilder {
    public BoilerExtendedBuilder(StructureBuilder builder, Supplier<CuboidStructureValidator<?>> validatorSupplier) {
        super(builder, validatorSupplier);
    }

    @Override
    protected void buildAdditional(Level level, BlockPos startPos, VoxelCuboid cuboid) {
        this.buildInnerLayer(level, cuboid, 1, MekanismBlocks.SUPERHEATING_ELEMENT.getBlock());
        this.buildInnerLayer(level, cuboid, cuboid.height() - 3, MekanismBlocks.PRESSURE_DISPERSER.getBlock());
    }
}
