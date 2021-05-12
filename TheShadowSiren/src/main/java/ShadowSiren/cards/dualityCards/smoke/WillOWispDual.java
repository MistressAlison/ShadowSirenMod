package ShadowSiren.cards.dualityCards.smoke;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cards.abstractCards.AbstractSmokeCard;
import ShadowSiren.cards.interfaces.VigorMagicBuff;
import ShadowSiren.cards.uniqueCards.UniqueCard;
import ShadowSiren.characters.Vivian;
import ShadowSiren.powers.BurnPower;
import ShadowSiren.powers.FoxFirePower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class WillOWispDual extends AbstractSmokeCard implements VigorMagicBuff, UniqueCard {

    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(WillOWispDual.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderSkill.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final AbstractCard.CardRarity RARITY = CardRarity.RARE;
    private static final AbstractCard.CardTarget TARGET = CardTarget.ENEMY;
    private static final AbstractCard.CardType TYPE = CardType.SKILL;
    public static final AbstractCard.CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 0;
    private static final int BURN = 3;
    private static final int UPGRADE_PLUS_BURN = 1;
    private static final int WISP = 2;
    private static final int UPGRADE_PLUS_WISP = 1;

    // /STAT DECLARATION/

    public WillOWispDual() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = BURN;
        secondMagicNumber = baseSecondMagicNumber = WISP;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ApplyPowerAction(m, p, new BurnPower(m, p, magicNumber)));
        this.addToBot(new ApplyPowerAction(m, p, new FoxFirePower(m, p, secondMagicNumber), secondMagicNumber, true));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_BURN);
            upgradeSecondMagicNumber(UPGRADE_PLUS_WISP);
            initializeDescription();
            super.upgrade();
        }
    }
}