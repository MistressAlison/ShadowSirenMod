package ShadowSiren.oldStuff.cards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cards.interfaces.MagicAnimation;
import ShadowSiren.characters.Vivian;
import ShadowSiren.oldStuff.cards.abstractCards.AbstractShadowCard;
import ShadowSiren.oldStuff.cards.dualityCards.veil.TrickDual;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class Trick extends AbstractShadowCard implements MagicAnimation {

    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(Trick.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderAttack.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final AbstractCard.CardRarity RARITY = CardRarity.COMMON;
    private static final AbstractCard.CardTarget TARGET = CardTarget.ENEMY;
    private static final AbstractCard.CardType TYPE = CardType.ATTACK;
    public static final AbstractCard.CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 1;
    private static final int DAMAGE = 6;
    private static final int UPGRADE_PLUS_DAMAGE = 2;

    // /STAT DECLARATION/


    public Trick() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET, new TrickDual());
        damage = baseDamage = DAMAGE;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        //Gain block equal to expected damage
        this.addToBot(new GainBlockAction(p, damage));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DAMAGE);
            initializeDescription();
            super.upgrade();
        }
    }
}
