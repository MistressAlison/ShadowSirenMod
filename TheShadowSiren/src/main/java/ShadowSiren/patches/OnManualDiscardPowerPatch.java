package ShadowSiren.patches;

import ShadowSiren.powers.interfaces.onDiscardCardPower;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class OnManualDiscardPowerPatch {
    @SpirePatch(clz = GameActionManager.class, method = "incrementDiscard")
    public static class DiscardListener {
        @SpirePostfixPatch
        public static void discardListener(boolean endOfTurn) {
            //Loop through our powers to see if any are onDiscardCardPower
            for (AbstractPower pow : AbstractDungeon.player.powers) {
                if (pow instanceof onDiscardCardPower) {
                    //Call onDiscardCard with the important variables: Whether or not the discard took place on the player turn, and if it was due end turn discard
                    ((onDiscardCardPower) pow).onDiscardCard(!AbstractDungeon.actionManager.turnHasEnded, endOfTurn);
                }
            }
        }
        /*private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(Iterator.class, "hasNext");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }*/
    }
}
