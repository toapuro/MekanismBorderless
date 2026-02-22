package dev.toapuro.mekanismborderless;

import dev.toapuro.mekanismborderless.builder.ExtendedBuildCommand;
import dev.toapuro.mekanismborderless.builder.ExtendedMultiblockBuilder;
import dev.toapuro.mekanismborderless.builder.supported.*;
import mekanism.common.MekanismLang;
import mekanism.common.base.MekanismPermissions;
import mekanism.common.command.builders.Builders;
import mekanism.common.content.boiler.BoilerValidator;
import mekanism.common.content.evaporation.EvaporationValidator;
import mekanism.common.content.matrix.MatrixValidator;
import mekanism.common.content.sps.SPSValidator;
import mekanism.common.content.tank.TankValidator;
import mekanism.generators.common.GeneratorsLang;
import mekanism.generators.common.content.fission.FissionReactorValidator;
import mekanism.generators.common.content.fusion.FusionReactorValidator;
import mekanism.generators.common.content.turbine.TurbineValidator;
import mekanism.generators.common.registries.GeneratorsBuilders;
import net.minecraft.commands.Commands;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod(MekanismBorderless.MODID)
public class MekanismBorderless {

    public static final String MODID = "mekanismborderless";

    @SuppressWarnings("removal")
    public MekanismBorderless() {
        MBClientConfig.initializeConfig();

        ModLoadingContext loadingContext = ModLoadingContext.get();
        loadingContext.registerConfig(ModConfig.Type.CLIENT, MBClientConfig.SPEC);
        loadingContext.registerConfig(ModConfig.Type.SERVER, MBServerConfig.getSpec());

        MinecraftForge.EVENT_BUS.addListener(this::registerCommands);
    }

    @SubscribeEvent
    public void registerCommands(RegisterCommandsEvent event) {
        // Mekanism
        ExtendedBuildCommand.register("boiler", MekanismLang.BOILER, new BoilerExtendedBuilder(new Builders.BoilerBuilder(), BoilerValidator::new));
        ExtendedBuildCommand.register("matrix", MekanismLang.MATRIX, new ExtendedMultiblockBuilder(new Builders.MatrixBuilder(), MatrixValidator::new));
        ExtendedBuildCommand.register("tank", MekanismLang.DYNAMIC_TANK, new ExtendedMultiblockBuilder(new Builders.TankBuilder(), TankValidator::new));
        ExtendedBuildCommand.register("evaporation", MekanismLang.EVAPORATION_PLANT, new EvaporationExtendedBuilder(new Builders.EvaporationBuilder(), EvaporationValidator::new));
        ExtendedBuildCommand.register("sps", MekanismLang.SPS, new SPSExtendedBuilder(new Builders.SPSBuilder(), SPSValidator::new));

        // Mekanism Generators
        ExtendedBuildCommand.register("turbine", GeneratorsLang.TURBINE, new TurbineExtendedBuilder(new GeneratorsBuilders.TurbineBuilder(), TurbineValidator::new));
        ExtendedBuildCommand.register("fission", GeneratorsLang.FISSION_REACTOR, new FissionReactorExtendedBuilder(new GeneratorsBuilders.FissionReactorBuilder(), FissionReactorValidator::new));
        ExtendedBuildCommand.register("fusion", GeneratorsLang.FUSION_REACTOR, new FusionReactorExtendedBuilder(new GeneratorsBuilders.FusionReactorBuilder(), FusionReactorValidator::new));


        event.getDispatcher().register(
                Commands.literal("mekborderless")
                        .requires(MekanismPermissions.COMMAND)
                        .then(ExtendedBuildCommand.COMMAND)
        );
    }
}
