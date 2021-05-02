package ShadowSiren.cards.dualityCards.smoke;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cards.Cloak;
import ShadowSiren.cards.abstractCards.AbstractSmokeCard;
import ShadowSiren.cards.uniqueCards.UniqueCard;
import ShadowSiren.characters.Vivian;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class CloakDual extends AbstractSmokeCard implements UniqueCard {

    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(CloakDual.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderAttack.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 1;
    private static final int DAMAGE = 6;
    private static final int UPGRADE_PLUS_DAMAGE = 3;


    // /STAT DECLARATION/


    public CloakDual() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        damage = baseDamage = DAMAGE;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        super.calculateCardDamage(mo);
        if (hasDebuff(mo)) {
            damage *= 2;
            isDamageModified = damage != baseDamage;
        }
    }

    private boolean hasDebuff(AbstractMonster mo) {
        for (AbstractPower pow : mo.powers) {
            if (pow.type == AbstractPower.PowerType.DEBUFF) {
                return true;
            }
        }
        return false;
    }



    public void triggerOnGlowCheck() {
        super.triggerOnGlowCheck();
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            if (!m.isDeadOrEscaped() && hasDebuff(m)) {
                this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
                break;
            }
        }
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