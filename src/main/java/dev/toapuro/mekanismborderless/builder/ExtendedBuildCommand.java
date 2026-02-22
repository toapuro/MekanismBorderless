package dev.toapuro.mekanismborderless.builder;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import mekanism.api.text.EnumColor;
import mekanism.api.text.ILangEntry;
import mekanism.common.MekanismLang;
import mekanism.common.base.MekanismPermissions;
import mekanism.common.lib.math.voxel.VoxelCuboid;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class ExtendedBuildCommand {
    public static final ArgumentBuilder<CommandSourceStack, ?> COMMAND =
            Commands.literal("build")
                    .requires(MekanismPermissions.COMMAND_BUILD.and(cs -> cs.getEntity() instanceof ServerPlayer));

    public static void register(String name, ILangEntry localizedName, ExtendedMultiblockBuilder builder) {
        COMMAND.then(registerSub(Commands.literal(name), localizedName, builder)
        );
    }

    private static ArgumentBuilder<CommandSourceStack, ?> registerSub(ArgumentBuilder<CommandSourceStack, ?> argumentBuilder, ILangEntry localizedName,
                                                                      ExtendedMultiblockBuilder builder) {
        return argumentBuilder
                .then(Commands.argument("start", BlockPosArgument.blockPos())
                        .then(Commands.argument("end", BlockPosArgument.blockPos())
                                .executes(ctx ->
                                        build(ctx, localizedName, builder,
                                                BlockPosArgument.getLoadedBlockPos(ctx, "start"), BlockPosArgument.getLoadedBlockPos(ctx, "end"))
                                )
                        )
                );
    }

    private static int build(CommandContext<CommandSourceStack> ctx, ILangEntry localizedName, ExtendedMultiblockBuilder builder, BlockPos start, BlockPos end) {
        CommandSourceStack source = ctx.getSource();
        BlockPos minPos = new BlockPos(Math.min(start.getX(), end.getX()), Math.min(start.getY(), end.getY()), Math.min(start.getZ(), end.getZ()));
        BlockPos maxPos = new BlockPos(Math.max(start.getX(), end.getX()), Math.max(start.getY(), end.getY()), Math.max(start.getZ(), end.getZ()));

        VoxelCuboid cuboid = new VoxelCuboid(minPos, maxPos);
        boolean success = builder.build(source.getLevel(), cuboid);

        if(success) {
            source.sendSuccess(() -> {
                ILangEntry builtEntry = MekanismLang.COMMAND_BUILD_BUILT;
                return builtEntry.translateColored(EnumColor.GRAY, EnumColor.INDIGO, localizedName);
            }, true);
        } else {
            source.sendFailure(Component.literal("Invalid multiblock size"));
        }
        return 0;
    }
}
