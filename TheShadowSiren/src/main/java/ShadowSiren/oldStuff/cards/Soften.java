package ShadowSiren.oldStuff.cards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.oldStuff.cards.abstractCards.AbstractHugeCard;
import ShadowSiren.oldStuff.cards.dualityCards.huge.SoftenDual;
import ShadowSiren.characters.Vivian;
import ShadowSiren.powers.SoftPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class Soften extends AbstractHugeCard {

    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(Soften.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderSkill.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF_AND_ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 1;
    private static final int UPGRADE_COST = 0;

    private static final int SOFT = 2;
    private static final int UPGRADE_PLUS_SOFT = 1;
    private static final int BLOCK = 5;
    private static final int UPGRADE_PLUS_BLOCK = 2;

    // /STAT DECLARATION/


    public Soften() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET, new SoftenDual());
        magicNumber = baseMagicNumber = SOFT;
        block = baseBlock = BLOCK;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new GainBlockAction(p, p, block));
        this.addToBot(new ApplyPowerAction(m, p, new SoftPower(m, magicNumber)));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            //upgradeBaseCost(UPGRADE_COST);
            upgradeMagicNumber(UPGRADE_PLUS_SOFT);
            upgradeBlock(UPGRADE_PLUS_BLOCK);
            //rawDescription = UPGRADE_DESCRIPTION;
            //target = CardTarget.ALL_ENEMY;
            initializeDescription();
            super.upgrade();
        }
    }
}