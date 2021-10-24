package ShadowSiren.oldStuff.cards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.characters.Vivian;
import ShadowSiren.oldStuff.cards.abstractCards.AbstractShadowCard;
import ShadowSiren.oldStuff.cards.dualityCards.veil.EvilPlanDual;
import ShadowSiren.stances.VeilStance;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class EvilPlan extends AbstractShadowCard {

    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(EvilPlan.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderSkill.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final AbstractCard.CardRarity RARITY = AbstractCard.CardRarity.COMMON;
    private static final AbstractCard.CardTarget TARGET = CardTarget.SELF;
    private static final AbstractCard.CardType TYPE = CardType.SKILL;
    public static final AbstractCard.CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 1;
    private static final int BLOCK = 3;
    private static final int VIGOR = 3;
    private static final int UPGRADE_PLUS_BLOCK = 2;
    private static final int UPGRADE_PLUS_VIGOR = 2;

    // /STAT DECLARATION/


    public EvilPlan() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET, new EvilPlanDual());
        block = baseBlock = BLOCK;
        magicNumber = baseMagicNumber = VIGOR;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ChangeStanceAction(new VeilStance()));
        this.addToBot(new GainBlockAction(p, block));
        this.addToBot(new ApplyPowerAction(p, p, new VigorPower(p, magicNumber)));
        //this.addToBot(new ApplyPowerAction(p, p, new NextTurnVeilPower(p)));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_PLUS_BLOCK);
            upgradeMagicNumber(UPGRADE_PLUS_VIGOR);
            initializeDescription();
            super.upgrade();
        }
    }
}