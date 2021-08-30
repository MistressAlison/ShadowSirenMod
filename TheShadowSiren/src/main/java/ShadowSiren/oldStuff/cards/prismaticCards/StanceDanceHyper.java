package ShadowSiren.oldStuff.cards.prismaticCards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.actions.EnterAnyStanceAction;
import ShadowSiren.oldStuff.cards.abstractCards.prismatics.AbstractPrismaticHyperCard;
import ShadowSiren.cards.uniqueCards.UniqueCard;
import ShadowSiren.characters.Vivian;
import ShadowSiren.patches.StanceCounterPatches;
import ShadowSiren.oldStuff.powers.ElectricPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class StanceDanceHyper extends AbstractPrismaticHyperCard implements UniqueCard {

    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(StanceDanceHyper.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderSkill.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final AbstractCard.CardRarity RARITY = CardRarity.RARE;
    private static final AbstractCard.CardTarget TARGET = CardTarget.SELF;
    private static final AbstractCard.CardType TYPE = CardType.SKILL;
    public static final AbstractCard.CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 2;
    private static final int UPGRADE_COST = 1;
    private static final int ELEC = 1;


    // /STAT DECLARATION/


    public StanceDanceHyper() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = ELEC;
        secondMagicNumber = baseSecondMagicNumber = 0;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new EnterAnyStanceAction());
        if (secondMagicNumber > 0) {
            this.addToBot(new ApplyPowerAction(p, p, new ElectricPower(p, magicNumber*secondMagicNumber)));
        }
    }

    @Override
    protected void applyPowersToBlock() {
        super.applyPowersToBlock();
        secondMagicNumber = StanceCounterPatches.getCombatUniqueStances(AbstractDungeon.player);
        isSecondMagicNumberModified = secondMagicNumber != baseSecondMagicNumber;
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADE_COST);
            initializeDescription();
            super.upgrade();
        }
    }
}