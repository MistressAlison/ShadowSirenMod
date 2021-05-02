package ShadowSiren.patches.relics;

import ShadowSiren.relics.StopWatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class StopWatchPatch {
    @SpirePatch(clz = AbstractMonster.class, method = "createIntent")
    public static class CreateIntentPatch {
        @SpirePostfixPatch
        public static SpireReturn<?> StopWatchStun(AbstractMonster __instance) {
            if (AbstractDungeon.player.hasRelic(StopWatch.ID)) {
                StopWatch watch = (StopWatch) AbstractDungeon.player.getRelic(StopWatch.ID);
                if (!watch.activated) {
                    AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
                        @Override
                        public void update() {
                            watch.activate(__instance);
                            this.isDone = true;
                        }
                    });
                }
            }
            return SpireReturn.Continue();
        }
    }
}
