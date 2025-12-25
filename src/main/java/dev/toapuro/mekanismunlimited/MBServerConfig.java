package dev.toapuro.mekanismunlimited;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.core.UnmodifiableConfig;
import com.google.common.primitives.Ints;
import dev.toapuro.mekanismunlimited.mixin.accessor.MultiblockManagerAccessor;
import dev.toapuro.mekanismunlimited.util.MBUtil;
import mekanism.common.lib.math.voxel.VoxelCuboid;
import mekanism.common.lib.multiblock.IStructureValidator;
import mekanism.common.lib.multiblock.MultiblockManager;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Stream;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class MBServerConfig {
    private static final Supplier<ForgeConfigSpec> lazySpec = Lazy.of(MBServerConfig::buildDefaultSpec);
    private static final Logger LOGGER = LoggerFactory.getLogger(MBServerConfig.class);

    private static CuboidConfig defaultBound;
    private static final Map<Class<?>, CuboidConfig> specificBoundMap = new HashMap<>();

    public static ForgeConfigSpec getSpec() {
        return lazySpec.get();
    }

    private static void defineCuboidConfig(ForgeConfigSpec.Builder builder, String prefix, boolean defaultOverride) {
        builder.define(prefix + "BoundOverride", defaultOverride);
        builder.define(prefix + "StructureXBound", 1920);
        builder.define(prefix + "StructureYBound", 1920);
        builder.define(prefix + "StructureZBound", 1920);
    }

    public static <T> Optional<T> readConfig(UnmodifiableConfig config, String name) {
        return Optional.ofNullable(config.get(name));
    }

    @Nullable
    private static CuboidConfig readCuboidConfig(String prefix, UnmodifiableConfig config) {
        Optional<Boolean> overrides = readConfig(config, prefix + "BoundOverride");
        Optional<Integer> maxX = readConfig(config, prefix + "StructureXBound");
        Optional<Integer> maxY = readConfig(config, prefix + "StructureYBound");
        Optional<Integer> maxZ = readConfig(config, prefix + "StructureZBound");

        if(overrides.isEmpty() || maxX.isEmpty() || maxY.isEmpty() || maxZ.isEmpty()) {
            return null;
        }

        return new CuboidConfig(overrides.get(), maxX.get(), maxY.get(), maxZ.get());
    }

    // Lazy init due to manager dependency
    public static ForgeConfigSpec buildDefaultSpec() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        List<String> loadClasses = List.of(
                "mekanism.common.Mekanism",
                "mekanism.generators.common.MekanismGenerators"
        );

        for (String loadClass : loadClasses) {
            try {
                Class.forName(loadClass);
            } catch (ClassNotFoundException ignored) {
            }
        }

        // Default Bound
        defineCuboidConfig(builder, "default", true);

        // Structure-specific Bounds
        Set<MultiblockManager<?>> staticManagers = MultiblockManagerAccessor.getStaticManagers();
        for (MultiblockManager<?> manager : staticManagers) {
            String name = manager.getName();
            builder.push(name);

            defineCuboidConfig(builder, name, false);

            builder.pop();
        }

        return builder.build();
    }

    @SubscribeEvent
    public static void onLoadConfig(ModConfigEvent.Loading event) {
        ForgeConfigSpec spec = getSpec();
        if(event.getConfig().getSpec() != spec) {
            return;
        }

        LOGGER.info("Loading config");
        CommentedConfig values = event.getConfig().getConfigData();

        defaultBound = readCuboidConfig("default", values);
        Objects.requireNonNull(defaultBound, "Require default config");

        Set<MultiblockManager<?>> staticManagers = MultiblockManagerAccessor.getStaticManagers();
        for (MultiblockManager<?> manager : staticManagers) {
            String name = manager.getName();
            IStructureValidator<?>  validator = manager.createValidator();

            CuboidConfig cuboidConfig = readCuboidConfig(name + "." + name, values);
            if(cuboidConfig != null) {
                specificBoundMap.put(validator.getClass(), cuboidConfig);
            }
        }
    }

    @Nullable
    public static VoxelCuboid getStructureCuboid(Class<?> validatorClass) {
        CuboidConfig specificBound = specificBoundMap.get(validatorClass);
        if(specificBound != null && specificBound.overrides) {
            return specificBound.getCuboid();
        }

        if(defaultBound.overrides) {
            return defaultBound.getCuboid();
        }

        return null;
    }

    public static Stream<VoxelCuboid> getAllCuboids() {
        List<VoxelCuboid> specificCuboidsNullable = specificBoundMap.values().stream()
                .map(CuboidConfig::getCuboid)
                .toList();
        Stream<VoxelCuboid> cuboids = Stream.concat(specificCuboidsNullable.stream(), Stream.of(defaultBound.getCuboid()));

        return cuboids
                .filter(Objects::nonNull);
    }

    public static Optional<Integer> getMaxStructureVolume() {
        return MBServerConfig.getAllCuboids()
                .map(MBUtil::getStructureVolumeInt)
                .max(Integer::compareTo)
                .map(Ints::saturatedCast);
    }

    record CuboidConfig(
            boolean overrides,
            int maxX,
            int maxY,
            int maxZ
    ) {
        @Nullable
        public VoxelCuboid getCuboid() {
            if(!overrides) return null;

            return new VoxelCuboid(maxX, maxY, maxZ);
        }
    }
}
