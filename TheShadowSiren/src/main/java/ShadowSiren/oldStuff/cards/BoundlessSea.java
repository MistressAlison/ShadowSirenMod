package ShadowSiren.oldStuff.cards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.actions.DrawAndExhaustIfAction;
import ShadowSiren.characters.Vivian;
import ShadowSiren.oldStuff.cards.abstractCards.AbstractAbyssCard;
import ShadowSiren.oldStuff.cards.dualityCards.abyss.BoundlessSeaDual;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class BoundlessSea extends AbstractAbyssCard {

    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(BoundlessSea.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderSkill.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final AbstractCard.CardRarity RARITY = CardRarity.UNCOMMON;
    private static final AbstractCard.CardTarget TARGET = CardTarget.SELF;
    private static final AbstractCard.CardType TYPE = CardType.SKILL;
    public static final AbstractCard.CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 1;
    private static final int DRAW = 2;
    private static final int UPGRADE_PLUS_DRAW = 1;

    // /STAT DECLARATION/


    public BoundlessSea() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET, new BoundlessSeaDual());
        magicNumber = baseMagicNumber = DRAW;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DrawAndExhaustIfAction(magicNumber));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_DRAW);
            initializeDescription();
            super.upgrade();
        }
    }
}
