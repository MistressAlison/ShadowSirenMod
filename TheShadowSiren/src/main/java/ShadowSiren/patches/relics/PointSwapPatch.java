package ShadowSiren.patches.relics;

import ShadowSiren.relics.PointSwap;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class PointSwapPatch {
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
        public static void ApplyPowerReader(ApplyPowerAction __instance, @ByRef AbstractCreature[] target, @ByRef AbstractCreature[] source, @ByRef AbstractPower[] powerToApply, int stackAmount, boolean isFast, AbstractGameAction.AttackEffect effect) {
            //If the power is a buff and we have Point Swap and if the source and target are not the player
            if (powerToApply[0].type == AbstractPower.PowerType.BUFF && source[0] != AbstractDungeon.player && /*target[0] != AbstractDungeon.player &&*/ AbstractDungeon.player.hasRelic(PointSwap.ID) && ((PointSwap) AbstractDungeon.player.getRelic(PointSwap.ID)).relicIsActive()) {
                ((PointSwap) AbstractDungeon.player.getRelic(PointSwap.ID)).onEnemyBuff();
            }
        }
    }
}
