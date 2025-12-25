package dev.toapuro.mekanismunlimited.util;

import com.google.common.primitives.Ints;
import dev.toapuro.mekanismunlimited.MBServerConfig;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.core.BlockPos;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.function.Predicate;

public class DiagonalFormationProtocol {

    public static int explore(BlockPos start, Predicate<BlockPos> checker) {
        return explore(start, checker, Ints.saturatedCast(MBServerConfig.getMaxStructureVolume().orElse(5832)));
    }

    public static int explore(BlockPos start, Predicate<BlockPos> checker, int maxCount) {
        if (!checker.test(start)) {
            return 0;
        }
        
        Queue<BlockPos> openSet = new LinkedList<>();
        Set<BlockPos> traversed = new ObjectOpenHashSet<>();
        openSet.add(start);
        traversed.add(start);

        while(!openSet.isEmpty()) {
            BlockPos ptr = openSet.poll();
            int traversedSize = traversed.size();
            if (traversedSize >= maxCount) {
                return traversedSize;
            }

            for (int i = 0; i < 27; i++) {
                int x = i % 3 - 1;
                int y = (i / 3) % 3 - 1;
                int z = (i / 9) % 3 - 1;
                BlockPos offset = ptr.offset(x, y, z);
                if (!traversed.contains(offset) && checker.test(offset)) {
                    openSet.add(offset);
                    traversed.add(offset);
                }
            }
        }

        return traversed.size();
    }
}
