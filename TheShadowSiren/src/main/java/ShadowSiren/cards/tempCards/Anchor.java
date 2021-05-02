package ShadowSiren.cards.tempCards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.actions.ImmediateExhaustCardAction;
import ShadowSiren.cards.abstractCards.AbstractAbyssCard;
import ShadowSiren.characters.Vivian;
import ShadowSiren.stances.AbyssStance;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class Anchor extends AbstractAbyssCard implements TempCard {

    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(Anchor.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderAttack.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final AbstractCard.CardRarity RARITY = CardRarity.UNCOMMON;
    private static final AbstractCard.CardTarget TARGET = CardTarget.NONE;
    private static final AbstractCard.CardType TYPE = CardType.STATUS;
    public static final AbstractCard.CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 0;

    // /STAT DECLARATION/


    public Anchor() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        exhaust = true;
        selfRetain = true;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {}

    @Override
    public void triggerOnGlowCheck() {
        super.triggerOnGlowCheck();
        if (!AbstractDungeon.player.stance.ID.equals(AbyssStance.STANCE_ID)) {
            this.addToTop(new ImmediateExhaustCardAction(this));
        }
    }

    @Override
    public void onRetained() {
        super.onRetained();
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            //rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
            super.upgrade();
        }
    }
}
