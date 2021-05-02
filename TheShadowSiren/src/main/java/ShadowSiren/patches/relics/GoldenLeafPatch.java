package ShadowSiren.patches.relics;

import ShadowSiren.relics.GoldenLeaf;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rewards.RewardItem;
import javassist.CtBehavior;

public class GoldenLeafPatch {

    @SpirePatch(clz = RewardItem.class, method = "applyGoldBonus")
    public static class GoldenLeafBonusGold {
        @SpireInsertPatch(locator = Locator.class)
        public static void applyGoldBonus(RewardItem __instance, boolean theft) {
            if (AbstractDungeon.player.hasRelic(GoldenLeaf.ID)) {
                GoldenLeaf rel = (GoldenLeaf) AbstractDungeon.player.getRelic(GoldenLeaf.ID);
                if (rel.counter > 0) {
                    __instance.bonusGold += rel.counter;
                }
            }
        }
        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "hasRelic");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}
