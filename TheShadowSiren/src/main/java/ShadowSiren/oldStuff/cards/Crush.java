package ShadowSiren.oldStuff.cards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.actions.CrushAction;
import ShadowSiren.characters.Vivian;
import ShadowSiren.oldStuff.cards.abstractCards.AbstractAbyssCard;
import ShadowSiren.oldStuff.cards.dualityCards.abyss.CrushDual;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class Crush extends AbstractAbyssCard {

    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(Crush.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderAttack.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final AbstractCard.CardRarity RARITY = CardRarity.UNCOMMON;
    private static final AbstractCard.CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final AbstractCard.CardType TYPE = CardType.ATTACK;
    public static final AbstractCard.CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 1;
    private static final int DAMAGE = 5;
    private static final int UPGRADE_PLUS_DAMAGE = 2;
    private static final int CARDS = 1;

    // /STAT DECLARATION/


    public Crush() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET, new CrushDual());
        damage = baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = CARDS;
        isMultiDamage = true;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new CrushAction(p, multiDamage, damageTypeForTurn));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DAMAGE);
            //rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
            super.upgrade();
        }
    }
}
