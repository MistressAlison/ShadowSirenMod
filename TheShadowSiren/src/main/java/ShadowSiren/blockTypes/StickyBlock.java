package ShadowSiren.blockTypes;

import IconsAddon.blockModifiers.AbstractBlockModifier;
import ShadowSiren.ShadowSirenMod;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;

public class StickyBlock extends AbstractBlockModifier {

    public static final String ID = ShadowSirenMod.makeID("StickyBlock");
    public final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final Color c = Color.GREEN.cpy();

    public StickyBlock() {}

    @Override
    public int amountLostAtStartOfTurn() {
        return 1;
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
        return new StickyBlock();
    }

    @Override
    public String getName() {
        return cardStrings.NAME;
    }

    @Override
    public String getDescription() {
        return cardStrings.DESCRIPTION;
    }
}
