package dev.toapuro.mekanismunlimited.util;

import com.google.common.primitives.Ints;
import mekanism.common.lib.math.voxel.VoxelCuboid;

public class MBUtil {

    public static int getStructureVolumeInt(VoxelCuboid cuboid) {
        return Ints.saturatedCast(
                (long) Ints.saturatedCast(
                        (long) cuboid.length() * (long) cuboid.width()
                ) * (long) cuboid.height()
        );
    }
}
