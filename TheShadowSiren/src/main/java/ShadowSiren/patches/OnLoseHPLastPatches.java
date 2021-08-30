package ShadowSiren.patches;

import ShadowSiren.stances.OnLoseHPStance;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import javassist.CtBehavior;

public class OnLoseHPLastPatches {
    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "damage"

    )
    public static class DamageTakenListener {
        @SpireInsertPatch(locator = Locator.class, localvars = {"damageAmount"})
        public static void damageTakenListener(AbstractPlayer __instance, DamageInfo info, @ByRef int[] damageAmount) {
            if (__instance.stance instanceof OnLoseHPStance) {
                damageAmount[0] = ((OnLoseHPStance) __instance.stance).onLoseHP(info, damageAmount[0]);
            }
        }
        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(Math.class, "min");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(
            clz = AbstractMonster.class,
            method = "damage"

    )
    public static class MonsterDamageTakenListener {
        @SpireInsertPatch(locator = Locator.class, localvars = {"damageAmount"})
        public static SpireReturn<?> damageTakenListener(AbstractMonster __instance, DamageInfo info, @ByRef int[] damageAmount) {
            /*if (__instance.hasPower(BurnPower.POWER_ID)) {
                //If it has already activated the suicide sequence, we ignore all future damage actions at this point
                if (((BurnPower)__instance.getPower(BurnPower.POWER_ID)).activated) {
                    return SpireReturn.Return(null);
                } else {
                    //Else check to see if the suicide action should trigger
                    ((BurnPower)__instance.getPower(BurnPower.POWER_ID)).deathCheck(damageAmount[0]);
                }
            }*/
            return SpireReturn.Continue();
        }
        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(Math.class, "min");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}
