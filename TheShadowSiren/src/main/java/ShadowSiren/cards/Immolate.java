package ShadowSiren.cards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cards.abstractCards.AbstractFireCard;
import ShadowSiren.characters.Vivian;
import ShadowSiren.powers.ImmolatePower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class Immolate extends AbstractFireCard {


    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(Immolate.class.getSimpleName());
    public static final String IMG = makeCardPath("Immolate.png");
    // Setting the image as as easy as can possibly be now. You just need to provide the image name

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 1;
    private static final int DMG = 5;
    private static final int UPGRADE_PLUS_DMG = 2;

    // /STAT DECLARATION/

    public Immolate() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = DMG;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ApplyPowerAction(p, p, new ImmolatePower(p, magicNumber)));
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_DMG);
            initializeDescription();
        }
    }
}
