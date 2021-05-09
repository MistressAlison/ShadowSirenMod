package ShadowSiren.cards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cards.abstractCards.AbstractDynamicCard;
import ShadowSiren.cards.abstractCards.AbstractPrismaticCard;
import ShadowSiren.cards.abstractCards.prismatics.AbstractPrismaticBaseCard;
import ShadowSiren.cards.prismaticCards.*;
import ShadowSiren.characters.Vivian;
import ShadowSiren.powers.NewMoonPower;
import ShadowSiren.powers.PocketDimensionPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class PocketDimension extends AbstractPrismaticBaseCard {

    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(PocketDimension.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderPower.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 1;
    private static final int UPGRADE_COST = 0;

    private static final int EFFECT = 1;

    // /STAT DECLARATION/
    //TODO different upgrade
    public PocketDimension() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET, null, new PocketDimensionVeil(), new PocketDimensionAbyss(), new PocketDimensionSmoke(), new PocketDimensionHuge(), new PocketDimensionHyper());
        magicNumber = baseMagicNumber = EFFECT;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ApplyPowerAction(p, p, new PocketDimensionPower(p, magicNumber)));
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