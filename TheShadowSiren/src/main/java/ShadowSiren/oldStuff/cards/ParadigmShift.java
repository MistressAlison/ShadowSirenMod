package ShadowSiren.oldStuff.cards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.characters.Vivian;
import ShadowSiren.oldStuff.cards.abstractCards.AbstractHyperCard;
import ShadowSiren.oldStuff.cards.dualityCards.hyper.ParadigmShiftDual;
import ShadowSiren.patches.StanceCounterPatches;
import ShadowSiren.stances.HyperStance;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class ParadigmShift extends AbstractHyperCard {

    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(ParadigmShift.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderSkill.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 4;
    private static final int UPGRADE_COST = 3;

    // /STAT DECLARATION/


    public ParadigmShift() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET, new ParadigmShiftDual());
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ChangeStanceAction(new HyperStance()));
    }

    @Override
    public void applyPowers() {
        calcCost();
        super.applyPowers();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        calcCost();
        super.calculateCardDamage(mo);
    }

    public void calcCost() {
        if (this.costForTurn > 0) {
            this.costForTurn = this.cost - StanceCounterPatches.getCombatStanceChanges(AbstractDungeon.player);
        }

        if (this.costForTurn < 0) {
            this.costForTurn = 0;
        }
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