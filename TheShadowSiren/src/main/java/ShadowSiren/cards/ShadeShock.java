package ShadowSiren.cards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cards.abstractCards.AbstractMultiElementCard;
import ShadowSiren.cards.interfaces.MagicAnimation;
import ShadowSiren.characters.Vivian;
import ShadowSiren.damageModifiers.ElectricDamage;
import ShadowSiren.damageModifiers.ShadowDamage;
import ShadowSiren.powers.ShockPower;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.damagemods.DamageModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class ShadeShock extends AbstractMultiElementCard implements MagicAnimation {


    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(ShadeShock.class.getSimpleName());
    public static final String IMG = makeCardPath("ShadeShock.png");
    // Setting the image as as easy as can possibly be now. You just need to provide the image name

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 1;
    private static final int DAMAGE = 6;
    private static final int UPGRADE_PLUS_DMG = 3;

    // /STAT DECLARATION/

    public ShadeShock() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = damage = DAMAGE;
        exhaust = true;
        DamageModifierManager.addModifier(this, new ElectricDamage());
        DamageModifierManager.addModifier(this, new ShadowDamage());
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.FIRE));
        this.addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                m.tint.color = Color.BLACK.cpy();
                m.tint.changeColor(Color.WHITE.cpy());
                this.isDone = true;
            }
        });
        this.addToBot(new ApplyPowerAction(m, p, new ShockPower(m, damage)));
        //this.addToBot(new ApplyPowerAction(m, p, new ShockPower(m, this.magicNumber), this.magicNumber, true, AbstractGameAction.AttackEffect.NONE));
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
