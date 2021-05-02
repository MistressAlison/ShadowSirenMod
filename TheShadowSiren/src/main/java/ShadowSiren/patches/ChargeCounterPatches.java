package ShadowSiren.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;

public class ChargeCounterPatches {

    @SpirePatch(clz = AbstractPlayer.class, method = SpirePatch.CLASS)
    private static class ChargeVar {
        //Used to hold the charges gained this combat
        public static SpireField<Integer> chargesThisCombat = new SpireField<>(() -> 0);
    }

    //getter
    public static int getChargesThisCombat(AbstractPlayer p) {
        return ChargeVar.chargesThisCombat.get(p);
    }

    //incrementer. Should be called in stackPower and initialApplication
    public static void incrementChargesThisCombat(AbstractPlayer p, int amount) {
        ChargeVar.chargesThisCombat.set(p, ChargeVar.chargesThisCombat.get(p) + amount);
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "onVictory")
    @SpirePatch(clz = AbstractPlayer.class, method = "preBattlePrep")
    public static class OnVictoryPatch {
        public static void Prefix(AbstractPlayer __instance) {
            //Reset our counter to 0
            ChargeVar.chargesThisCombat.set(__instance, 0);
        }
    }
}
