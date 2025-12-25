package dev.toapuro.mekanismunlimited.util.timer;

public class TileTickTracker {

    private long lastClaimed;

    /**
     * Returns true only on the first call made during a single server tick.
     * If this method is called multiple times in the same tick, it reports
     * true once and false after that.
     */
    public boolean claimTick() {
        int ticks = ServerTickTracker.INSTANCE.getTicks();

        if(lastClaimed != ticks) {
            lastClaimed = ticks;
            return true;
        }
        return false;
    }
}
