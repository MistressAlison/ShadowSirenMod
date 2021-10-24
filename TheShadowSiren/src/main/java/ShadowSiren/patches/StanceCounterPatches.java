package ShadowSiren.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.stances.NeutralStance;
import javassist.CtBehavior;

import java.util.HashSet;

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

        //Used to hold the unique stances we visit
        public static SpireField<HashSet<String>> stancesVisited = new SpireField<>(HashSet::new);

        /*//Used to hold if we entered Veil
        public static SpireField<Boolean> enteredVeilThisCombat = new SpireField<>(() -> Boolean.FALSE);

        //Used to hold if we entered Abyss
        public static SpireField<Boolean> enteredAbyssThisCombat = new SpireField<>(() -> Boolean.FALSE);

        //Used to hold if we entered Smoke
        public static SpireField<Boolean> enteredSmokeThisCombat = new SpireField<>(() -> Boolean.FALSE);

        //Used to hold if we entered Huge
        public static SpireField<Boolean> enteredHugeThisCombat = new SpireField<>(() -> Boolean.FALSE);

        //Used to hold if we entered Hyper
        public static SpireField<Boolean> enteredHyperThisCombat = new SpireField<>(() -> Boolean.FALSE);*/

    }

    public static int getTurnStanceChanges(AbstractPlayer p) {
        return StanceVar.turnStanceChanges.get(p);
    }

    public static int getCombatStanceChanges(AbstractPlayer p) {
        return StanceVar.combatStanceChanges.get(p);
    }

    public static int getCombatUniqueStances(AbstractPlayer p) {
        /*int retVal = 0;
        retVal += StanceVar.enteredVeilThisCombat.get(p) ? 1 : 0;
        retVal += StanceVar.enteredAbyssThisCombat.get(p) ? 1 : 0;
        retVal += StanceVar.enteredSmokeThisCombat.get(p) ? 1 : 0;
        retVal += StanceVar.enteredHugeThisCombat.get(p) ? 1 : 0;
        retVal += StanceVar.enteredHyperThisCombat.get(p) ? 1 : 0;*/
        return StanceVar.stancesVisited.get(p).size();
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "switchedStance")
    public static class SwitchedStancePatch {
        public static void Prefix(AbstractPlayer __instance) {
            StanceVar.turnStanceChanges.set(__instance, StanceVar.turnStanceChanges.get(__instance)+1);
            StanceVar.combatStanceChanges.set(__instance, StanceVar.combatStanceChanges.get(__instance)+1);
            //We don't consider Neutral to be a stance, since it is the lack of a stance
            if (!__instance.stance.ID.equals(NeutralStance.STANCE_ID)) {
                StanceVar.stancesVisited.get(__instance).add(__instance.stance.ID);
            }
            /*if (__instance.stance.ID.equals(VeilStance.STANCE_ID)) {
                StanceVar.enteredVeilThisCombat.set(__instance, true);
            } else if (__instance.stance.ID.equals(AbyssStance.STANCE_ID)) {
                StanceVar.enteredAbyssThisCombat.set(__instance, true);
            } else if (__instance.stance.ID.equals(SmokeStance.STANCE_ID)) {
                StanceVar.enteredSmokeThisCombat.set(__instance, true);
            } else if (__instance.stance.ID.equals(HugeStance.STANCE_ID)) {
                StanceVar.enteredHugeThisCombat.set(__instance, true);
            } else if (__instance.stance.ID.equals(HyperStance.STANCE_ID)) {
                StanceVar.enteredHyperThisCombat.set(__instance, true);
            }*/
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "onVictory")
    @SpirePatch(clz = AbstractPlayer.class, method = "preBattlePrep")
    public static class OnVictoryPatch {
        public static void Prefix(AbstractPlayer __instance) {
            StanceVar.turnStanceChanges.set(__instance, 0);
            StanceVar.combatStanceChanges.set(__instance, 0);
            StanceVar.stancesVisited.get(__instance).clear();
            /*
            StanceVar.enteredVeilThisCombat.set(__instance, false);
            StanceVar.enteredAbyssThisCombat.set(__instance, false);
            StanceVar.enteredSmokeThisCombat.set(__instance, false);
            StanceVar.enteredHugeThisCombat.set(__instance, false);
            StanceVar.enteredHyperThisCombat.set(__instance, false);*/
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
