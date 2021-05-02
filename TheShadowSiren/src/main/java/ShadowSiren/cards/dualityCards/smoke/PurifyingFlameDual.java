package ShadowSiren.cards.dualityCards.smoke;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cards.abstractCards.AbstractSmokeCard;
import ShadowSiren.cards.uniqueCards.UniqueCard;
import ShadowSiren.characters.Vivian;
import ShadowSiren.powers.BurnPower;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class PurifyingFlameDual extends AbstractSmokeCard implements UniqueCard {

    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(PurifyingFlameDual.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderSkill.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final AbstractCard.CardRarity RARITY = CardRarity.COMMON;
    private static final AbstractCard.CardTarget TARGET = CardTarget.ENEMY;
    private static final AbstractCard.CardType TYPE = CardType.SKILL;
    public static final AbstractCard.CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 0;
    private static final int TEMP = 3;
    private static final int UPGRADE_PLUS_TEMP = 2;
    private static final int EXHAUST = 1;

    // /STAT DECLARATION/


    public PurifyingFlameDual() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = TEMP;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ExhaustAction(EXHAUST, false, false));
        this.addToBot(new AddTemporaryHPAction(p, p, magicNumber));
    }


    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_TEMP);
            initializeDescription();
            super.upgrade();
        }
    }
}