package ShadowSiren.oldStuff.cards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.actions.EnterAnyStanceAction;
import ShadowSiren.characters.Vivian;
import ShadowSiren.oldStuff.cards.abstractCards.prismatics.AbstractPrismaticBaseCard;
import ShadowSiren.oldStuff.cards.prismaticCards.*;
import ShadowSiren.patches.StanceCounterPatches;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class StanceDance extends AbstractPrismaticBaseCard {

    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(StanceDance.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderSkill.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final AbstractCard.CardRarity RARITY = CardRarity.RARE;
    private static final AbstractCard.CardTarget TARGET = CardTarget.SELF;
    private static final AbstractCard.CardType TYPE = CardType.SKILL;
    public static final AbstractCard.CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 2;
    private static final int UPGRADE_COST = 1;
    private static final int CARDS = 1;


    // /STAT DECLARATION/


    public StanceDance() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET, null, new StanceDanceVeil(), new StanceDanceAbyss(), new StanceDanceSmoke(), new StanceDanceHuge(), new StanceDanceHyper());
        magicNumber = baseMagicNumber = CARDS;
        secondMagicNumber = baseSecondMagicNumber = 0;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new EnterAnyStanceAction());
        if (secondMagicNumber > 0) {
            this.addToBot(new DrawCardAction(magicNumber*secondMagicNumber));
        }
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
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