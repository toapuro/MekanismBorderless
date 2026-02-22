package dev.toapuro.mekanismborderless.builder.supported;

import dev.toapuro.mekanismborderless.builder.ExtendedMultiblockBuilder;
import mekanism.common.command.builders.StructureBuilder;
import mekanism.common.lib.math.voxel.VoxelCuboid;
import mekanism.common.lib.multiblock.CuboidStructureValidator;
import mekanism.common.registries.MekanismBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.function.Supplier;

public class SPSExtendedBuilder extends ExtendedMultiblockBuilder {
    public SPSExtendedBuilder(StructureBuilder builder, Supplier<CuboidStructureValidator<?>> validatorSupplier) {
        super(builder, validatorSupplier);
    }

    protected void buildPort(Level level, VoxelCuboid cuboid, Direction face) {
        BlockPos center = cuboid.getCenter();
        Vec3i normal = face.getNormal();
        Vec3i halfDelta = new Vec3i(cuboid.length() / 2, cuboid.height() / 2, cuboid.width() / 2);

        BlockPos pos = center.offset(normal.getX() * halfDelta.getX(), normal.getY() * halfDelta.getY(), normal.getZ() * halfDelta.getZ());
        level.setBlockAndUpdate(pos, MekanismBlocks.SPS_PORT.getBlock().defaultBlockState());
        BlockState state = MekanismBlocks.SUPERCHARGED_COIL.getBlock().defaultBlockState();
        state.setValue(BlockStateProperties.FACING, face.getOpposite());
        level.setBlockAndUpdate(pos.relative(face.getOpposite()), state);
    }

    @Override
    protected void buildAdditional(Level level, BlockPos startPos, VoxelCuboid cuboid) {
        for (Direction direction : Direction.values()) {
            buildPort(level, cuboid, direction);
        }
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
