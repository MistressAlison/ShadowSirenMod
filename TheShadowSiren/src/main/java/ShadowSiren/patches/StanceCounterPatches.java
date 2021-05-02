package ShadowSiren.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import javassist.CtBehavior;

public class StanceCounterPatches {

    @SpirePatch(
            clz= AbstractPlayer.class,
            method=SpirePatch.CLASS
    )
    private static class StanceVar {
        //Used to hold the stance changes this turn
        public static SpireField<Integer> turnStanceChanges = new SpireField<>(() -> 0);

        //Used to hold the stance changes this combat
        public static SpireField<Integer> combatStanceChanges = new SpireField<>(() -> 0);
    }

    public static int getTurnStanceChanges(AbstractPlayer p) {
        return StanceVar.turnStanceChanges.get(p);
    }

    public static int getCombatStanceChanges(AbstractPlayer p) {
        return StanceVar.combatStanceChanges.get(p);
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "switchedStance")
    public static class SwitchedStancePatch {
        public static void Prefix(AbstractPlayer __instance) {
            StanceVar.turnStanceChanges.set(__instance, StanceVar.turnStanceChanges.get(__instance)+1);
            StanceVar.combatStanceChanges.set(__instance, StanceVar.combatStanceChanges.get(__instance)+1);
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "onVictory")
    @SpirePatch(clz = AbstractPlayer.class, method = "preBattlePrep")
    public static class OnVictoryPatch {
        public static void Prefix(AbstractPlayer __instance) {
            StanceVar.turnStanceChanges.set(__instance, 0);
            StanceVar.combatStanceChanges.set(__instance, 0);
        }
    }

    @SpirePatch(clz = GameActionManager.class, method = "getNextAction")
    public static class OverrideTypeRender {
        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(GameActionManager __instance) {
            StanceVar.turnStanceChanges.set(AbstractDungeon.player, 0);
        }
        private static class Locator extends SpireInsertLocator {
            private Locator() {
            }
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher matcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "applyStartOfTurnRelics");
                return LineFinder.findInOrder(ctBehavior, matcher);
            }
        }
    }
}
