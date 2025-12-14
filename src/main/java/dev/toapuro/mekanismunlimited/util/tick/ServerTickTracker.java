package dev.toapuro.mekanismunlimited.util.tick;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ServerTickTracker {

    public static final ServerTickTracker INSTANCE = new ServerTickTracker();

    private int ticks;

    public void onTickServer() {
        ticks++;
    }

    @SubscribeEvent
    public static void onTickServerTick(TickEvent.ServerTickEvent event) {
        INSTANCE.onTickServer();
    }

    public int getTicks() {
        return ticks;
    }
}
