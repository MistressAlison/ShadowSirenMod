package ShadowSiren.util;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.powers.StarFormPower;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class StarBarManager {
    public static final int AMOUNT_PER_POWER = 100;
    public static int progress = 0;
    public static int starPower = 0;
    public static int maxStarPower = 2;
    public static int tempMaxStarPower = 0;
    public static final String saveString = ShadowSirenMod.makeID("StarBarManager");

    public static class Container {
        public int pro;
        public int pow;
        public int mPow;
    }

    public static void addProgress(int amount) {
        if (starPower < maxStarPower) {
            if (CardCrawlGame.dungeon != null && AbstractDungeon.player != null && AbstractDungeon.player.hasPower(StarFormPower.POWER_ID)) {
                amount *= AbstractDungeon.player.getPower(StarFormPower.POWER_ID).amount;
            }
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

    public static void applyTemporaryStarPower(int amount) {
        maxStarPower += amount;
        tempMaxStarPower += amount;
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "preBattlePrep")
    public static class ResetToZero {
        @SpirePostfixPatch
        public static void byeBye(AbstractPlayer __instance) {
            progress = 0;
            starPower = 0;
        }
    }

}
