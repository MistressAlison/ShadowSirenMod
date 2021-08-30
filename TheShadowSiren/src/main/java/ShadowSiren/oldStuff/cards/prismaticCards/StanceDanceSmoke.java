package ShadowSiren.oldStuff.cards.prismaticCards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.actions.EnterAnyStanceAction;
import ShadowSiren.oldStuff.cards.abstractCards.prismatics.AbstractPrismaticSmokeCard;
import ShadowSiren.cards.uniqueCards.UniqueCard;
import ShadowSiren.characters.Vivian;
import ShadowSiren.patches.StanceCounterPatches;
import ShadowSiren.oldStuff.powers.BurnPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class StanceDanceSmoke extends AbstractPrismaticSmokeCard implements UniqueCard {

    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(StanceDanceSmoke.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderSkill.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final AbstractCard.CardRarity RARITY = CardRarity.RARE;
    private static final AbstractCard.CardTarget TARGET = CardTarget.ENEMY;
    private static final AbstractCard.CardType TYPE = CardType.SKILL;
    public static final AbstractCard.CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 1;
    private static final int DRENCH = 2;
    private static final int UPGRADE_PLUS_DRENCH = 1;


    // /STAT DECLARATION/


    public StanceDanceSmoke() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = DRENCH;
        secondMagicNumber = baseSecondMagicNumber = 0;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new EnterAnyStanceAction());
        if (secondMagicNumber > 0) {
            this.addToBot(new ApplyPowerAction(m, p, new BurnPower(m, p, magicNumber*secondMagicNumber)));
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
            upgradeMagicNumber(UPGRADE_PLUS_DRENCH);
            initializeDescription();
            super.upgrade();
        }
    }
}