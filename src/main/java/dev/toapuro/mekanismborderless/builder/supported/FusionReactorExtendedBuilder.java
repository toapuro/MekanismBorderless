package dev.toapuro.mekanismborderless.builder.supported;

import dev.toapuro.mekanismborderless.builder.ExtendedMultiblockBuilder;
import mekanism.common.command.builders.StructureBuilder;
import mekanism.common.lib.math.voxel.VoxelCuboid;
import mekanism.common.lib.multiblock.CuboidStructureValidator;
import mekanism.common.lib.multiblock.FormationProtocol.FormationResult;
import mekanism.generators.common.registries.GeneratorsBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

import java.util.function.Supplier;

public class FusionReactorExtendedBuilder extends ExtendedMultiblockBuilder {
    public FusionReactorExtendedBuilder(StructureBuilder builder, Supplier<CuboidStructureValidator<?>> validatorSupplier) {
        super(builder, validatorSupplier);
    }

    @Override
    protected void buildAdditional(Level level, BlockPos startPos, VoxelCuboid cuboid) {
        level.setBlockAndUpdate(startPos.offset(cuboid.length()/2, cuboid.height() - 1, cuboid.width()/2), GeneratorsBlocks.FUSION_REACTOR_CONTROLLER.getBlock().defaultBlockState());
    }

    @Override
    protected FormationResult precheck(Level level, BlockPos startPos, VoxelCuboid cuboid) {
        int xSize = cuboid.length();
        int ySize = cuboid.height();
        int zSize = cuboid.width();
        if (xSize != ySize || ySize != zSize) {
            return FormationResult.fail(
                    Component.literal("Couldn't form, width and length of structure must be equal.")
            );
        }
        if(xSize % 2 != 0) {
            return FormationResult.fail(
                    Component.literal("Couldn't form, width and length of structure must be odd.")
            );
        }
        return FormationResult.SUCCESS;
    }
}
