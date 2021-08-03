package ShadowSiren.damageModifiers;

import IconsAddon.damageModifiers.AbstractDamageModifier;
import IconsAddon.icons.AbstractCustomIcon;
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
    TipType tipType;
    public AbstractVivianDamageModifier(String ID, TipType tipType) {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        this.tipType = tipType;
    }

    abstract public AbstractCustomIcon getAccompanyingIcon();
    abstract public String getKeyword();
}
