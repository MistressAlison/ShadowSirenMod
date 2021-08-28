package ShadowSiren.util;

import ShadowSiren.ShadowSirenMod;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class StarBarManager {
    public static final int AMOUNT_PER_POWER = 100;
    public static int progress = 0;
    public static int starPower = 0;
    public static int maxStarPower = 2;
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

    public static void resetVars() {
        progress = 0;
        starPower = 0;
        maxStarPower = 2;
    }

    public static void consumeStarPower(int amount) {
        starPower -= amount;
        if (starPower < 0) {
            starPower = 0;
        }
        StarBarPanel.fontScale = 2.0F;
    }

    public static void modifyMaxAmount(int deltaAmount) {
        maxStarPower += deltaAmount;
    }

    public static float makeCurrentAmount() {
        return starPower + (float)progress/AMOUNT_PER_POWER;
    }

    /*@SpirePatch(clz = AbstractDungeon.class, method = "dungeonTransitionSetup")
    public static class IncreaseMaxAmountOnNewAct {
        @SpirePrefixPatch
        public static void bigAmountTime() {
            if (AbstractDungeon.actNum <= 4) {
                modifyMaxAmount(2);
            }
        }
    }*/

}
