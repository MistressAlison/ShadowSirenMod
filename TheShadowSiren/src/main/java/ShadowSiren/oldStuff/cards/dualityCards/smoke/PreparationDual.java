package ShadowSiren.oldStuff.cards.dualityCards.smoke;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.actions.BuffDamageAllCardsInHandAction;
import ShadowSiren.cards.uniqueCards.UniqueCard;
import ShadowSiren.characters.Vivian;
import ShadowSiren.oldStuff.cards.abstractCards.AbstractSmokeCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class PreparationDual extends AbstractSmokeCard implements UniqueCard {

    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(PreparationDual.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderSkill.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = -2;
    private static final int BONUS_DAMAGE = 2;
    private static final int UPGRADE_PLUS_BONUS_DAMAGE = 1;


    // /STAT DECLARATION/


    public PreparationDual() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET, new CloakDual());
        magicNumber = baseMagicNumber = BONUS_DAMAGE;
    }

    @Override
    public void onRetained() {
        this.addToTop(new BuffDamageAllCardsInHandAction(magicNumber, AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy()));
        //flash(AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy());
        superFlash();
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {}

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return false;
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_BONUS_DAMAGE);
            initializeDescription();
            super.upgrade();
        }
    }
}