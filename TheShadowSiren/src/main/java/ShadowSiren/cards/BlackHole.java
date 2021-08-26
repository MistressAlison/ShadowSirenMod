package ShadowSiren.cards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cards.abstractCards.AbstractShadowCard;
import ShadowSiren.cards.interfaces.MagicAnimation;
import ShadowSiren.cards.interfaces.ModularDescription;
import ShadowSiren.characters.Vivian;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.EmptyStanceEffect;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class BlackHole extends AbstractShadowCard implements ModularDescription, MagicAnimation {


    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(BlackHole.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderAttack.png");
    // Setting the image as as easy as can possibly be now. You just need to provide the image name

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ALL;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 1;
    private static final int DAMAGE = 6;
    private static final int UPGRADE_PLUS_DMG = 3;
    private static final int INCREASE = 2;
    private static final int UPGRADE_PLUS_INCREASE = 1;

    // /STAT DECLARATION/

    //TODO VFX
    public BlackHole() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = damage = DAMAGE;
        magicNumber = baseMagicNumber = INCREASE;
        isMultiDamage = true;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new VFXAction(new EmptyStanceEffect(p.hb.cX, p.hb.cY)));
        for (AbstractPower pow : p.powers) {
            if (pow.type == AbstractPower.PowerType.DEBUFF && !(pow instanceof InvisiblePower)) {
                this.addToBot(new RemoveSpecificPowerAction(p, p, pow));
            }
        }
        this.addToBot(new DamageAllEnemiesAction(p, multiDamage, damageTypeForTurn, AbstractGameAction.AttackEffect.NONE, true));
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        int base = baseDamage;
        baseDamage += numDebuffs();
        super.calculateCardDamage(mo);
        baseDamage = base;
        isDamageModified = baseDamage != damage;
    }

    private int numDebuffs() {
        return (int) AbstractDungeon.player.powers.stream().filter(p -> p.type == AbstractPower.PowerType.DEBUFF).count();
    }

    private boolean hasDebuff() {
        return AbstractDungeon.player.powers.stream().anyMatch(p -> p.type == AbstractPower.PowerType.DEBUFF);
    }

    public void triggerOnGlowCheck() {
        super.triggerOnGlowCheck();
        if (hasDebuff()) {
            this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
        } else {
            this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            upgradeMagicNumber(UPGRADE_PLUS_INCREASE);
            initializeDescription();
        }
    }

    @Override
    public void changeDescription() {
        if (DESCRIPTION != null) {
            if (secondMagicNumber != 1) {
                rawDescription = DESCRIPTION;
            } else {
                rawDescription = EXTENDED_DESCRIPTION[1];
            }
        }
    }
}
