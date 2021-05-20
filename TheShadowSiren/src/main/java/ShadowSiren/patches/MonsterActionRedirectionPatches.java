package ShadowSiren.patches;

import ShadowSiren.actions.MakeTempCardInExhaustAction;
import ShadowSiren.powers.ConfusedPower;
import ShadowSiren.powers.DizzyPower;
import ShadowSiren.powers.HexingPower;
import ShadowSiren.powers.SinisterReflectionPower;
import ShadowSiren.relics.DataCollector;
import ShadowSiren.relics.PointSwap;
import ShadowSiren.relics.RepelCape;
import basemod.ReflectionHacks;
import basemod.interfaces.CloneablePowerInterface;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.unique.VampireDamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import javassist.CtBehavior;

import java.util.ArrayList;

public class MonsterActionRedirectionPatches {

    @SpirePatch(clz= AbstractCreature.class, method=SpirePatch.CLASS)
    private static class MissFlags {
        //Used to hold the boolean for if we tried Dizzy already
        public static SpireField<Boolean> dizzyFlag = new SpireField<>(() -> false);

        //Used to hold the boolean for if we tried Confused already
        public static SpireField<Boolean> confusedFlag = new SpireField<>(() -> false);

        //Used to hold the new target of a confused monster
        public static SpireField<AbstractCreature> targetField = new SpireField<>(() -> null);

        //Used to hold the boolean for if we tried Repel Cape already
        public static SpireField<Boolean> capeFlag = new SpireField<>(() -> false);
    }

    //Make sure to reset else there will be problems, lol
    public static void resetFlags(AbstractCreature m) {
        MissFlags.dizzyFlag.set(m, false);
        MissFlags.confusedFlag.set(m, false);
        MissFlags.capeFlag.set(m, false);
        MissFlags.targetField.set(m, null);
    }

    @SpirePatch(clz = GameActionManager.class, method = "getNextAction")
    public static class ResetFlags {
        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(GameActionManager __instance) {
            //Reset everyone's flags. We don't care if they are dead or not because they could come back to life, so play it safe and reset everything
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                resetFlags(m);
            }
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

    @SpirePatch(clz= GameActionManager.class, method=SpirePatch.CLASS)
    private static class MakeCardFlags {
        //Used to hold the boolean for if we hit a miss/redirect. This will stop us from getting status cards
        public static SpireField<Boolean> attackMissed = new SpireField<>(() -> false);
    }

    public static void resetCardFlag(GameActionManager g) {
        MakeCardFlags.attackMissed.set(g, false);
    }

    @SpirePatch(clz = GameActionManager.class, method = "getNextAction")
    public static class SetFlags {
        //Pls grab the correct local var...
        @SpireInsertPatch(locator = Locator.class, localvars = {"m"})
        public static void Insert(GameActionManager __instance, @ByRef AbstractMonster[] m) {
            //Check if the monster about to take a turn is Dizzy
            if (m[0].hasPower(DizzyPower.POWER_ID)) {
                //If they are, and fail the check, set the dizzy flag to be used in the other patches
                if (AbstractDungeon.monsterRng.randomBoolean()) {
                    MissFlags.dizzyFlag.set(m[0], true);
                    MakeCardFlags.attackMissed.set(__instance, true);
                    //Flash the power so the player knows something happened that will influence the turn
                    m[0].getPower(DizzyPower.POWER_ID).flash();
                }
            }
            //Do the same with Confused, but also grab a new target
            if (m[0].hasPower(ConfusedPower.POWER_ID)) {
                if (AbstractDungeon.monsterRng.randomBoolean()) {
                    MissFlags.confusedFlag.set(m[0], true);
                    MakeCardFlags.attackMissed.set(__instance, true);
                    //Create a list to hold potential valid targets
                    ArrayList<AbstractCreature> targetList = new ArrayList<>();
                    //Add the owner of the attack and all alive monsters to the valid targets list
                    targetList.add(m[0]);
                    for (AbstractMonster aM : AbstractDungeon.getMonsters().monsters) {
                        if (!aM.isDeadOrEscaped() && aM != m[0]) {
                            targetList.add(aM);
                        }
                    }
                    //Set our new target for use in redirection attacks
                    MissFlags.targetField.set(m[0], targetList.get(AbstractDungeon.cardRandomRng.random(0, targetList.size()-1)));
                    //Flash the power so the player knows something happened that will influence the turn
                    m[0].getPower(ConfusedPower.POWER_ID).flash();
                }
            }
            //A slightly different one, this will set if the monster will miss thanks to the player's relic
            if (AbstractDungeon.player.hasRelic(RepelCape.ID)) {
                if (AbstractDungeon.monsterRng.random(0, 99) < RepelCape.DODGE_PERCENT) {
                    MissFlags.capeFlag.set(m[0], true);
                    MakeCardFlags.attackMissed.set(__instance, true);
                    //Flash the relic so the player knows something happened that will influence the turn
                    ((RepelCape) AbstractDungeon.player.getRelic(RepelCape.ID)).flashRelicAbovePlayer();
                }
            }
        }
        private static class Locator extends SpireInsertLocator {
            private Locator() {
            }
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher matcher = new Matcher.MethodCallMatcher(AbstractMonster.class, "takeTurn");
                return LineFinder.findInOrder(ctBehavior, matcher);
            }
        }
    }

    @SpirePatch(clz = GameActionManager.class, method = "addToBottom")
    @SpirePatch(clz = GameActionManager.class, method = "addToTop")
    public static class DontMakeTempCardPls {
        @SpirePrefixPatch()
        public static SpireReturn<?> MissTarget(GameActionManager __instance, AbstractGameAction action) {
            if (MakeCardFlags.attackMissed.get(__instance)) {
                if (action instanceof MakeTempCardAtBottomOfDeckAction
                        || action instanceof MakeTempCardInDiscardAction
                        || action instanceof MakeTempCardInDiscardAndDeckAction
                        || action instanceof MakeTempCardInDrawPileAction
                        || action instanceof MakeTempCardInHandAction
                        || action instanceof MakeTempCardInExhaustAction) {
                    return SpireReturn.Return(null);
                }
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = GameActionManager.class, method = "getNextAction")
    public static class ResetCardFlag {
        @SpireInsertPatch(locator = Locator.class, localvars = {"m"})
        public static void Insert(GameActionManager __instance, @ByRef AbstractMonster[] m) {
            //We already took our turn, and our atb/att patches should have triggered by now. reset the flag
            resetCardFlag(__instance);
        }
        private static class Locator extends SpireInsertLocator {
            private Locator() {
            }
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher matcher = new Matcher.MethodCallMatcher(AbstractMonster.class, "applyTurnPowers");
                return LineFinder.findInOrder(ctBehavior, matcher);
            }
        }
    }

    @SpirePatch(clz = DamageAction.class, method = "update")
    public static class DamageActionRetarget {
        @SpireInsertPatch(locator = Locator.class, localvars = {"info"})
        public static void ChangeTarget(DamageAction __instance, @ByRef DamageInfo[] info) {
            //This insert patch takes place after the dizzy prefix patch. As such, this code shouldn't run if the target was going to miss anyway
            //If the source isn't null, and the source of the action failed the confusion check...
            if (__instance.source != null && MissFlags.confusedFlag.get(__instance.source)) {
                //Set the target of this damage action to the previously stored target
                __instance.target = MissFlags.targetField.get(__instance.source);
                //Flash the power on the confused monster
                //__instance.source.getPower(ConfusedPower.POWER_ID).flash();
                //Increment data collector if we have it
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
        @SpireInsertPatch(locator = Locator.class, localvars = {"info"})
        public static void ChangeTarget(VampireDamageAction __instance, @ByRef DamageInfo[] info) {
            //Literally the same code as the first one. I should consolidate these into 2 patches, but I fear the different types might not work
            //If the source isn't null, and the source of the action failed the confusion check...
            if (__instance.source != null && MissFlags.confusedFlag.get(__instance.source)) {
                //Set the target of this damage action to the previously stored target
                __instance.target = MissFlags.targetField.get(__instance.source);
                //Flash the power on the confused monster
                //__instance.source.getPower(ConfusedPower.POWER_ID).flash();
                //Increment data collector if we have it
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
            //Here we prefix check for dizzy flags. This way confusion shouldn't proc if dizzy does.
            //If the cape proc triggered... (But not confusion!)
            if (__instance.source != null && MissFlags.capeFlag.get(__instance.source) && !MissFlags.confusedFlag.get(__instance.source)) {
                //Set this action as finished
                __instance.isDone = true;
                //Flash the relic above the player
                //((RepelCape) ((AbstractPlayer) __instance.target).getRelic(RepelCape.ID)).flashRelicAbovePlayer();
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
            //Likewise, if the source of the action is dizzy...
            if (__instance.source != null && MissFlags.dizzyFlag.get(__instance.source)) {
                //Set the action as done
                __instance.isDone = true;
                //Flash the dizzy power
                //__instance.source.getPower(DizzyPower.POWER_ID).flash();
                //Pass the damage back to the data collector
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
                //Return so nothing else happens
                return SpireReturn.Return(null);
            }
            //If we get here, then cape and dizzy both failed. Continue as normal.
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz = ApplyPowerAction.class,
            method = SpirePatch.CONSTRUCTOR,
            paramtypez = {
                    AbstractCreature.class,
                    AbstractCreature.class,
                    AbstractPower.class,
                    int.class,
                    boolean.class,
                    AbstractGameAction.AttackEffect.class
            })
    public static class ApplyPowerPatches {
        @SpirePrefixPatch
        public static SpireReturn<?> ApplyPowerReader(ApplyPowerAction __instance, @ByRef AbstractCreature[] target, @ByRef AbstractCreature[] source, @ByRef AbstractPower[] powerToApply, int stackAmount, boolean isFast, AbstractGameAction.AttackEffect effect) {
            //If the source gets caped while trying to apply to the player, but the attack isn't redirected
            if (source[0] != null && MissFlags.capeFlag.get(source[0]) && !MissFlags.confusedFlag.get(source[0]) && target[0] instanceof AbstractPlayer) {
                //Set the action as done and return immediately
                __instance.isDone = true;
                //Flash the relic above the player
                //((RepelCape) ((AbstractPlayer) __instance.target).getRelic(RepelCape.ID)).flashRelicAbovePlayer();
                SpireReturn.Return(null);
            }
            //If the source is dizzy and it isnt targeting itself, we end the action
            if (source[0] != null && MissFlags.dizzyFlag.get(source[0]) && target[0] != source[0]) {
                //Set the action as done and return immediately
                __instance.isDone = true;
                //Flash the dizzy power
                //__instance.source.getPower(DizzyPower.POWER_ID).flash();
                SpireReturn.Return(null);
            }
            //If the source is confused...
            if (source[0] != null && MissFlags.confusedFlag.get(source[0])) {
                //Set the new target and the new owner as the target we previously set
                target[0] = MissFlags.targetField.get(source[0]);
                powerToApply[0].owner = MissFlags.targetField.get(source[0]);
                //If the power we are applying is Hex, just turn it into Hexing
                if (powerToApply[0] instanceof HexPower) {
                    //If its Hex, we need to bounce back a Hexing. I decided to go with a 1 to 5 ratio
                    powerToApply[0] = new HexingPower(powerToApply[0].owner, source[0], powerToApply[0].amount * HexingPower.HEX_TO_HEXING_RATIO);
                }
                //Flash the confusion power
                //__instance.source.getPower(ConfusedPower.POWER_ID).flash();
            }
            //If the power is a debuff...
            if (powerToApply[0].type == AbstractPower.PowerType.DEBUFF) {
                //Grab the player for reference
                AbstractPlayer p = AbstractDungeon.player;
                //Sinister Reflection Logic works if a the target of a debuff is the player
                if (target[0] == p && p.hasPower(SinisterReflectionPower.POWER_ID)) {
                    //Grab the Sinister Reflection power for reference
                    AbstractPower pow = p.getPower(SinisterReflectionPower.POWER_ID);
                    //Check if the debuff is Cloneable. All basegame powers should be thanks to basemod. Modded powers are supposed to implement this as well.
                    if (powerToApply[0] instanceof CloneablePowerInterface) {
                        //Apply a copy to all monsters, taking care to set the new owner. Source isnt set for powers that need those but this is fine for now
                        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                            //Define a copy
                            AbstractPower copy;
                            if (powerToApply[0] instanceof HexPower) {
                                //If its Hex, we need to bounce back a Hexing. I decided to go with a 1 to 5 ratio
                                copy = new HexingPower(powerToApply[0].owner, p, powerToApply[0].amount * HexingPower.HEX_TO_HEXING_RATIO);
                            } else {
                                //Else we can just make a copy of the original power, no issue
                                copy = ((CloneablePowerInterface) powerToApply[0]).makeCopy();
                            }
                            //multiply the amount by our stacks of SR
                            copy.amount *= pow.amount;
                            //set the new owner
                            copy.owner = m;
                            //Set an action to apply the power
                            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, copy, copy.amount, true, effect));
                        }
                    }
                }
            } else {
                //If the power is a buff and we have Point Swap and if the source and target are not the player
                if (source[0] != AbstractDungeon.player && target[0] != AbstractDungeon.player && AbstractDungeon.player.hasRelic(PointSwap.ID) && ((PointSwap) AbstractDungeon.player.getRelic(PointSwap.ID)).relicIsActive()) {
                    //If the power isn't problematic
                    if (!(powerToApply[0] instanceof TheBombPower) &&
                            !(powerToApply[0] instanceof FadingPower) &&
                            !(powerToApply[0] instanceof BeatOfDeathPower) &&
                            !(powerToApply[0] instanceof SurroundedPower) &&
                            !(powerToApply[0] instanceof BackAttackPower) &&
                            !(powerToApply[0] instanceof MinionPower) &&
                            !(powerToApply[0] instanceof ModeShiftPower) &&
                            !(powerToApply[0] instanceof ReactivePower) &&
                            !(powerToApply[0] instanceof RegrowPower) &&
                            !(powerToApply[0] instanceof ResurrectPower) &&
                            !(powerToApply[0] instanceof StasisPower) &&
                            !(powerToApply[0] instanceof ThieveryPower) &&
                            !(powerToApply[0] instanceof TimeWarpPower) &&
                            !(powerToApply[0] instanceof UnawakenedPower)) {
                        //Set the target and owner to the player. Yoink.
                        target[0] = AbstractDungeon.player;
                        powerToApply[0].owner = AbstractDungeon.player;
                        //If it is ritual set it's flag
                        if (powerToApply[0] instanceof RitualPower) {
                            ReflectionHacks.setPrivate(powerToApply[0], RitualPower.class, "onPlayer", true);
                        }
                        //If it is sharp hide, turn it into thorns
                        if (powerToApply[0] instanceof SharpHidePower) {
                            powerToApply[0] = new ThornsPower(target[0], powerToApply[0].amount);
                        }
                        //If it is something else that has a problem, crash time I guess.
                        //Call the steal power function to increment relic stats
                        ((PointSwap) AbstractDungeon.player.getRelic(PointSwap.ID)).onStealPower();
                    } else if (!(powerToApply[0] instanceof RegrowPower) && !(powerToApply[0] instanceof MinionPower)) {
                        //We need to negate the power instead
                        //negate the power by setting the action as done and simply returning before anything happens
                        //TODO negating Regrow does nothing same dice with Minion, as such we just do nothing with these for now
                        ((PointSwap) AbstractDungeon.player.getRelic(PointSwap.ID)).onNegatePower();
                        __instance.isDone = true;
                        return SpireReturn.Return(null);
                    }
                }
            }
            return SpireReturn.Continue();
        }
    }
}
