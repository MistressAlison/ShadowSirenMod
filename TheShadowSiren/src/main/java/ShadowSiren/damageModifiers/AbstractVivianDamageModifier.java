package ShadowSiren.damageModifiers;

import IconsAddon.damageModifiers.AbstractDamageModifier;
import IconsAddon.icons.AbstractCustomIcon;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;

public abstract class AbstractVivianDamageModifier extends AbstractDamageModifier {
    public enum TipType {
        DAMAGE_AND_BLOCK,
        DAMAGE,
        BLOCK,
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
