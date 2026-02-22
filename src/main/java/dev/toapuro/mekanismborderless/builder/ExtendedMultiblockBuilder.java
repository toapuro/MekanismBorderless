package dev.toapuro.mekanismborderless.builder;

import dev.toapuro.mekanismborderless.mixin.accessor.CuboidStructureValidatorAccessor;
import dev.toapuro.mekanismborderless.mixin.accessor.StructureBuilderAccessor;
import mekanism.common.command.builders.StructureBuilder;
import mekanism.common.lib.math.voxel.VoxelCuboid;
import mekanism.common.lib.multiblock.CuboidStructureValidator;
import mekanism.common.lib.multiblock.FormationProtocol;
import mekanism.common.util.WorldUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.function.Consumer;
import java.util.function.Supplier;

@SuppressWarnings("SameParameterValue")
public class ExtendedMultiblockBuilder {

    private final Supplier<CuboidStructureValidator<?>> validatorSupplier;
    private final Block casingBlock;
    private final Block otherBlock;

    public ExtendedMultiblockBuilder(StructureBuilder builder, Supplier<CuboidStructureValidator<?>> validatorSupplier) {
        this(
                validatorSupplier,
                ((StructureBuilderAccessor) builder).invokeGetCasing(),
                ((StructureBuilderAccessor) builder).invokeGetWallBlock(BlockPos.ZERO)
        );
    }

    public ExtendedMultiblockBuilder(Supplier<CuboidStructureValidator<?>> validatorSupplier, Block casingBlock, Block otherBlock) {
        this.validatorSupplier = validatorSupplier;
        this.casingBlock = casingBlock;
        this.otherBlock = otherBlock;
    }
    protected final void buildInnerLayer(Level level, VoxelCuboid cuboid, int yLevel, Block block) {
        for(int x = 1; x < cuboid.length() - 1; ++x) {
            for(int z = 1; z < cuboid.width() - 1; ++z) {
                level.setBlockAndUpdate(cuboid.getMinPos().offset(x, yLevel, z), block.defaultBlockState());
            }
        }
    }

    protected final void buildColumn(Level world, BlockPos start, BlockPos pos, int height, Block block) {
        for(int y = 0; y < height; ++y) {
            world.setBlockAndUpdate(start.offset(pos).offset(0, y, 0), block.defaultBlockState());
        }
    }

    protected final <T extends BlockEntity> void buildColumn(Level world, BlockPos start, BlockPos pos, int height, Block block, Class<T> tileClass, Consumer<T> tileConsumer) {
        for(int y = 0; y < height; ++y) {
            BlockPos position = start.offset(pos).offset(0, y, 0);
            world.setBlockAndUpdate(position, block.defaultBlockState());
            T tile = WorldUtils.getTileEntity(tileClass, world, position);
            if (tile != null) {
                tileConsumer.accept(tile);
            }
        }
    }

    protected final void buildPlane(Level world, BlockPos start, int x1, int z1, int x2, int z2, int yLevel, Block block) {
        for(int x = x1; x < x2 - 1; ++x) {
            for(int z = z1; z < z2 - 1; ++z) {
                world.setBlockAndUpdate(start.offset(x, yLevel, z), block.defaultBlockState());
            }
        }
    }

    public boolean build(Level level, VoxelCuboid cuboid) {
        CuboidStructureValidator<?> validator = createValidator(cuboid);
        var access = (CuboidStructureValidatorAccessor) validator;

        BlockPos min = cuboid.getMinPos();
        BlockPos max = cuboid.getMaxPos();
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        if(!precheck(level, cuboid.getMinPos(), cuboid)) {
            return false;
        }

        for(int x = min.getX(); x <= max.getX(); ++x) {
            for(int y = min.getY(); y <= max.getY(); ++y) {
                for(int z = min.getZ(); z <= max.getZ(); ++z) {
                    pos.set(x, y, z);
                    FormationProtocol.StructureRequirement requirement = access.invokeGetStructureRequirement(pos);

                    this.buildBlock(level, pos, requirement);
                }
            }
        }

        this.buildAdditional(level, cuboid.getMinPos(), cuboid);
        return true;
    }

    public CuboidStructureValidator<?> createValidator(VoxelCuboid cuboid) {
        CuboidStructureValidator<?> validator = validatorSupplier.get();

        var access = (CuboidStructureValidatorAccessor) validator;
        access.setCuboid(cuboid);

        return validator;
    }

    protected void buildBlock(Level level, BlockPos pos, FormationProtocol.StructureRequirement requirement) {
        Block block;

        switch (requirement) {
            case IGNORED, INNER -> block = Blocks.AIR;
            case FRAME -> block = this.casingBlock;
            case OTHER -> block = this.otherBlock;
            default -> { return; }
        }

        level.setBlockAndUpdate(pos, block.defaultBlockState());
    }

    protected void buildAdditional(Level level, BlockPos startPos, VoxelCuboid cuboid) {

    }

    protected boolean precheck(Level level, BlockPos startPos, VoxelCuboid cuboid) {
        return true;
    }
}
