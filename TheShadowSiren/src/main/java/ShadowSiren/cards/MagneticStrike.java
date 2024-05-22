package ShadowSiren.cards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cards.abstractCards.AbstractMultiElementCard;
import ShadowSiren.characters.Vivian;
import ShadowSiren.damageModifiers.ElectricDamage;
import ShadowSiren.damageModifiers.IceDamage;
import com.evacipated.cardcrawl.mod.stslib.damagemods.DamageModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.EmptyStanceEffect;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class MagneticStrike extends AbstractMultiElementCard {


    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(MagneticStrike.class.getSimpleName());
    public static final String IMG = makeCardPath("MagneticStrike.png");
    // Setting the image as as easy as can possibly be now. You just need to provide the image name

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 1;
    private static final int DAMAGE = 8;
    private static final int UPGRADE_PLUS_DMG = 3;
    private static final float DAMAGE_MULTI = 1.5f;

    // /STAT DECLARATION/

    public MagneticStrike() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = damage = DAMAGE;
        DamageModifierManager.addModifier(this, new IceDamage());
        DamageModifierManager.addModifier(this, new ElectricDamage());
        this.tags.add(CardTags.STRIKE);
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        boolean hasBlock = m.currentBlock > 0;
        if (hasBlock) {
            this.addToBot(new VFXAction(new EmptyStanceEffect(m.hb.cX, m.hb.cY)));
        }
        this.addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), hasBlock?AbstractGameAction.AttackEffect.BLUNT_HEAVY:AbstractGameAction.AttackEffect.BLUNT_LIGHT));
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        int backup = baseDamage;
        if (mo.currentBlock > 0) {
            baseDamage *= DAMAGE_MULTI;
        }
        super.calculateCardDamage(mo);
        baseDamage = backup;
        isDamageModified = damage != baseDamage;
    }

    private boolean enemyHasBlock() {
        for (AbstractMonster aM : AbstractDungeon.getMonsters().monsters) {
            if (!aM.isDeadOrEscaped() && aM.currentBlock > 0){
                return true;
            }
        }
        return false;
    }

    public void triggerOnGlowCheck() {
        super.triggerOnGlowCheck();
        if (enemyHasBlock()) {
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
