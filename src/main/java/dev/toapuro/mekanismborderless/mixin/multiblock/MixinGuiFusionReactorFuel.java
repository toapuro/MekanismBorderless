package dev.toapuro.mekanismborderless.mixin.multiblock;

import mekanism.client.gui.element.text.GuiTextField;
import mekanism.common.inventory.container.tile.EmptyTileContainer;
import mekanism.generators.client.gui.GuiFusionReactorFuel;
import mekanism.generators.client.gui.GuiFusionReactorInfo;
import mekanism.generators.common.content.fusion.FusionReactorMultiblockData;
import mekanism.generators.common.tile.fusion.TileEntityFusionReactorController;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GuiFusionReactorFuel.class, remap = false)
public abstract class MixinGuiFusionReactorFuel extends GuiFusionReactorInfo {

    @Shadow private GuiTextField injectionRateField;

    protected MixinGuiFusionReactorFuel(EmptyTileContainer<TileEntityFusionReactorController> container, Inventory inv, Component title) {
        super(container, inv, title);
    }

    @Inject(method = "addGuiElements", at = @At("TAIL"))
    public void addGuiElements(CallbackInfo ci) {
        FusionReactorMultiblockData multiblock = tile.getMultiblock();
        int volume = multiblock.getVolume();
        int maxInjectionRate = (volume / 125) * 99;
        injectionRateField.setMaxLength(Integer.toString(maxInjectionRate).length());
    }
}
