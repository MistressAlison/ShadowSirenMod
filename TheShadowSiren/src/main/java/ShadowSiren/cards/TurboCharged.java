package ShadowSiren.cards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cardModifiers.ChargeModifier;
import ShadowSiren.cards.abstractCards.AbstractElectricCard;
import ShadowSiren.characters.Vivian;
import ShadowSiren.util.XCostGrabber;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class TurboCharged extends AbstractElectricCard {

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

    // /STAT DECLARATION/

    public TurboCharged() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        secondMagicNumber = baseSecondMagicNumber = BASE_EFFECT;
        exhaust = true;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int effect = XCostGrabber.getXCostAmount(this);

        for (AbstractCard c : p.hand.group) {
            if (c.baseBlock >= 0 || c.baseDamage >= 0) {
                CardModifierManager.addModifier(c, new ChargeModifier(effect));
            }
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
        isSecondMagicNumberModified = secondMagicNumber != baseSecondMagicNumber;
    }

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