package ShadowSiren.damageModifiers;

import IconsAddon.damageModifiers.AbstractDamageModifier;
import IconsAddon.icons.AbstractCustomIcon;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;

public abstract class AbstractVivianDamageModifier extends AbstractDamageModifier {
    public final CardStrings cardStrings;
    public boolean isAnElement = true;
    public AbstractVivianDamageModifier(String ID) {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    }

    abstract public AbstractCustomIcon getAccompanyingIcon();
    abstract public String getKeyword();
}
