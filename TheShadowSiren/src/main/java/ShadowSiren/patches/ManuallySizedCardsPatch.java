package ShadowSiren.patches;

import ShadowSiren.cards.interfaces.ManuallySizeAdjustedCard;
import basemod.patches.com.megacrit.cardcrawl.cards.AbstractCard.ShrinkLongDescription;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.FontHelper;

public class ManuallySizedCardsPatch {
    @SpirePatch(clz = ShrinkLongDescription.ShrinkInitializeDescription.class, method = "Prefix")
    public static class SetTextSizeAndFontScale {
        public static void Postfix(AbstractCard __instance, int ___depth) {
            //Attacks already cause us to lose Vigor, so we need to remove it only if our card implements the Vigor buff and is not an attack
            if (___depth == 0 && __instance instanceof ManuallySizeAdjustedCard) {
                ShrinkLongDescription.Scale.descriptionScale.set(__instance, ((ManuallySizeAdjustedCard) __instance).getAdjustedScale());
                FontHelper.cardDescFont_N.getData().setScale(((ManuallySizeAdjustedCard) __instance).getAdjustedScale());
            }
        }
    }

    @SpirePatch(clz = ShrinkLongDescription.ShrinkInitializeDescription.class, method = "Postfix")
    public static class RenderAndFixFontScale {
        public static void Postfix(AbstractCard __instance, int ___depth) {
            //Attacks already cause us to lose Vigor, so we need to remove it only if our card implements the Vigor buff and is not an attack
            if (___depth == 0 && __instance instanceof ManuallySizeAdjustedCard) {
                //ShrinkLongDescription.Scale.descriptionScale.set(__instance, 1f);
                FontHelper.cardDescFont_N.getData().setScale(1f);
            }
        }
    }
}
