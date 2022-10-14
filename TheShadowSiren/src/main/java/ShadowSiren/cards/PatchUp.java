package ShadowSiren.cards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cards.abstractCards.AbstractDynamicCard;
import ShadowSiren.cards.interfaces.ManuallySizeAdjustedCard;
import ShadowSiren.characters.Vivian;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class PatchUp extends AbstractDynamicCard implements ManuallySizeAdjustedCard {

    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(PatchUp.class.getSimpleName());
    public static final String IMG = makeCardPath("PatchUp.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 1;
    private static final int TEMP_HP = 4;
    private static final int UPGRADE_PLUS_TEMP_HP = 2;
    private static final int CARDS = 2;

    // /STAT DECLARATION/


    public PatchUp() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        block = baseBlock = TEMP_HP;
        magicNumber = baseMagicNumber = CARDS;
        this.exhaust = true;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new AddTemporaryHPAction(p, p, block));
        this.addToBot(new DrawCardAction(magicNumber));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_PLUS_TEMP_HP);
            initializeDescription();
        }
    }

    @Override
    public float getAdjustedScale() {
        return 0.99f;
    }
}
