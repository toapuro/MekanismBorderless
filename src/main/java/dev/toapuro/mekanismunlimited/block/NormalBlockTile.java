package dev.toapuro.mekanismunlimited.block;

import mekanism.common.block.prefab.BlockTile;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.tile.base.TileEntityMekanism;

import java.util.function.UnaryOperator;

public class NormalBlockTile<TILE extends TileEntityMekanism> extends BlockTile<TILE, BlockTypeTile<TILE>> {
    public NormalBlockTile(BlockTypeTile<TILE> tileBlockTypeTile, UnaryOperator<Properties> propertiesModifier) {
        super(tileBlockTypeTile, propertiesModifier);
    }

    public NormalBlockTile(BlockTypeTile<TILE> tileBlockTypeTile, Properties properties) {
        super(tileBlockTypeTile, properties);
    }
}
