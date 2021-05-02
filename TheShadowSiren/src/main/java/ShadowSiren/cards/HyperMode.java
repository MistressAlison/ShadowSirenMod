package ShadowSiren.cards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cards.abstractCards.AbstractHyperCard;
import ShadowSiren.cards.dualityCards.hyper.HyperModeDual;
import ShadowSiren.characters.Vivian;
import ShadowSiren.patches.ChargeCounterPatches;
import ShadowSiren.stances.HyperStance;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class HyperMode extends AbstractHyperCard {

    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(HyperMode.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderSkill.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 6;
    private static final int UPGRADE_COST = 4;

    // /STAT DECLARATION/


    public HyperMode() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET, new HyperModeDual());
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ChangeStanceAction(new HyperStance()));
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        setCostForTurn(Math.max(0,this.cost - ChargeCounterPatches.getChargesThisCombat(AbstractDungeon.player)));
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