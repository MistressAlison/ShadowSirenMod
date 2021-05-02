package ShadowSiren.patches;

import ShadowSiren.cards.interfaces.ChargeMagicBuff;
import ShadowSiren.cards.interfaces.VigorMagicBuff;
import ShadowSiren.powers.ChargePower;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;

public class MagicBuffPatches {

    @SpirePatch(clz = AbstractCard.class, method = "applyPowers")
    public static class ApplyPowersPatch {
        public static void Postfix(AbstractCard __instance) {
            //There are 2 different interfaces we can implement to get a bonus to our magic number
            int bonus = 0;
            //Vigor check
            if (__instance instanceof VigorMagicBuff && AbstractDungeon.player.hasPower(VigorPower.POWER_ID)) {
                bonus += AbstractDungeon.player.getPower(VigorPower.POWER_ID).amount;;
            }
            //Charge check
            if (__instance instanceof ChargeMagicBuff && AbstractDungeon.player.hasPower(ChargePower.POWER_ID)) {
                bonus += AbstractDungeon.player.getPower(ChargePower.POWER_ID).amount;;
            }
            //If our card implements either of these, we add any bonus and update the magic number accordingly
            if (__instance instanceof VigorMagicBuff || __instance instanceof ChargeMagicBuff) {
                __instance.magicNumber = __instance.baseMagicNumber + bonus;
                __instance.isMagicNumberModified = __instance.magicNumber != __instance.baseMagicNumber;
                __instance.initializeDescription();
            }
        }
    }

    @SpirePatch(clz = VigorPower.class, method = "onUseCard")
    public static class OnUseCardPatch {
        public static void Postfix(VigorPower __instance, AbstractCard card, UseCardAction action) {
            //Attacks already cause us to lose Vigor, so we need to remove it only if our card implements the Vigor buff and is not an attack
            if (card.type != AbstractCard.CardType.ATTACK && card instanceof VigorMagicBuff) {
                __instance.flash();
                AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(__instance.owner, __instance.owner, VigorPower.POWER_ID));
            }
        }
    }
}
