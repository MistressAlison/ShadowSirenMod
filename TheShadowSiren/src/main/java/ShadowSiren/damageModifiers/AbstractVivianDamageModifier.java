package ShadowSiren.damageModifiers;

import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;

public abstract class AbstractVivianDamageModifier extends AbstractDamageModifier {
    public enum TipType {
        TYPE_AND_DAMAGE,
        DAMAGE,
        NONE
    }

    public final CardStrings cardStrings;
    public boolean isAnElement = true;
    public boolean putIconOnCard = false;
    public TipType tipType;
    public AbstractVivianDamageModifier(String ID, TipType tipType) {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        this.tipType = tipType;
    }

    @Override
    public boolean affectsDamageType(DamageInfo.DamageType type) {
        return type != DamageInfo.DamageType.HP_LOSS;
    }

    @Override
    public boolean shouldPushIconToCard(AbstractCard card) {
        return putIconOnCard;
    }

    abstract public String getKeyword();
}
