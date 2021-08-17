package ShadowSiren.cards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cards.abstractCards.AbstractElectricCard;
import ShadowSiren.cards.interfaces.ModularDescription;
import ShadowSiren.characters.Vivian;
import ShadowSiren.damageModifiers.AbstractVivianDamageModifier;
import ShadowSiren.powers.ChargePower;
import ShadowSiren.util.XCostGrabber;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class TurboCharged extends AbstractElectricCard implements ModularDescription {

    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(TurboCharged.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderSkill.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = -1;
    private static final int BASE_EFFECT = 0;
    private static final int UPGRADE_BASE_EFFECT = 1;

    // /STAT DECLARATION/

    public TurboCharged() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = BASE_EFFECT;
        secondMagicNumber = baseSecondMagicNumber = BASE_EFFECT;
        initializeDescription();
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int effect = XCostGrabber.getXCostAmount(this);

        effect += magicNumber;

        if (effect > 0) {
            this.addToBot(new ApplyPowerAction(p, p, new ChargePower(p, effect)));
        }

        if (!this.freeToPlayOnce) {
            p.energy.use(EnergyPanel.totalCount);
        }
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        updateSecondValue();
        initializeDescription();
    }

    private void updateSecondValue() {
        secondMagicNumber = XCostGrabber.getXCostAmount(this, true);
        secondMagicNumber += magicNumber;
        isSecondMagicNumberModified = secondMagicNumber != baseSecondMagicNumber;
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_BASE_EFFECT);
            upgradeSecondMagicNumber(UPGRADE_BASE_EFFECT);
            initializeDescription();
        }
    }

    @Override
    public void changeDescription() {
        if (DESCRIPTION != null) {
            if (magicNumber > 0) {
                if (secondMagicNumber != 1) {
                    rawDescription = EXTENDED_DESCRIPTION[1];
                } else {
                    rawDescription = EXTENDED_DESCRIPTION[0];
                }
            } else {
                if (secondMagicNumber != 1) {
                    rawDescription = UPGRADE_DESCRIPTION;
                } else {
                    rawDescription = DESCRIPTION;
                }
            }
        }
    }
}