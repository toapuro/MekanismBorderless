package dev.toapuro.mekanismborderless.util;

import mekanism.common.lib.math.voxel.VoxelCuboid;
import mekanism.common.lib.math.voxel.VoxelPlane;
import mekanism.common.lib.multiblock.FormationProtocol;
import mekanism.common.lib.multiblock.Structure;

import java.util.Map;
import java.util.NavigableMap;

@SuppressWarnings("JavadocReference")
public class MBStructureHelper {

    /**
     * @param size The size must be an odd number
     * @return {@link FormationProtocol.StructureRequirement#ordinal()}
     */
    public static byte getCircleStructureRequirement(int x, int y, int size, int radius) {
        int center = size / 2;

        // manhattan distance
        int dst = Math.abs(center - x) + Math.abs(center - y);

        // outside
        if (dst > radius) {
            return 0;
        }

        // boundary (circle edge)
        if (dst == radius) {
            return 1;
        }

        // grid border
        if (x == 0 || y == 0 || x == size - 1 || y == size - 1) {
            return 1;
        }

        // inside
        return 2;
    }

    public static VoxelCuboid fetchCuboid(Structure structure, VoxelCuboid minBounds, VoxelCuboid maxBounds, int tolelance) {
        VoxelCuboid prev = null;
        for (Structure.Axis axis : Structure.Axis.values()) {
            NavigableMap<Integer, VoxelPlane> majorAxisMap = structure.getMajorAxisMap(axis);
            Map.Entry<Integer, VoxelPlane> firstMajor = majorAxisMap.firstEntry(), lastMajor = majorAxisMap.lastEntry();
            if (firstMajor == null || !firstMajor.getValue().equals(lastMajor.getValue())) {
                //If there is no major plane, or the two parallel planes are mismatched in size,
                return null;
            }
            if(firstMajor.getValue().getMissing() > tolelance) {
                return null;
            }

            VoxelCuboid cuboid = VoxelCuboid.from(firstMajor.getValue(), lastMajor.getValue(), firstMajor.getKey(), lastMajor.getKey());
            // if this is the first axial cuboid check, make sure we have the correct bounds
            if (prev == null && (!cuboid.greaterOrEqual(minBounds) || !maxBounds.greaterOrEqual(cuboid))) {
                return null;
            }
            // if this isn't the first axial cuboid check, make sure the cuboids match
            if (prev != null && !prev.equals(cuboid)) {
                return null;
            }
            // check to make sure that we don't have any framed minor planes sticking out of our cuboid
            NavigableMap<Integer, VoxelPlane> minorAxisMap = structure.getMinorAxisMap(axis);
            if (!minorAxisMap.isEmpty()) {
                if (hasOutOfBoundsNegativeMinor(minorAxisMap, firstMajor.getKey()) || hasOutOfBoundsPositiveMinor(minorAxisMap, lastMajor.getKey())) {
                    return null;
                }
            }
            prev = cuboid;
        }
        return prev;
    }


    /**
     * {@link mekanism.common.lib.multiblock.StructureHelper#hasOutOfBoundsNegativeMinor(NavigableMap, int)}
     */
    private static boolean hasOutOfBoundsNegativeMinor(NavigableMap<Integer, VoxelPlane> minorAxisMap, int majorKey) {
        int minorKey;
        for(Map.Entry<Integer, VoxelPlane> minorEntry = minorAxisMap.firstEntry(); minorEntry != null; minorEntry = minorAxisMap.higherEntry(minorKey)) {
            minorKey = minorEntry.getKey();
            if (minorKey >= majorKey) {
                break;
            }

            if (minorEntry.getValue().hasFrame()) {
                return true;
            }
        }

        return false;
    }

    /**
     * {@link mekanism.common.lib.multiblock.StructureHelper#hasOutOfBoundsPositiveMinor(NavigableMap, int)}
     */
    private static boolean hasOutOfBoundsPositiveMinor(NavigableMap<Integer, VoxelPlane> minorAxisMap, int majorKey) {
        Map.Entry<Integer, VoxelPlane> minorEntry = minorAxisMap.lastEntry();
        while (minorEntry != null) {
            int minorKey = minorEntry.getKey();
            if (minorKey <= majorKey) {
                //If our outer minor plane is in the bounds of our major plane
                // then just exit as the other minor planes will be as well
                break;
            } else if (minorEntry.getValue().hasFrame()) {
                //Otherwise, if it isn't in the bounds, and it has a frame, fail out
                return true;
            }
            //If we don't have a frame and are out of bounds, see if we have any minor entries that
            // are "smaller" that may be invalid
            minorEntry = minorAxisMap.lowerEntry(minorKey);
        }
        return false;
    }
}
