package dev.toapuro.mekanismunlimited.block.tile;

import dev.toapuro.mekanismunlimited.registries.MBBlocks;
import mekanism.api.Action;
import mekanism.api.AutomationType;
import mekanism.api.IContentsListener;
import mekanism.api.math.FloatingLong;
import mekanism.common.capabilities.Capabilities;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.capabilities.holder.energy.EnergyContainerHelper;
import mekanism.common.capabilities.holder.energy.IEnergyContainerHolder;
import mekanism.common.capabilities.resolver.BasicCapabilityResolver;
import mekanism.common.lib.multiblock.MultiblockManager;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.tile.component.TileComponentConfig;
import mekanism.common.tile.component.config.ConfigInfo;
import mekanism.common.tile.component.config.DataType;
import mekanism.common.tile.prefab.TileEntityStructuralMultiblock;
import mekanism.common.util.MekanismUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class TileEntityReactorMaintenancePort extends TileEntityStructuralMultiblock implements ITileRepairBooster {

    public static FloatingLong MAX_RATE = FloatingLong.create(10_000); // 10K

    public TileComponentConfig configComponent; // Configurable
    private MachineEnergyContainer<TileEntityReactorMaintenancePort> energyContainer;

    private double repairRate = 0;

    public CompoundTag getConfigurationData(Player player) {
        CompoundTag data = super.getConfigurationData(player);
        configComponent.write(data);
        return data;
    }

    public void setConfigurationData(Player player, CompoundTag data) {
        super.setConfigurationData(player, data);
        configComponent.read(data);
    }

    public TileEntityReactorMaintenancePort(BlockPos pos, BlockState state) {
        super(MBBlocks.MAINTENANCE_PORT, pos, state);
        this.addCapabilityResolver(BasicCapabilityResolver.constant(Capabilities.CONFIG_CARD, this));

        configComponent = new TileComponentConfig(this, TransmissionType.ENERGY);
        ConfigInfo energyConfig = configComponent.setupInputConfig(TransmissionType.ENERGY, energyContainer);
        if(energyConfig != null) {
            energyConfig.setEjecting(false);
            energyConfig.fill(DataType.INPUT);
        }
    }

    @Override
    protected @Nullable IEnergyContainerHolder getInitialEnergyContainers(IContentsListener listener) {
        EnergyContainerHelper builder = EnergyContainerHelper.forSideWithConfig(this::getDirection, () -> configComponent);
        builder.addContainer(energyContainer = MachineEnergyContainer.input(this, listener));
        return builder.build();
    }

    @Override
    protected void onUpdateServer() {
        super.onUpdateServer();

        if(!hasFormedMultiblock()) return;
        FloatingLong extracted = energyContainer.extract(MAX_RATE, Action.SIMULATE, AutomationType.INTERNAL);
        FloatingLong actual = energyContainer.extract(extracted, Action.EXECUTE, AutomationType.INTERNAL);
        MekanismUtils.logMismatchedStackSize(extracted.longValue(), actual.longValue());

        repairRate = actual.divide(1_000_000).doubleValue();
    }

    @Override
    public boolean canInterface(MultiblockManager<?> manager) {
        return true;
    }

    @Override
    public double mekborderlss$getAdditionalRepairRate() {
        return repairRate;
    }
}
