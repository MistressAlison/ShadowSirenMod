package ShadowSiren.cards.starCards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cards.abstractCards.AbstractStarCard;
import ShadowSiren.characters.Vivian;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class SweetTreat extends AbstractStarCard {

    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(SweetTreat.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderSkill.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 1;
    private static final int HEAL = 5;
    private static final int UPGRADE_PLUS_HEAL = 3;
    private static final int ENERGY = 1;
    private static final int UPGRADE_PLUS_ENERGY = 1;
    // /STAT DECLARATION/


    public SweetTreat() {
        this(false);
    }

    public SweetTreat(boolean showStarCost) {
        super(ID, IMG, TYPE, COLOR, TARGET, showStarCost);
        baseMagicNumber = magicNumber = HEAL;
        baseSecondMagicNumber = secondMagicNumber = ENERGY;
        purgeOnUse = true;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new HealAction(p, p, magicNumber));
        this.addToBot(new GainEnergyAction(secondMagicNumber));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_HEAL);
            initializeDescription();
        }
    }

    @Override
    public int getSpawnCost() {
        return COST;
    }
}
