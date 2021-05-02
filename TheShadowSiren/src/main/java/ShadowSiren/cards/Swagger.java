package ShadowSiren.cards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cards.abstractCards.AbstractDynamicCard;
import ShadowSiren.cards.interfaces.VigorMagicBuff;
import ShadowSiren.characters.Vivian;
import ShadowSiren.powers.ConfusedPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DoubleDamagePower;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class Swagger extends AbstractDynamicCard implements VigorMagicBuff {

    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(Swagger.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderSkill.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final AbstractCard.CardRarity RARITY = CardRarity.UNCOMMON;
    private static final AbstractCard.CardTarget TARGET = CardTarget.ENEMY;
    private static final AbstractCard.CardType TYPE = CardType.SKILL;
    public static final AbstractCard.CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 1;
    private static final int UPGRADE_COST = 0;
    private static final int CONFUSED = 2;
    private static final int DOUBLE_DAMAGE = 1;

    // /STAT DECLARATION/


    public Swagger() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = CONFUSED;
        secondMagicNumber = baseSecondMagicNumber = DOUBLE_DAMAGE;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ApplyPowerAction(m, p, new ConfusedPower(m, magicNumber)));
        this.addToBot(new ApplyPowerAction(m, p, new DoubleDamagePower(m, secondMagicNumber, false)));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADE_COST);
            initializeDescription();
        }
    }
}