package ShadowSiren.powers;

import IconsAddon.damageModifiers.AbstractDamageModifier;
import IconsAddon.powers.DamageModApplyingPower;
import IconsAddon.util.DamageModContainer;
import IconsAddon.util.DamageModifierManager;
import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cards.abstractCards.AbstractInertCard;
import ShadowSiren.damageModifiers.AbstractVivianDamageModifier;
import ShadowSiren.powers.interfaces.OnChangeElementPower;
import ShadowSiren.util.ParticleOrbitRenderer;
import com.evacipated.cardcrawl.mod.stslib.patches.NeutralPowertypePatch;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
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

public class ElementalPower extends AbstractPower implements InvisiblePower, DamageModApplyingPower {
    public static final String POWER_ID = ShadowSirenMod.makeID("ElementalPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public DamageModContainer con = new DamageModContainer();

    public ElementalPower(AbstractCreature owner) {
        this.owner = owner;
        this.ID = POWER_ID;
        this.name = NAME;
        this.priority = Integer.MIN_VALUE;
        this.type = NeutralPowertypePatch.NEUTRAL;
        updateDescription();
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        if (DamageModifierManager.modifiers(card).stream().anyMatch(d -> d instanceof AbstractVivianDamageModifier && ((AbstractVivianDamageModifier) d).isAnElement)) {
            grabElementsOffCard(card);
        }
    }

    public void grabElementsOffCard(AbstractCard card) {
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
            }
            updateDescription();
            for (AbstractPower pow : owner.powers) {
                if (pow instanceof OnChangeElementPower) {
                    ((OnChangeElementPower) pow).onChangeElement();
                }
            }
        }
    }

    @Override
    public void updateDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append(DESCRIPTIONS[0]);
        if (!con.modifiers().isEmpty()) {
            sb.append(DESCRIPTIONS[1]);
        }
        for (AbstractDamageModifier mod : con.modifiers()) {
            if (mod instanceof AbstractVivianDamageModifier) {
                sb.append(" NL ").append(((AbstractVivianDamageModifier) mod).cardStrings.DESCRIPTION);
            }
        }
        this.description = sb.toString();
    }

    public static boolean hasElementalPower() {
        return AbstractDungeon.player.hasPower(ElementalPower.POWER_ID);
    }

    public static ElementalPower getElementalPower() {
        return (ElementalPower) AbstractDungeon.player.getPower(ElementalPower.POWER_ID);
    }

    public static void removeAllElements() {
        if (hasElementalPower()) {
            ElementalPower p = getElementalPower();
            p.con.clearModifiers();
            p.updateDescription();
        }
    }

    public static boolean hasAnElement() {
        return !getElementalPower().con.modifiers().isEmpty();
    }

    public static int numActiveElements() {
        return getElementalPower().con.modifiers().size();
    }

    public static List<AbstractDamageModifier> getActiveElements() {
        if (hasElementalPower()) {
            return getElementalPower().con.modifiers();
        }
        return new ArrayList<>();
    }

    @Override
    public void onAddedDamageModsToDamageInfo(DamageInfo damageInfo, AbstractCard card) {
        this.addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                removeAllElements();
                this.isDone = true;
            }
        });
        ParticleOrbitRenderer.increaseSpeed(ParticleOrbitRenderer.NORMAL_BOOST);
    }

    @Override
    public boolean shouldPushMods(DamageInfo damageInfo, AbstractCard card, List<AbstractDamageModifier> list) {
        return hasAnElement() && card != null && !(card instanceof AbstractInertCard) && card.type == AbstractCard.CardType.ATTACK && list.stream().noneMatch(m -> m instanceof AbstractVivianDamageModifier && ((AbstractVivianDamageModifier) m).isAnElement);
    }

    @Override
    public ArrayList<AbstractDamageModifier> modsToPush(DamageInfo damageInfo, AbstractCard card, List<AbstractDamageModifier> list) {
        return new ArrayList<>(con.modifiers());
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
