package ShadowSiren.patches;

import IconsAddon.damageModifiers.AbstractDamageModifier;
import IconsAddon.util.DamageModifierManager;
import ShadowSiren.cards.abstractCards.AbstractInertCard;
import ShadowSiren.cards.abstractCards.AbstractModdedCard;
import ShadowSiren.cards.interfaces.ElementallyInert;
import ShadowSiren.damageModifiers.*;
import ShadowSiren.powers.ElementalPower;
import ShadowSiren.util.ParticleOrbitRenderer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import javassist.CannotCompileException;
import javassist.CtBehavior;

import java.util.ArrayList;

public class ElementalPatches {
    public static boolean isNonElementalVivianCard(AbstractCard card) { //unused
        return card instanceof AbstractModdedCard && noElementalModifiers(card);
    }
    public static boolean noElementalModifiers(Object o) {
        return DamageModifierManager.modifiers(o).stream().noneMatch(m -> m instanceof AbstractVivianDamageModifier && ((AbstractVivianDamageModifier) m).isAnElement) && !(o instanceof ElementallyInert);
    }
    public static boolean shouldPushElements(AbstractCard card) {
        return noElementalModifiers(card) && AbstractDungeon.player.hasPower(ElementalPower.POWER_ID) && card.type == AbstractCard.CardType.ATTACK && !(card instanceof AbstractInertCard);
    }

    /*@SpirePatch(clz = DamageInfo.class, method = "<ctor>", paramtypez = {AbstractCreature.class, int.class, DamageInfo.DamageType.class})
    public static class BindObjectToDamageInfoIfNotBoundYet {
        @SpirePostfixPatch()
        public static void ButOnlyIfYouHaveThePowerInQuestion(DamageInfo __instance, AbstractCreature damageSource, int base, DamageInfo.DamageType type) {
            if (damageSource == AbstractDungeon.player && type == DamageInfo.DamageType.NORMAL && AbstractDungeon.player.hasPower(ElementalPower.POWER_ID)) {
                AbstractDungeon.actionManager.addToTop(new AbstractGameAction() {
                    @Override
                    public void update() {
                        delayedSpliceCheck(__instance);
                        ParticleOrbitRenderer.increaseSpeed(ParticleOrbitRenderer.NORMAL_BOOST);
                        this.isDone = true;
                    }
                });
            }
        }
    }

    public static void delayedSpliceCheck(DamageInfo di) {
        Object obj = DamageModifierManager.getBoundObject(di);
        if (obj == null || noElementalModifiers(obj)) {
            Object o = new Object();
            DamageModifierManager.addModifiers(o, DamageModifierManager.modifiers(AbstractDungeon.player.getPower(ElementalPower.POWER_ID)));
            DamageModifierManager.spliceBoundObject(di, o);
        }
    }*/

    @SpirePatch(clz = AbstractCard.class, method = "calculateCardDamage")
    public static class AddTempModifiers {

        private static final ArrayList<AbstractDamageModifier> pushedMods = new ArrayList<>();

        @SpirePrefixPatch()
        public static void addMods(AbstractCard __instance, AbstractMonster mo) {
            if (shouldPushElements(__instance)) {
                DamageModifierManager.addModifiers(__instance, DamageModifierManager.modifiers(AbstractDungeon.player.getPower(ElementalPower.POWER_ID)));
                pushedMods.addAll(DamageModifierManager.modifiers(AbstractDungeon.player.getPower(ElementalPower.POWER_ID)));
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
            for (AbstractDamageModifier mod : DamageModifierManager.modifiers(AbstractDungeon.player.getPower(ElementalPower.POWER_ID))) {
                if (mod instanceof AbstractVivianDamageModifier && ((AbstractVivianDamageModifier) mod).isAnElement) {
                    mods.add((AbstractVivianDamageModifier) mod);
                }
            }
            float dx = -(mods.size()-1)*16f;
            float dy = 210f;
            for (AbstractVivianDamageModifier mod : mods) {
                AtlasRegion img = mod.getAccompanyingIcon().getAtlasTexture();
                sb.draw(img, drawX + dx + img.offsetX - (float) img.originalWidth / 2.0F, drawY + dy + img.offsetY - (float) img.originalHeight / 2.0F,
                        (float) img.originalWidth / 2.0F - img.offsetX - dx, (float) img.originalHeight / 2.0F - img.offsetY - dy,
                        (float) img.packedWidth, (float) img.packedHeight,
                        C.drawScale * Settings.scale, C.drawScale * Settings.scale, C.angle);
                dx += 32f;
            }
        }
    }

    @SpirePatch2(clz = AbstractPlayer.class, method = "renderPowerTips")
    public static class RenderElementalTooltip {
        @SpireInsertPatch(locator = Locator.class, localvars = {"tips"})
        public static void patch(AbstractPlayer __instance, ArrayList<PowerTip> tips) {
            if (__instance.hasPower(ElementalPower.POWER_ID)) {
                tips.add(new PowerTip(__instance.getPower(ElementalPower.POWER_ID).name, __instance.getPower(ElementalPower.POWER_ID).description));
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "stance");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}
