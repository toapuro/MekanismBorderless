package dev.toapuro.mekanismborderless.builder.supported;

import dev.toapuro.mekanismborderless.builder.ExtendedMultiblockBuilder;
import mekanism.common.command.builders.StructureBuilder;
import mekanism.common.lib.math.voxel.VoxelCuboid;
import mekanism.common.lib.multiblock.CuboidStructureValidator;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.function.Supplier;

public class SPSExtendedBuilder extends ExtendedMultiblockBuilder {
    public SPSExtendedBuilder(StructureBuilder builder, Supplier<CuboidStructureValidator<?>> validatorSupplier) {
        super(builder, validatorSupplier);
    }

    @Override
    protected boolean precheck(Level level, BlockPos startPos, VoxelCuboid cuboid) {
        int xSize = cuboid.length();
        int ySize = cuboid.height();
        int zSize = cuboid.width();
        if (xSize != ySize || ySize != zSize) {
            return false;
        }
        return xSize % 2 != 0;
    }
}
