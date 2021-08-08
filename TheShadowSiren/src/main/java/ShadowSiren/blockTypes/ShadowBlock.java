package ShadowSiren.blockTypes;

import IconsAddon.blockModifiers.AbstractBlockModifier;
import ShadowSiren.ShadowSirenMod;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;

public class ShadowBlock extends AbstractBlockModifier {

    public static final String ID = ShadowSirenMod.makeID("ShadowBlock");
    public final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final Color c = Color.DARK_GRAY.cpy();

    public ShadowBlock() {}

    @Override
    public int amountLostAtStartOfTurn() {
        return 0;
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
        return new ShadowBlock();
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
    public boolean isInherent() {
        return true;
    }
}
