package ShadowSiren.cards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cards.abstractCards.AbstractSmokeCard;
import ShadowSiren.cards.dualityCards.smoke.PreparationDual;
import ShadowSiren.characters.Vivian;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.EnergizedBluePower;
import com.megacrit.cardcrawl.powers.EquilibriumPower;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class Preparation extends AbstractSmokeCard {

    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(Preparation.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderSkill.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 1;
    private static final int ENERGY = 2;
    private static final int UPGRADE_PLUS_ENERGY = 1;


    // /STAT DECLARATION/


    public Preparation() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET, new PreparationDual());
        magicNumber = baseMagicNumber = ENERGY;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ApplyPowerAction(p, p, new EquilibriumPower(p, 1)));
        this.addToBot(new ApplyPowerAction(p, p, new EnergizedBluePower(p, magicNumber)));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_ENERGY);
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
            super.upgrade();
        }
    }
}