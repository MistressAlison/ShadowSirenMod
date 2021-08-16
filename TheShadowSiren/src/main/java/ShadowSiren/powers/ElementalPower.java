package ShadowSiren.powers;

import IconsAddon.damageModifiers.AbstractDamageModifier;
import IconsAddon.powers.OnCreateDamageInfoPower;
import IconsAddon.util.DamageModifierManager;
import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cards.abstractCards.AbstractElementalCard;
import ShadowSiren.cards.abstractCards.AbstractInertCard;
import ShadowSiren.damageModifiers.AbstractVivianDamageModifier;
import ShadowSiren.damageModifiers.ElementallyInert;
import ShadowSiren.patches.ElementalPatches;
import ShadowSiren.powers.interfaces.OnChangeElementPower;
import ShadowSiren.util.ParticleOrbitRenderer;
import com.evacipated.cardcrawl.mod.stslib.patches.NeutralPowertypePatch;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.ArrayList;
import java.util.List;

public class ElementalPower extends AbstractPower implements InvisiblePower, OnCreateDamageInfoPower {
    public static final String POWER_ID = ShadowSirenMod.makeID("ElementalPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private final ArrayList<AbstractDamageModifier> mods = new ArrayList<>();

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
        if (elementsAreDifferent(card)) {
            mods.clear();
            for (AbstractDamageModifier mod : DamageModifierManager.modifiers(card)) {
                if (mod instanceof AbstractVivianDamageModifier && ((AbstractVivianDamageModifier) mod).isAnElement) {
                    if (!mods.contains(mod)) {
                        mods.add(mod);
                    }
                }
            }
            if (!mods.isEmpty()) {
                DamageModifierManager.removeAllModifiers(this);
                for (AbstractDamageModifier mod : mods) {
                    DamageModifierManager.addModifier(this, mod);
                }
                updateDescription();
            }
            for (AbstractPower pow : owner.powers) {
                if (pow instanceof OnChangeElementPower) {
                    ((OnChangeElementPower) pow).onChangeElement();
                }
            }
        }
    }

    private boolean elementsAreDifferent(AbstractCard c) {
        ArrayList<Class<?>> uniqueClasses = new ArrayList<>();
        for (AbstractDamageModifier mod : mods) {
            uniqueClasses.add(mod.getClass());
        }
        for (AbstractDamageModifier mod : DamageModifierManager.modifiers(c)) {
            if (mod instanceof AbstractVivianDamageModifier && ((AbstractVivianDamageModifier) mod).isAnElement) {
                if (!uniqueClasses.remove(mod.getClass())) {
                    return true;
                }
            }
        }
        return uniqueClasses.size() != 0;
    }

    @Override
    public void updateDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append(DESCRIPTIONS[0]);
        if (DamageModifierManager.modifiers(this).size() > 0) {
            sb.append(DESCRIPTIONS[1]);
        }
        for (AbstractDamageModifier mod : mods) {
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
            p.mods.clear();
            DamageModifierManager.removeAllModifiers(p);
            p.updateDescription();
        }
    }

    public static boolean hasAnElement() {
        return !DamageModifierManager.modifiers(getElementalPower()).isEmpty();
    }

    public static int numActiveElements() {
        return DamageModifierManager.modifiers(getElementalPower()).size();
    }

    public static List<AbstractDamageModifier> getActiveElements() {
        if (hasElementalPower()) {
            return DamageModifierManager.modifiers(getElementalPower());
        }
        return new ArrayList<>();
    }

    @Override
    public void onCreateDamageInfo(DamageInfo damageInfo, AbstractCard card) {
        if (hasAnElement() && card != null && !(card instanceof AbstractInertCard) && card.type == AbstractCard.CardType.ATTACK && (DamageModifierManager.getDamageMods(damageInfo).isEmpty() || DamageModifierManager.getDamageMods(damageInfo).stream().noneMatch(m -> m instanceof AbstractVivianDamageModifier && ((AbstractVivianDamageModifier) m).isAnElement))) {
            DamageModifierManager.bindDamageModsFromObject(damageInfo, this);
        }
        ParticleOrbitRenderer.increaseSpeed(ParticleOrbitRenderer.NORMAL_BOOST);
    }
}
