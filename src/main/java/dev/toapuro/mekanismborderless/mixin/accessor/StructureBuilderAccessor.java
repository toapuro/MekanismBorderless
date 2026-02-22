package dev.toapuro.mekanismborderless.mixin.accessor;

import mekanism.common.command.builders.StructureBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = StructureBuilder.class, remap = false)
public interface StructureBuilderAccessor {

    @Invoker("getWallBlock")
    Block invokeGetWallBlock(BlockPos pos);

    @Invoker("getCasing")
    Block invokeGetCasing();
}
