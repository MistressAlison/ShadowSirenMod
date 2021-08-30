package ShadowSiren.cardModifiers;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cards.interfaces.ChargeMultiEffect;
import ShadowSiren.powers.interfaces.OnApplyChargePower;
import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.colorless.RitualDagger;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class ChargeModifier extends AbstractCardModifier {
    public static final String ID = ShadowSirenMod.makeID("ChargeModifier");
    public static final String[] TEXT = CardCrawlGame.languagePack.getCardStrings(ID).EXTENDED_DESCRIPTION;
    int amount;
    boolean inherent;


    public ChargeModifier(int amount) {
        /*float tmp = amount;
        for (AbstractPower p : AbstractDungeon.player.powers) {
            if (p instanceof OnApplyChargePower) {
                tmp = ((OnApplyChargePower) p).onApplyCharge(tmp);
                p.flash();
            }
        }
        this.amount = (int)tmp;*/
        this.amount = amount;
    }

    public ChargeModifier(int amount, boolean inherent) {
        this(amount);
        this.inherent = inherent;
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        //CardCrawlGame.sound.play("ORB_LIGHTNING_CHANNEL", 0.1F);
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return TEXT[0]+amount+TEXT[1]+rawDescription;
    }

    @Override
    public boolean shouldApply(AbstractCard card) {
        if (CardModifierManager.hasModifier(card, identifier(card))) {
            ChargeModifier mod = (ChargeModifier) CardModifierManager.getModifiers(card, identifier(card)).get(0);
            mod.amount += amount;
            card.initializeDescription();
            CardCrawlGame.sound.play("ORB_LIGHTNING_CHANNEL", 0.1F);
            return false;
        }
        return amount > 0;
    }

    public void applyEffect(AbstractCard card) {
        float tmp = amount;
        tmp *= card instanceof ChargeMultiEffect ? ((ChargeMultiEffect) card).getChargeMultiplier() : 1f;
        if (card.baseDamage >= 0) {
            card.baseDamage += (int)tmp;
        }
        if (card.baseBlock >= 0 && !(card instanceof RitualDagger)) {
            card.baseBlock += (int)tmp;
        }
        card.applyPowers();
       // card.superFlash();
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }

    @Override
    public boolean isInherent(AbstractCard card) {
        return inherent;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new ChargeModifier(amount, inherent);
    }

    public static void triggerCharge() {
        for(AbstractCard c : AbstractDungeon.player.discardPile.group) {
            if (CardModifierManager.hasModifier(c, ChargeModifier.ID)) {
                ((ChargeModifier)(CardModifierManager.getModifiers(c, ChargeModifier.ID).get(0))).applyEffect(c);
            }
        }

        for (AbstractCard c: AbstractDungeon.player.drawPile.group) {
            if (CardModifierManager.hasModifier(c, ChargeModifier.ID)) {
                ((ChargeModifier)(CardModifierManager.getModifiers(c, ChargeModifier.ID).get(0))).applyEffect(c);
            }
        }

        for (AbstractCard c: AbstractDungeon.player.hand.group) {
            if (CardModifierManager.hasModifier(c, ChargeModifier.ID)) {
                ((ChargeModifier)(CardModifierManager.getModifiers(c, ChargeModifier.ID).get(0))).applyEffect(c);
                c.superFlash();
            }
        }
    }
}
