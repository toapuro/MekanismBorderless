package dev.toapuro.mekanismborderless.builder;

import com.google.common.base.Preconditions;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import mekanism.api.text.EnumColor;
import mekanism.api.text.ILangEntry;
import mekanism.common.MekanismLang;
import mekanism.common.base.MekanismPermissions;
import mekanism.common.lib.math.voxel.VoxelCuboid;
import mekanism.common.lib.multiblock.FormationProtocol;
import net.minecraft.commands.CommandRuntimeException;
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
                        .then(Commands.argument("length", IntegerArgumentType.integer())
                                .then(Commands.argument("height", IntegerArgumentType.integer())
                                        .then(Commands.argument("width", IntegerArgumentType.integer())
                                                .executes(ctx ->
                                                        build(ctx, localizedName, builder,
                                                                BlockPosArgument.getLoadedBlockPos(ctx, "start"),
                                                                IntegerArgumentType.getInteger(ctx, "length"),
                                                                IntegerArgumentType.getInteger(ctx, "height"),
                                                                IntegerArgumentType.getInteger(ctx, "width"))
                                                )
                                        )
                                )
                        )
                );
    }

    private static int build(CommandContext<CommandSourceStack> ctx, ILangEntry localizedName, ExtendedMultiblockBuilder builder, BlockPos start, int length, int height, int width) {
        Preconditions.checkArgument(length > 0, new CommandRuntimeException(Component.literal("The length must be positive")));
        Preconditions.checkArgument(height > 0, new CommandRuntimeException(Component.literal("The height must be positive")));
        Preconditions.checkArgument(width > 0, new CommandRuntimeException(Component.literal("The width must be positive")));

        CommandSourceStack source = ctx.getSource();
        BlockPos maxPos = start.offset(length-1, height-1, width-1);

        VoxelCuboid cuboid = new VoxelCuboid(start, maxPos);
        FormationProtocol.FormationResult result = builder.build(source.getLevel(), cuboid);

        if(result.isFormed()) {
            source.sendSuccess(() -> {
                ILangEntry builtEntry = MekanismLang.COMMAND_BUILD_BUILT;
                return builtEntry.translateColored(EnumColor.GRAY, EnumColor.INDIGO, localizedName);
            }, true);
        } else {
            source.sendFailure(result.getResultText());
        }
        return 0;
    }
}
