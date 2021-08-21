package ShadowSiren.util;

import ShadowSiren.ShadowSirenMod;

public class StarBarManager {
    private static final int AMOUNT_PER_POWER = 10;
    public static int progress = 0;
    public static int starPower = 0;
    public static int maxStarPower = 1;
    public static final String saveString = ShadowSirenMod.makeID("StarBarManager");

    public static class Container {
        public int pro;
        public int pow;
        public int mPow;
    }

    public static void addProgress(int amount) {
        if (starPower < maxStarPower){
            progress += amount;
            while (progress >= AMOUNT_PER_POWER) {
                starPower++;
                progress -= AMOUNT_PER_POWER;
                StarBarPanel.vfxTimer = 2f;
            }
            if (starPower >= maxStarPower) {
                progress = 0;
                starPower = maxStarPower;
            }
            StarBarPanel.fontScale = 2.0F;
        }
    }

    public static void consumeStarPower(int amount) {
        maxStarPower -= amount;
        if (maxStarPower < 0) {
            maxStarPower = 0;
        }
        StarBarPanel.fontScale = 2.0F;
    }

    public static float makeCurrentAmount() {
        return starPower + progress/10f;
    }

}
