package ShadowSiren.cards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cards.abstractCards.AbstractShadowCard;
import ShadowSiren.cards.dualityCards.veil.AmbushDual;
import ShadowSiren.characters.Vivian;
import ShadowSiren.powers.NextTurnVigorPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.EnergizedBluePower;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class Ambush extends AbstractShadowCard {

    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(Ambush.class.getSimpleName());
    public static final String IMG = makeCardPath("Ambush.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final AbstractCard.CardRarity RARITY = CardRarity.COMMON;
    private static final AbstractCard.CardTarget TARGET = CardTarget.SELF;
    private static final AbstractCard.CardType TYPE = CardType.SKILL;
    public static final AbstractCard.CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 1;
    private static final int ENERGY = 1;
    private static final int UPGRADE_PLUS_ENERGY = 1;
    private static final int VIGOR = 5;

    // /STAT DECLARATION/


    public Ambush() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET, new AmbushDual());
        magicNumber = baseMagicNumber = VIGOR;
        secondMagicNumber = baseSecondMagicNumber = ENERGY;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        //Effect is next turn
        this.addToBot(new ApplyPowerAction(p, p, new EnergizedBluePower(p, secondMagicNumber)));
        this.addToBot(new ApplyPowerAction(p, p, new NextTurnVigorPower(p, magicNumber)));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeSecondMagicNumber(UPGRADE_PLUS_ENERGY);
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
            super.upgrade();
        }
    }
}