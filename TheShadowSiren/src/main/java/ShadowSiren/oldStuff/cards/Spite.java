package ShadowSiren.oldStuff.cards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.oldStuff.cards.abstractCards.AbstractHugeCard;
import ShadowSiren.oldStuff.cards.dualityCards.huge.SpiteDual;
import ShadowSiren.characters.Vivian;
import ShadowSiren.oldStuff.powers.HexingPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class Spite extends AbstractHugeCard {

    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(Spite.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderSkill.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 0;
    private static final int WEAK = 1;
    private static final int HEX = 4;
    private static final int UPGRADE_PLUS_HEX = 2;

    // /STAT DECLARATION/

    public Spite() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET, new SpiteDual());
        magicNumber = baseMagicNumber = HEX;
        secondMagicNumber = baseSecondMagicNumber = WEAK;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ApplyPowerAction(m, p, new WeakPower(m, secondMagicNumber, false)));
        this.addToBot(new ApplyPowerAction(m, p, new HexingPower(m, p, magicNumber)));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_HEX);
            initializeDescription();
            super.upgrade();
        }
    }
}