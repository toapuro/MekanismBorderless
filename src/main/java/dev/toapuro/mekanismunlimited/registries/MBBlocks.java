package dev.toapuro.mekanismunlimited.registries;

import dev.toapuro.mekanismunlimited.MekanismBorderless;
import dev.toapuro.mekanismunlimited.block.NormalBlockTile;
import dev.toapuro.mekanismunlimited.block.tile.TileEntityReactorMaintenancePort;
import mekanism.common.item.block.ItemBlockTooltip;
import mekanism.common.registration.impl.BlockDeferredRegister;
import mekanism.common.registration.impl.BlockRegistryObject;
import net.minecraft.world.level.material.MapColor;

public class MBBlocks {
    public static final BlockDeferredRegister BLOCKS = new BlockDeferredRegister(MekanismBorderless.MODID);
    public static final BlockRegistryObject<NormalBlockTile<TileEntityReactorMaintenancePort>,
            ItemBlockTooltip<NormalBlockTile<TileEntityReactorMaintenancePort>>> MAINTENANCE_PORT;

    static {
        MAINTENANCE_PORT = BLOCKS.registerDefaultProperties("maintenance_port",
                () -> new NormalBlockTile<>(MBBlockTypes.MAINTENANCE_PORT, p -> p.mapColor(MapColor.COLOR_GRAY)), ItemBlockTooltip::new);
    }
}
