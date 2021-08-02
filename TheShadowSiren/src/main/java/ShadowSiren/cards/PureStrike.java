package ShadowSiren.cards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cards.abstractCards.AbstractDynamicCard;
import ShadowSiren.characters.Vivian;
import ShadowSiren.powers.ElementalPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class PureStrike extends AbstractDynamicCard {


    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(PureStrike.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderAttack.png");
    // Setting the image as as easy as can possibly be now. You just need to provide the image name

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 1;
    private static final int DAMAGE = 9;
    private static final int UPGRADE_PLUS_DMG = 3;
    private static final float DAMAGE_MULTIPLIER = 1.5f;

    // /STAT DECLARATION/

    public PureStrike() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = damage = DAMAGE;
        this.tags.add(CardTags.STRIKE);
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
    }

    @Override
    public void applyPowers() {
        if (!ElementalPower.hasAnElement()) {
            baseDamage *= DAMAGE_MULTIPLIER;
        }
        super.applyPowers();
        if (!ElementalPower.hasAnElement()) {
            baseDamage /= DAMAGE_MULTIPLIER;
        }
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        if (!ElementalPower.hasAnElement()) {
            baseDamage *= DAMAGE_MULTIPLIER;
        }
        super.calculateCardDamage(mo);
        if (!ElementalPower.hasAnElement()) {
            baseDamage /= DAMAGE_MULTIPLIER;
        }
    }

    public void triggerOnGlowCheck() {
        super.triggerOnGlowCheck();
        if (!ElementalPower.hasAnElement()) {
            this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
        } else {
            this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            initializeDescription();
        }
    }
}
