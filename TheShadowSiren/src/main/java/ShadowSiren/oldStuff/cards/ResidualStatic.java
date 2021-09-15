package ShadowSiren.oldStuff.cards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.oldStuff.cards.abstractCards.AbstractHyperCard;
import ShadowSiren.oldStuff.cards.dualityCards.hyper.ResidualStaticDual;
import ShadowSiren.characters.Vivian;
import ShadowSiren.patches.ChargeCounterPatches;
import ShadowSiren.powers.ChargePower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class ResidualStatic extends AbstractHyperCard {

    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(ResidualStatic.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderSkill.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 1;
    private static final int BLOCK = 6;
    private static final int UPGRADE_PLUS_BLOCK = 3;
    private static final int CHARGE = 1;
    private static final int BPC = 2;
    private static final int UPGRADE_PLUS_BPC = 1;

    // /STAT DECLARATION/


    public ResidualStatic() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET, new ResidualStaticDual());
        block = baseBlock = BLOCK;
        magicNumber = baseMagicNumber = CHARGE;
        secondMagicNumber = baseSecondMagicNumber = BPC;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new GainBlockAction(p, p, block));
        this.addToBot(new SFXAction("ORB_LIGHTNING_CHANNEL", 0.1F));
        this.addToBot(new ApplyPowerAction(p, p, new ChargePower(p, magicNumber)));
    }

    @Override
    protected void applyPowersToBlock() {
        baseBlock += secondMagicNumber * ChargeCounterPatches.getChargesThisCombat(AbstractDungeon.player);
        super.applyPowersToBlock();
        baseBlock -= secondMagicNumber * ChargeCounterPatches.getChargesThisCombat(AbstractDungeon.player);
        isBlockModified = block != baseBlock;
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_PLUS_BLOCK);
            upgradeSecondMagicNumber(UPGRADE_PLUS_BPC);
            initializeDescription();
            super.upgrade();
        }
    }
}