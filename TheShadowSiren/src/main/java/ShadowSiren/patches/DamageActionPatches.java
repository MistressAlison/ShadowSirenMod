package ShadowSiren.patches;

import ShadowSiren.powers.ConfusedPower;
import ShadowSiren.powers.DizzyPower;
import ShadowSiren.relics.DataCollector;
import ShadowSiren.relics.RepelCape;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.unique.VampireDamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import javassist.CtBehavior;

import java.util.ArrayList;

public class DamageActionPatches {

    @SpirePatch(clz = DamageAction.class, method = "update")
    public static class DamageActionRetarget {
        @SpireInsertPatch(locator = DamageActionRetarget.Locator.class, localvars = {"info"})
        public static void ChangeTarget(DamageAction __instance, @ByRef DamageInfo[] info) {
            if (__instance.source != null && __instance.source.hasPower(ConfusedPower.POWER_ID) && AbstractDungeon.cardRandomRng.random(0, 1) == 0) {
                ArrayList<AbstractCreature> targetList = new ArrayList<>();
                targetList.add(__instance.source);
                for (AbstractMonster aM : AbstractDungeon.getMonsters().monsters) {
                    if (!aM.isDeadOrEscaped() && aM != __instance.source) {
                        targetList.add(aM);
                    }
                }
                __instance.target = targetList.get(AbstractDungeon.cardRandomRng.random(0, targetList.size()-1));
                __instance.source.getPower(ConfusedPower.POWER_ID).flash();
                if (AbstractDungeon.player.hasRelic(DataCollector.ID)) {
                    DataCollector dataCollector = (DataCollector) AbstractDungeon.player.getRelic(DataCollector.ID);
                    dataCollector.onConfusedDamage(info[0].output);
                }
            }
        }
        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(ArrayList.class, "add");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(clz = VampireDamageAction.class, method = "update")
    public static class VampireDamageActionRetarget {
        @SpireInsertPatch(locator = VampireDamageActionRetarget.Locator.class, localvars = {"info"})
        public static void ChangeTarget(VampireDamageAction __instance, @ByRef DamageInfo[] info) {
            if (__instance.source != null && __instance.source.hasPower(ConfusedPower.POWER_ID) && AbstractDungeon.cardRandomRng.random(0, 1) == 0) {
                ArrayList<AbstractCreature> targetList = new ArrayList<>();
                targetList.add(__instance.source);
                for (AbstractMonster aM : AbstractDungeon.getMonsters().monsters) {
                    if (!aM.isDeadOrEscaped() && aM != __instance.source) {
                        targetList.add(aM);
                    }
                }
                __instance.target = targetList.get(AbstractDungeon.cardRandomRng.random(0, targetList.size()-1));
                __instance.source.getPower(ConfusedPower.POWER_ID).flash();
                if (AbstractDungeon.player.hasRelic(DataCollector.ID)) {
                    DataCollector dataCollector = (DataCollector) AbstractDungeon.player.getRelic(DataCollector.ID);
                    dataCollector.onConfusedDamage(info[0].output);
                }
            }
        }
        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(ArrayList.class, "add");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(clz = VampireDamageAction.class, method = "update")
    @SpirePatch(clz = DamageAction.class, method = "update")
    public static class DamageActionMiss {
        @SpirePrefixPatch()
        public static SpireReturn<?> MissTarget(AbstractGameAction __instance) {
            //If the source of the action is dizzy...
            if (__instance.source != null && __instance.source.hasPower(DizzyPower.POWER_ID) && !MissFlags.dizzyFlag.get(__instance)) {
                //Check a 50/50 flag. Could use random bool, oh well.
                if (AbstractDungeon.cardRandomRng.random(0, 1) == 0) {
                    //Set the instance is done, flash the power, and expressly return from the update function so nothing else happens
                    __instance.isDone = true;
                    __instance.source.getPower(DizzyPower.POWER_ID).flash();
                    DamageInfo info = null;
                    if (__instance instanceof DamageAction) {
                        info = ReflectionHacks.getPrivate(__instance, DamageAction.class, "info");
                    } else if (__instance instanceof VampireDamageAction) {
                        info = ReflectionHacks.getPrivate(__instance, VampireDamageAction.class, "info");
                    }
                    if (info != null) {
                        if (AbstractDungeon.player.hasRelic(DataCollector.ID)) {
                            DataCollector dataCollector = (DataCollector) AbstractDungeon.player.getRelic(DataCollector.ID);
                            dataCollector.onDizzyMiss(info.output);
                        }
                    }
                    return SpireReturn.Return(null);
                }
                //If we failed the check, set the flag for it so we don't keep trying each time update is called
                MissFlags.dizzyFlag.set(__instance, true);
            }
            //Removed this functionality, but Im keeping it for future reference if I want to reimplement it in a better way
            /*if (__instance.source != null && __instance.target instanceof AbstractPlayer && ((AbstractPlayer) __instance.target).stance.ID.equals(VoidStance.STANCE_ID) && !MissFlags.dodgyFlag.get(__instance)) {
                if (AbstractDungeon.cardRandomRng.random(0, 1) == 0) {
                    __instance.isDone = true;
                    return SpireReturn.Return(null);
                }
                MissFlags.dodgyFlag.set(__instance, true);
            }*/
            //Likewise, check if we have the Repel Cape
            if (__instance.source != null && __instance.target instanceof AbstractPlayer && ((AbstractPlayer) __instance.target).hasRelic(RepelCape.ID) && !MissFlags.capeFlag.get(__instance)) {
                //Compare its percent chance to avoid...
                if (AbstractDungeon.cardRandomRng.random(0, 99) < RepelCape.DODGE_PERCENT) {
                    //Se the instance as done and flash the relic
                    __instance.isDone = true;
                    ((RepelCape) ((AbstractPlayer) __instance.target).getRelic(RepelCape.ID)).flashRelicAbovePlayer();
                    //Grab the damage we were going to take and pass it back for Relic Stats purposes
                    DamageInfo info = null;
                    if (__instance instanceof DamageAction) {
                        info = ReflectionHacks.getPrivate(__instance, DamageAction.class, "info");
                    } else if (__instance instanceof VampireDamageAction) {
                        info = ReflectionHacks.getPrivate(__instance, VampireDamageAction.class, "info");
                    }
                    if (info != null) {
                        ((RepelCape) ((AbstractPlayer) __instance.target).getRelic(RepelCape.ID)).onAvoidDamage(info.output);
                    }
                    //Return the function call so nothing else happens
                    return SpireReturn.Return(null);
                }
                //IF we failed our check, set the flag for it so we don't keep trying
                MissFlags.capeFlag.set(__instance, true);
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz= AbstractGameAction.class, method=SpirePatch.CLASS)
    private static class MissFlags {
        //Used to hold the boolean for if we tried Dizzy already
        public static SpireField<Boolean> dizzyFlag = new SpireField<>(() -> false);

        //Used to hold the boolean for if we tried Void already
        //public static SpireField<Boolean> dodgyFlag = new SpireField<>(() -> false);

        //Used to hold the boolean for if we tried Repel Cape already
        public static SpireField<Boolean> capeFlag = new SpireField<>(() -> false);
    }
}
