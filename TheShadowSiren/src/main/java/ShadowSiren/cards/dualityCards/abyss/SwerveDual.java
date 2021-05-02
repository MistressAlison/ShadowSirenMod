package ShadowSiren.cards.dualityCards.abyss;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cards.abstractCards.AbstractAbyssCard;
import ShadowSiren.cards.uniqueCards.UniqueCard;
import ShadowSiren.characters.Vivian;
import ShadowSiren.powers.ConfusedPower;
import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class SwerveDual extends AbstractAbyssCard implements UniqueCard {

    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(SwerveDual.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderAttack.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final AbstractCard.CardRarity RARITY = CardRarity.UNCOMMON;
    private static final AbstractCard.CardTarget TARGET = CardTarget.ENEMY;
    private static final AbstractCard.CardType TYPE = CardType.ATTACK;
    public static final AbstractCard.CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 1;
    private static final int DAMAGE = 4;
    private static final int UPGRADE_PLUS_DAMAGE = 2;
    private static final int CONFUSE = 1;
    private static final int UPGRADE_PLUS_CONFUSE = 1;

    // /STAT DECLARATION/


    public SwerveDual() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        damage = baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = CONFUSE;
        secondMagicNumber = baseSecondMagicNumber = DAMAGE;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DamageAction(m, new DamageInfo(p, secondMagicNumber, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HEAVY));
        this.addToBot(new ApplyPowerAction(m, p, new ConfusedPower(m, magicNumber)));
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
            dmg = (int) ReflectionHacks.getPrivate(mo, AbstractMonster.class, "intentDmg");
            if ((boolean)ReflectionHacks.getPrivate(mo, AbstractMonster.class, "isMultiDmg"))
            {
                dmg *= (int)ReflectionHacks.getPrivate(mo, AbstractMonster.class, "intentMultiAmt");
            }
        }
        secondMagicNumber = damage + dmg/2;
        isSecondMagicNumberModified = secondMagicNumber != baseSecondMagicNumber;
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DAMAGE);
            upgradeMagicNumber(UPGRADE_PLUS_CONFUSE);
            initializeDescription();
            super.upgrade();
        }
    }
}
