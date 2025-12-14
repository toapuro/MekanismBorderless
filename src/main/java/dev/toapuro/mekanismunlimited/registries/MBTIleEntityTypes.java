package dev.toapuro.mekanismunlimited.registries;

import dev.toapuro.mekanismunlimited.MekanismBorderless;
import dev.toapuro.mekanismunlimited.block.tile.TileEntityReactorMaintenancePort;
import mekanism.common.registration.impl.TileEntityTypeDeferredRegister;
import mekanism.common.registration.impl.TileEntityTypeRegistryObject;
import mekanism.common.tile.base.TileEntityMekanism;

public class MBTIleEntityTypes {
    public static final TileEntityTypeDeferredRegister TILE_ENTITY_TYPES = new TileEntityTypeDeferredRegister(MekanismBorderless.MODID);
    public static final TileEntityTypeRegistryObject<TileEntityReactorMaintenancePort> MAINTENANCE_HATCH;

    static {
        MAINTENANCE_HATCH = TILE_ENTITY_TYPES.register(MBBlocks.MAINTENANCE_PORT, TileEntityReactorMaintenancePort::new, TileEntityMekanism::tickServer, TileEntityMekanism::tickClient);
    }
}
