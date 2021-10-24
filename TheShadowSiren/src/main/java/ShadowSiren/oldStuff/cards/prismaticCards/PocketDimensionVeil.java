package ShadowSiren.oldStuff.cards.prismaticCards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cardModifiers.BideModifier;
import ShadowSiren.cards.interfaces.ModularDescription;
import ShadowSiren.cards.uniqueCards.UniqueCard;
import ShadowSiren.characters.Vivian;
import ShadowSiren.oldStuff.cards.abstractCards.prismatics.AbstractPrismaticVeilCard;
import ShadowSiren.oldStuff.powers.NewMoonPower;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class PocketDimensionVeil extends AbstractPrismaticVeilCard implements UniqueCard, ModularDescription {

    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(PocketDimensionVeil.class.getSimpleName());
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

    public PocketDimensionVeil() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = EFFECT;
        CardModifierManager.addModifier(this, new BideModifier(1, 0, 0, 1));
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ApplyPowerAction(p, p, new NewMoonPower(p, magicNumber)));
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

    @Override
    public void changeDescription() {
        rawDescription = magicNumber == 1 ? DESCRIPTION : UPGRADE_DESCRIPTION;
        initializeDescription();
    }
}