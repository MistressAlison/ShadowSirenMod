package ShadowSiren.oldStuff.cards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cardModifiers.BideModifier;
import ShadowSiren.oldStuff.cards.abstractCards.AbstractHyperCard;
import ShadowSiren.oldStuff.cards.dualityCards.hyper.PowerBoostDual;
import ShadowSiren.characters.Vivian;
import ShadowSiren.powers.ChargePower;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class PowerBoost extends AbstractHyperCard {

    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(PowerBoost.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderSkill.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final AbstractCard.CardRarity RARITY = CardRarity.RARE;
    private static final AbstractCard.CardTarget TARGET = CardTarget.SELF;
    private static final AbstractCard.CardType TYPE = CardType.SKILL;
    public static final AbstractCard.CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 0;
    private static final int CHARGE = 1;
    private static final int UPGRADE_PLUS_CHARGE = 1;
    private static final int VIGOR = 5;
    private static final int UPGRADE_PLUS_VIGOR = 3;

    // /STAT DECLARATION/


    public PowerBoost() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET, new PowerBoostDual());
        magicNumber = baseMagicNumber = CHARGE;
        secondMagicNumber = baseSecondMagicNumber = VIGOR;
        exhaust = true;
        CardModifierManager.addModifier(this, new BideModifier(3, 0, 0, UPGRADE_PLUS_CHARGE, UPGRADE_PLUS_VIGOR, 0));
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ApplyPowerAction(p, p, new ChargePower(p, magicNumber)));
        this.addToBot(new ApplyPowerAction(p, p, new VigorPower(p, secondMagicNumber)));
    }


    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_CHARGE);
            upgradeSecondMagicNumber(UPGRADE_PLUS_VIGOR);
            initializeDescription();
            super.upgrade();
        }
    }
}