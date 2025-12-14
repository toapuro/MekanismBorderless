package dev.toapuro.mekanismunlimited.block.attribute;

import mekanism.common.block.attribute.Attribute;
import mekanism.common.lib.multiblock.FormationProtocol;

public record AttributeCasing(FormationProtocol.CasingType casingType) implements Attribute {
}
