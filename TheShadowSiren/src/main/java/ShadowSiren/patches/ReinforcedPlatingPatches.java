package ShadowSiren.patches;

import ShadowSiren.powers.ReinforcedPlatingPower;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.AbstractCreature;

public class ReinforcedPlatingPatches {

    @SpirePatch(clz = AbstractCreature.class, method = "loseBlock", paramtypez = {int.class, boolean.class})
    public static class LoseBlockPatch {
        @SpirePrefixPatch
        public static void Prefix(AbstractCreature __instance, int amount, boolean noAnimation) {
            //If we have the power that prevents us from losing Block...
            if (__instance.hasPower(ReinforcedPlatingPower.POWER_ID) && amount > 0) {
                //Grab the power
                ReinforcedPlatingPower pow = (ReinforcedPlatingPower) __instance.getPower(ReinforcedPlatingPower.POWER_ID);
                //If our power actually can be used...
                if (pow.hitsBlockedThisTurn < pow.amount) {
                    //Flash it
                    pow.flash();
                    //Increment the uses
                    pow.hitsBlockedThisTurn++;
                    //Tack the amount we were going to lose onto our current block
                    __instance.currentBlock += amount;
                    //Now we will be left with whatever our original amount was once the rest of the function reduces our Block by said amount.
                    //We still need to patch brokeBlock since it is possible for it to get called when we still have block remaining due to this.
                }
            }
        }
    }

    @SpirePatch(clz = AbstractCreature.class, method = "brokeBlock")
    public static class BrokeBlockPatch {
        @SpirePrefixPatch
        public static SpireReturn<?> Prefix(AbstractCreature __instance) {
            //Since brokeBlock can get called when we still have block, simply perform a check and return immediately if that's the case.
            if (__instance.currentBlock > 0) {
                return SpireReturn.Return(null);
            }
            //Else continue as normal
            return SpireReturn.Continue();
        }
    }
}
