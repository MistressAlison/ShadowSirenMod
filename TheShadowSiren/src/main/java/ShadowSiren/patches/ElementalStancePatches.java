package ShadowSiren.patches;

import IconsAddon.damageModifiers.AbstractDamageModifier;
import IconsAddon.util.DamageModifierManager;
import ShadowSiren.cards.abstractCards.AbstractItemCard;
import ShadowSiren.cards.abstractCards.AbstractModdedCard;
import ShadowSiren.damageModifiers.*;
import ShadowSiren.stances.ElementalStance;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import javassist.CtBehavior;

import java.util.ArrayList;

public class ElementalStancePatches {
    public static boolean isNonElemental(AbstractCard card) {
        return card instanceof AbstractModdedCard && noElementalModifiers(card);
    }
    public static boolean noElementalModifiers(Object o) {
        return DamageModifierManager.modifiers(o).stream().noneMatch(m -> m instanceof AbstractVivianDamageModifier && ((AbstractVivianDamageModifier) m).isAnElement);
    }
    public static boolean shouldPushElements(AbstractCard card) {
        return isNonElemental(card) && AbstractDungeon.player.stance.ID.equals(ElementalStance.STANCE_ID) && card.type == AbstractCard.CardType.ATTACK;
    }
    @SpirePatch(clz = DamageInfo.class, method = "<ctor>", paramtypez = {AbstractCreature.class, int.class, DamageInfo.DamageType.class})
    public static class BindObjectToDamageInfoIfNotBoundYet {
        @SpirePostfixPatch()
        public static void ButOnlyIfYouHaveTheStanceInQuestion(DamageInfo __instance, AbstractCreature damageSource, int base, DamageInfo.DamageType type) {
            Object obj = DamageModifierManager.getBoundObject(__instance);
            if (obj == null || noElementalModifiers(obj)) {
                if (damageSource == AbstractDungeon.player && type == DamageInfo.DamageType.NORMAL && AbstractDungeon.player.stance.ID.equals(ElementalStance.STANCE_ID)) {
                    Object o = new Object();
                    DamageModifierManager.addModifiers(o, DamageModifierManager.modifiers(AbstractDungeon.player.stance));
                    DamageModifierManager.spliceBoundObject(__instance, o);
                }
            }
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "useCard")
    public static class RememberCardPreUseCall {
        @SpireInsertPatch(locator = Locator.class)
        public static void removePowerListener(AbstractPlayer __instance, AbstractCard c, AbstractMonster monster, int energyOnUse) {
            if (c instanceof AbstractModdedCard && !(c instanceof AbstractItemCard) && DamageModifierManager.modifiers(c).size() > 0 && !__instance.stance.ID.equals(ElementalStance.STANCE_ID)) {
                AbstractDungeon.actionManager.addToTop(new ChangeStanceAction(new ElementalStance(c)));
            }
        }
        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractCard.class, "use");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(clz = AbstractCard.class, method = "calculateCardDamage")
    public static class AddTempModifiers {

        private static final ArrayList<AbstractDamageModifier> pushedMods = new ArrayList<>();

        @SpirePrefixPatch()
        public static void addMods(AbstractCard __instance, AbstractMonster mo) {
            if (shouldPushElements(__instance)) {
                DamageModifierManager.addModifiers(__instance, DamageModifierManager.modifiers(AbstractDungeon.player.stance));
                pushedMods.addAll(DamageModifierManager.modifiers(AbstractDungeon.player.stance));
            }
        }

        @SpirePostfixPatch()
        public static void removeMods(AbstractCard __instance, AbstractMonster mo) {
            DamageModifierManager.removeModifiers(__instance, pushedMods);
            pushedMods.clear();
        }
    }

    @SpirePatch(clz = AbstractCard.class, method = "renderEnergy")
    public static class RenderOnCardPatch
    {
        @SpirePostfixPatch
        public static void RenderOnCard(AbstractCard __instance, SpriteBatch sb) {
            if (AbstractDungeon.player != null && validLocation(__instance)) {
                if (shouldPushElements(__instance)) {
                    renderHelper(sb, __instance.current_x, __instance.current_y, __instance);
                }
            }
        }

        //Don't bother rendering if it isn't in one of 4 immediately viewable locations. We also don't want to render in master deck
        private static boolean validLocation(AbstractCard c) {
            return AbstractDungeon.player.hand.contains(c) ||
                    AbstractDungeon.player.drawPile.contains(c) ||
                    AbstractDungeon.player.discardPile.contains(c) ||
                    AbstractDungeon.player.exhaustPile.contains(c);
        }


        private static void renderHelper(SpriteBatch sb, float drawX, float drawY, AbstractCard C) {
            sb.setColor(Color.WHITE.cpy());
            ArrayList<AbstractVivianDamageModifier> mods = new ArrayList<>();
            for (AbstractDamageModifier mod : DamageModifierManager.modifiers(AbstractDungeon.player.stance)) {
                if (mod instanceof AbstractVivianDamageModifier && ((AbstractVivianDamageModifier) mod).isAnElement) {
                    mods.add((AbstractVivianDamageModifier) mod);
                }
            }
            float dx = -(mods.size()-1)*16f*Settings.scale;
            float dy = 210f;
            for (AbstractVivianDamageModifier mod : mods) {
                AtlasRegion img = mod.getAccompanyingIcon().getAtlasTexture();
                sb.draw(img, drawX + dx + img.offsetX - (float) img.originalWidth / 2.0F, drawY + dy + img.offsetY - (float) img.originalHeight / 2.0F,
                        (float) img.originalWidth / 2.0F - img.offsetX - dx, (float) img.originalHeight / 2.0F - img.offsetY - dy,
                        (float) img.packedWidth, (float) img.packedHeight,
                        C.drawScale * Settings.scale, C.drawScale * Settings.scale, C.angle);
                dx += 32f*Settings.scale;
            }
        }
    }
}
