package ShadowSiren.cards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cards.abstractCards.AbstractElectricCard;
import ShadowSiren.characters.Vivian;
import ShadowSiren.powers.ChargePower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.ChemicalX;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class TurboCharged extends AbstractElectricCard {

    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(TurboCharged.class.getSimpleName());
    public static final String IMG = makeCardPath("TurboCharge.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = -1;
    private static final int BASE_EFFECT = 0;
    private static final int MULTI = 4;

    // /STAT DECLARATION/

    public TurboCharged() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
//        secondMagicNumber = baseSecondMagicNumber = BASE_EFFECT;
        exhaust = true;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int effect = energyOnUse;

        if (p.hasRelic(ChemicalX.ID)) {
            effect += ChemicalX.BOOST;
        }

        if (effect > 0) {
            this.addToBot(new ApplyPowerAction(p, p, new ChargePower(p, effect * MULTI)));
        }

        if (!this.freeToPlayOnce) {
            p.energy.use(EnergyPanel.totalCount);
        }
    }

//    @Override
//    public void applyPowers() {
//        super.applyPowers();
//        updateSecondValue();
//        initializeDescription();
//    }
//
//    private void updateSecondValue() {
//        secondMagicNumber = XCostGrabber.getXCostAmount(this, true)*MULTI;
//        isSecondMagicNumberModified = secondMagicNumber != baseSecondMagicNumber;
//    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            exhaust = false;
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}