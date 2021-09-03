package ShadowSiren.patches;

import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class ReviveFix {
    @SpirePatch(clz = AbstractMonster.class, method = "heal")
    public static class StopRevivingWithZeroHP {
        @SpirePrefixPatch
        public static void fixTime(AbstractMonster __instance, @ByRef int[] healAmount) {
            if (__instance.currentHealth == 0 && healAmount[0] == 0) {
                healAmount[0] = 1;
            }
        }
    }
}
