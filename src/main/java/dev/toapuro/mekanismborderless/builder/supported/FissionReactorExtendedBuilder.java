package dev.toapuro.mekanismborderless.builder.supported;

import dev.toapuro.mekanismborderless.builder.ExtendedMultiblockBuilder;
import mekanism.common.command.builders.StructureBuilder;
import mekanism.common.lib.math.voxel.VoxelCuboid;
import mekanism.common.lib.multiblock.CuboidStructureValidator;
import mekanism.generators.common.registries.GeneratorsBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

import java.util.function.Supplier;

public class FissionReactorExtendedBuilder extends ExtendedMultiblockBuilder {
    public FissionReactorExtendedBuilder(StructureBuilder builder, Supplier<CuboidStructureValidator<?>> validatorSupplier) {
        super(builder, validatorSupplier);
    }

    @Override
    protected void buildAdditional(Level level, BlockPos startPos, VoxelCuboid cuboid) {
        for(int x = 1; x < cuboid.length() - 1; ++x) {
            for(int z = 1; z < cuboid.width() - 1; ++z) {
                if (x % 2 == z % 2) {
                    this.buildColumn(level, startPos, new BlockPos(x, 1, z), cuboid.height() - 3, GeneratorsBlocks.FISSION_FUEL_ASSEMBLY.getBlock());
                    level.setBlockAndUpdate(startPos.offset(x, cuboid.height() - 2, z), GeneratorsBlocks.CONTROL_ROD_ASSEMBLY.getBlock().defaultBlockState());
                } else {
                    this.buildColumn(level, startPos, new BlockPos(x, 1, z), cuboid.height() - 2, Blocks.AIR);
                }
            }
        }
    }
}
