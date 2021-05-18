package ShadowSiren.patches;
/*
import ShadowSiren.powers.HexingPower;
import ShadowSiren.powers.SinisterReflectionPower;
import ShadowSiren.relics.PointSwap;
import basemod.ReflectionHacks;
import basemod.interfaces.CloneablePowerInterface;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;

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
public class ApplyPowerPatches
{
    @SpirePrefixPatch
    public static SpireReturn<?> ApplyPowerReader(ApplyPowerAction __instance, @ByRef AbstractCreature[] target, @ByRef AbstractCreature[] source, @ByRef AbstractPower[] powerToApply, int stackAmount, boolean isFast, AbstractGameAction.AttackEffect effect)
    {
        //If the power is a debuff...
        if(powerToApply[0].type == AbstractPower.PowerType.DEBUFF) {
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
                            copy = new HexingPower(powerToApply[0].owner, p, powerToApply[0].amount*5);
                        } else {
                            //Else we can just make a copy of the original power, no issue
                            copy = ((CloneablePowerInterface)powerToApply[0]).makeCopy();
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
                        !(powerToApply[0] instanceof UnawakenedPower)){
                    target[0] = AbstractDungeon.player;
                    powerToApply[0].owner = AbstractDungeon.player;
                    if (powerToApply[0] instanceof RitualPower) {
                        ReflectionHacks.setPrivate(powerToApply[0], RitualPower.class, "onPlayer", true);
                    }
                    if (powerToApply[0] instanceof SharpHidePower) {
                        powerToApply[0] = new ThornsPower(target[0], powerToApply[0].amount);
                    }
                    ((PointSwap) AbstractDungeon.player.getRelic(PointSwap.ID)).onStealPower();
                } else if (!(powerToApply[0] instanceof RegrowPower) && !(powerToApply[0] instanceof MinionPower)){
                    //We need to negate the power instead
                    //negate the power by setting the action as done and simply returning before anything happens
                    //TODO negating Regrow does nothing same dice with Minion
                    ((PointSwap) AbstractDungeon.player.getRelic(PointSwap.ID)).onNegatePower();
                    __instance.isDone = true;
                    return SpireReturn.Return(null);
                }
            }
        }
        return SpireReturn.Continue();
    }
}*/