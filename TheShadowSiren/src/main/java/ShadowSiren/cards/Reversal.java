package ShadowSiren.cards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cards.abstractCards.AbstractDynamicCard;
import ShadowSiren.cards.tempCards.Hammer;
import ShadowSiren.characters.Vivian;
import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class Reversal extends AbstractDynamicCard {


    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(Reversal.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderAttack.png");
    // Setting the image as as easy as can possibly be now. You just need to provide the image name

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final AbstractCard.CardRarity RARITY = CardRarity.UNCOMMON;
    private static final AbstractCard.CardTarget TARGET = CardTarget.ENEMY;
    private static final AbstractCard.CardType TYPE = AbstractCard.CardType.ATTACK;
    public static final AbstractCard.CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 1;
    private static final int DAMAGE = 5;
    private static final int UPGRADE_PLUS_DMG = 2;
    private static final int EFFECT = 1;
    private static final int UPGRADE_PLUS_EFFECT = 1;

    // /STAT DECLARATION/

    public Reversal() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = damage = DAMAGE;
        secondMagicNumber = baseSecondMagicNumber = DAMAGE;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DamageAction(m, new DamageInfo(p, secondMagicNumber, damageTypeForTurn), AbstractGameAction.AttackEffect.SMASH));
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        secondMagicNumber = damage;
        isSecondMagicNumberModified = secondMagicNumber != baseSecondMagicNumber;
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        super.calculateCardDamage(mo);
        int dmg = 0;
        if (!mo.isDeadOrEscaped() && mo.getIntentBaseDmg() > 0) {
            dmg = ReflectionHacks.<Integer>getPrivate(mo, AbstractMonster.class, "intentDmg");
            if (ReflectionHacks.<Boolean>getPrivate(mo, AbstractMonster.class, "isMultiDmg"))
            {
                dmg *= ReflectionHacks.<Integer>getPrivate(mo, AbstractMonster.class, "intentMultiAmt");
            }
        }
        secondMagicNumber = damage + dmg/2;
        isSecondMagicNumberModified = secondMagicNumber != baseSecondMagicNumber;
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            upgradeSecondMagicNumber(UPGRADE_PLUS_DMG);
            initializeDescription();
        }
    }
}
