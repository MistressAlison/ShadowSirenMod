package ShadowSiren.cards.dualityCards.veil;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cards.abstractCards.AbstractShadowCard;
import ShadowSiren.cards.uniqueCards.UniqueCard;
import ShadowSiren.characters.Vivian;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class AmbushDual extends AbstractShadowCard implements UniqueCard {

    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(AmbushDual.class.getSimpleName());
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


    public AmbushDual() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = VIGOR;
        secondMagicNumber = baseSecondMagicNumber = ENERGY;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        //If we already have Shadow, effect is immediate
        this.addToBot(new GainEnergyAction(secondMagicNumber));
        this.addToBot(new ApplyPowerAction(p, p, new VigorPower(p, magicNumber)));
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