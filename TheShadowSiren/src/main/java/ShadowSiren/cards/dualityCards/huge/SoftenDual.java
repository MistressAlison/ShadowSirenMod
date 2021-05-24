package ShadowSiren.cards.dualityCards.huge;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cards.abstractCards.AbstractHugeCard;
import ShadowSiren.cards.uniqueCards.UniqueCard;
import ShadowSiren.characters.Vivian;
import ShadowSiren.powers.SoftPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class SoftenDual extends AbstractHugeCard implements UniqueCard {

    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(SoftenDual.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderSkill.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF_AND_ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 0;

    private static final int EFFECT = 1;
    private static final int UPGRADE_PLUS_EFFECT = 1;
    private static final int BLOCK = 5;
    private static final int UPGRADE_PLUS_BLOCK = 2;

    // /STAT DECLARATION/


    public SoftenDual() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = EFFECT;
        block = baseBlock = BLOCK;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new GainBlockAction(p, p, block));
        this.addToBot(new ApplyPowerAction(m, p, new SoftPower(m, magicNumber)));
        this.addToBot(new ApplyPowerAction(m, p, new VulnerablePower(m, magicNumber, false)));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_EFFECT);
            upgradeBlock(UPGRADE_PLUS_BLOCK);
            initializeDescription();
            super.upgrade();
        }
    }
}