package ShadowSiren.util;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cards.abstractCards.AbstractInertCard;
import ShadowSiren.characters.Vivian;
import ShadowSiren.damageModifiers.AbstractVivianDamageModifier;
import ShadowSiren.powers.interfaces.OnChangeElementPower;
import basemod.helpers.TooltipInfo;
import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier;
import com.evacipated.cardcrawl.mod.stslib.damagemods.DamageModContainer;
import com.evacipated.cardcrawl.mod.stslib.damagemods.DamageModifierManager;
import com.evacipated.cardcrawl.mod.stslib.patches.BindingPatches;
import com.evacipated.cardcrawl.mod.stslib.patches.DescriptorAndTooltipPatches;
import com.evacipated.cardcrawl.mod.stslib.patches.RenderElementsOnCardPatches;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import javassist.CannotCompileException;
import javassist.CtBehavior;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ElementManager {
    public static final String POWER_ID = ShadowSirenMod.makeID("ElementalPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final DamageModContainer con = new DamageModContainer(null);
    private static String description = "";

    public static void onPlayCard(AbstractCard card, AbstractMonster m) {
        if (DamageModifierManager.modifiers(card).stream().anyMatch(d -> d instanceof AbstractVivianDamageModifier && ((AbstractVivianDamageModifier) d).isAnElement)) {
            grabElementsOffCard(card);
        }
    }

    public static void grabElementsOffCard(AbstractCard card) {
        HashMap<Class<?>, AbstractVivianDamageModifier> newMods = new HashMap<>();
        for (AbstractDamageModifier mod : DamageModifierManager.modifiers(card)) {
            if (mod instanceof AbstractVivianDamageModifier && ((AbstractVivianDamageModifier) mod).isAnElement) {
                newMods.put(mod.getClass(), (AbstractVivianDamageModifier) mod.makeCopy());
            }
        }
        for (AbstractDamageModifier mod : con.modifiers()) {
            newMods.remove(mod.getClass());
        }
        if (newMods.size() > 0) {
            for (Class<?> modClass : newMods.keySet()) {
                AbstractVivianDamageModifier copy = (AbstractVivianDamageModifier) newMods.get(modClass).makeCopy();
                copy.tipType = AbstractVivianDamageModifier.TipType.DAMAGE;
                copy.putIconOnCard = true;
                con.addModifier(copy);
                for (AbstractPower pow : AbstractDungeon.player.powers) {
                    if (pow instanceof OnChangeElementPower) {
                        ((OnChangeElementPower) pow).onChangeElement();
                    }
                }
            }
            updateDescription();
        }
    }

    public static void updateDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append(DESCRIPTIONS[0]);
        if (!con.modifiers().isEmpty()) {
            sb.append(DESCRIPTIONS[1]);
        }
        for (AbstractDamageModifier mod : con.modifiers()) {
            if (mod instanceof AbstractVivianDamageModifier) {
                sb.append(" NL ").append(((AbstractVivianDamageModifier) mod).cardStrings.EXTENDED_DESCRIPTION[0]);
            }
        }
        description = sb.toString();
    }

    public static void removeAllElements() {
        con.clearModifiers();
        updateDescription();
    }

    public static boolean hasAnElement() {
        return !con.modifiers().isEmpty();
    }

    public static int numActiveElements() {
        return con.modifiers().size();
    }

    public static List<AbstractDamageModifier> getActiveElements() {
        return con.modifiers();
    }

    public static void onAddedDamageModsToDamageInfo(DamageInfo damageInfo, Object o) {
        AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
            @Override
            public void update() {
                removeAllElements();
                this.isDone = true;
            }
        });
        ParticleOrbitRenderer.increaseSpeed(ParticleOrbitRenderer.NORMAL_BOOST);
    }

    public static boolean shouldPushMods(DamageInfo damageInfo, Object o, List<AbstractDamageModifier> list) {
        return hasAnElement() &&
                o instanceof AbstractCard &&
                !(o instanceof AbstractInertCard) &&
                ((AbstractCard) o).type == AbstractCard.CardType.ATTACK &&
                list.stream().noneMatch(m -> m instanceof AbstractVivianDamageModifier && ((AbstractVivianDamageModifier) m).isAnElement);
    }

    public static List<AbstractDamageModifier> modsToPush(DamageInfo damageInfo, Object u, List<AbstractDamageModifier> list) {
        return new ArrayList<>(con.modifiers());
    }

    @SpirePatch2(clz = GameActionManager.class, method = "getNextAction")
    public static class PlayCardHook {
        @SpireInsertPatch(locator = Locator.class)
        public static void plz(GameActionManager __instance) {
            onPlayCard(__instance.cardQueue.get(0).card, __instance.cardQueue.get(0).monster);
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher m = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "powers");
                return LineFinder.findAllInOrder(ctBehavior, m);
            }
        }
    }

    @SpirePatch2(clz = AbstractPlayer.class, method = "renderPowerTips")
    public static class RenderElementalTooltip {
        @SpireInsertPatch(locator = Locator.class, localvars = {"tips"})
        public static void patch(AbstractPlayer __instance, ArrayList<PowerTip> tips) {
            if (__instance instanceof Vivian || ElementManager.hasAnElement()) {
                tips.add(new PowerTip(ElementManager.NAME, ElementManager.description));
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "stance");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch2(clz = BindingPatches.AddTempModifiers.class, method = "addMods")
    public static class TempDamageModPatch {
        @SpireInsertPatch(locator = Locator.class, localvars = {"pushedMods"})
        public static void plz(Object[] __args, ArrayList<AbstractDamageModifier> pushedMods) {
            if (shouldPushMods(null, __args[0], pushedMods)) {
                pushedMods.addAll(modsToPush(null, __args[0], pushedMods));
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher m = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "powers");
                return LineFinder.findAllInOrder(ctBehavior, m);
            }
        }
    }

    @SpirePatch2(clz = BindingPatches.BindObjectToDamageInfo.class, method = "PostfixMeToPiggybackBinding")
    public static class ApplyDamageModPatch {
        @SpireInsertPatch(locator = Locator.class, localvars = {"instigator"})
        public static void plz(Object[] __args, Object instigator, ArrayList<AbstractDamageModifier> ___boundMods) {
            if (shouldPushMods((DamageInfo) __args[0], instigator, ___boundMods)) {
                ___boundMods.addAll(modsToPush((DamageInfo) __args[0], instigator, ___boundMods));
                onAddedDamageModsToDamageInfo((DamageInfo) __args[0], instigator);
            }
        }
        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher m = new Matcher.FieldAccessMatcher(AbstractCreature.class, "powers");
                return LineFinder.findAllInOrder(ctBehavior, m);
            }
        }
    }

    @SpirePatch2(clz = RenderElementsOnCardPatches.RenderOnCardPatch.class, method = "renderHelper")
    public static class AddIconsPatch {
        @SpireInsertPatch(locator = Locator.class, localvars = {"mods"})
        public static void plz(AbstractCard card, ArrayList<AbstractDamageModifier> mods) {
            if (shouldPushMods(null, card, DamageModifierManager.modifiers(card))) {
                for (AbstractDamageModifier mod : modsToPush(null, card, DamageModifierManager.modifiers(card))) {
                    if (mod.shouldPushIconToCard(card) && mod.getAccompanyingIcon() != null) {
                        mods.add(mod);
                    }
                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher m = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "powers");
                return LineFinder.findAllInOrder(ctBehavior, m);
            }
        }
    }

    @SpirePatch2(clz = DescriptorAndTooltipPatches.AddTooltipTop.class, method = "part1")
    @SpirePatch2(clz = DescriptorAndTooltipPatches.AddTooltipTop.class, method = "part2")
    public static class AddTooltips {
        @SpireInsertPatch(locator = Locator.class)
        public static void plz(AbstractCard ___card, List<TooltipInfo>[] tooltips) {
            if (shouldPushMods(null, ___card, DamageModifierManager.modifiers(___card))) {
                for (AbstractDamageModifier mod : modsToPush(null, ___card, DamageModifierManager.modifiers(___card))) {
                    if (mod.getCustomTooltips() != null) {
                        tooltips[0].addAll(mod.getCustomTooltips());
                    }
                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher m = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "powers");
                return LineFinder.findAllInOrder(ctBehavior, m);
            }
        }
    }

    @SpirePatch2(clz = GameActionManager.class, method = "clear")
    public static class ResetCounters {
        @SpirePrefixPatch
        public static void reset() {
            con.clearModifiers();
            updateDescription();
        }
    }
}
