package ShadowSiren.cards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.actions.BlastBurnAction;
import ShadowSiren.cards.abstractCards.AbstractFireCard;
import ShadowSiren.cards.tempCards.Ashes;
import ShadowSiren.characters.Vivian;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class BlastBurn extends AbstractFireCard {


    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(BlastBurn.class.getSimpleName());
    public static final String IMG = makeCardPath("BlastBurn.png");
    // Setting the image as as easy as can possibly be now. You just need to provide the image name

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 2;
    private static final int DAMAGE = 15;
    private static final int UPGRADE_PLUS_DMG = 5;
    private static final int ASHES = 2;
    private static final int UPGRADE_PLUS_ASHES = 1;

    // /STAT DECLARATION/

    public BlastBurn() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = damage = DAMAGE;
        magicNumber = baseMagicNumber = ASHES;
        cardsToPreview = new Ashes();
        isMultiDamage = true;
        exhaust = true;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new BlastBurnAction(p, multiDamage, damageTypeForTurn));
        this.addToBot(new MakeTempCardInHandAction(cardsToPreview, magicNumber));
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            //upgradeMagicNumber(UPGRADE_PLUS_ASHES);
            initializeDescription();
        }
    }
}
