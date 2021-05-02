package ShadowSiren.cards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cards.abstractCards.AbstractDynamicCard;
import ShadowSiren.cards.interfaces.VigorMagicBuff;
import ShadowSiren.characters.Vivian;
import ShadowSiren.powers.BurnPower;
import ShadowSiren.powers.WillOWispPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class WillOWisp extends AbstractDynamicCard implements VigorMagicBuff {

    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(WillOWisp.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderSkill.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final AbstractCard.CardRarity RARITY = CardRarity.RARE;
    private static final AbstractCard.CardTarget TARGET = CardTarget.ENEMY;
    private static final AbstractCard.CardType TYPE = CardType.SKILL;
    public static final AbstractCard.CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 2;
    private static final int BURN = 6;
    private static final int UPGRADE_PLUS_BURN = 3;

    // /STAT DECLARATION/


    public WillOWisp() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = BURN;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ApplyPowerAction(m, p, new BurnPower(m, p, magicNumber)));
        this.addToBot(new ApplyPowerAction(m, p, new WillOWispPower(m, p)));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_BURN);
            initializeDescription();
        }
    }
}