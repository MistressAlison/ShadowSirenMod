package ShadowSiren.blockTypes;

import IconsAddon.blockModifiers.AbstractBlockModifier;
import ShadowSiren.ShadowSirenMod;
import ShadowSiren.powers.ChillPower;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;

public class IceBlock extends AbstractBlockModifier {

    public static final String ID = ShadowSirenMod.makeID("IceBlock");
    public final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final Color c = Color.CYAN.cpy();

    public IceBlock() {}

    @Override
    public void onAttacked(DamageInfo info, int damageAmount) {
        if (info.owner != null && info.owner != owner) {
            this.addToBot(new ApplyPowerAction(info.owner, owner, new ChillPower(info.owner, 1)));
        }
    }

    @Override
    public Color blockImageColor() {
        return c;
    }

    @Override
    public Color blockTextColor() {
        return c;
    }

    @Override
    public AbstractBlockModifier makeCopy() {
        return new IceBlock();
    }

    @Override
    public String getName() {
        return cardStrings.NAME;
    }

    @Override
    public String getDescription() {
        return cardStrings.DESCRIPTION;
    }

    @Override
    public int subPriority() {
        return -1;
    }

    @Override
    public boolean isInherent() {
        return true;
    }
}
