package ShadowSiren.cards.cutCards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.actions.IntensifyAction;
import ShadowSiren.cards.abstractCards.AbstractMultiElementCard;
import ShadowSiren.cards.interfaces.MagicAnimation;
import ShadowSiren.characters.Vivian;
import ShadowSiren.damageModifiers.AbstractVivianDamageModifier;
import ShadowSiren.damageModifiers.ElectricDamage;
import ShadowSiren.damageModifiers.FireDamage;
import ShadowSiren.damageModifiers.IceDamage;
import basemod.AutoAdd;
import com.evacipated.cardcrawl.mod.stslib.damagemods.BindingHelper;
import com.evacipated.cardcrawl.mod.stslib.damagemods.DamageModContainer;
import com.evacipated.cardcrawl.mod.stslib.damagemods.DamageModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static ShadowSiren.ShadowSirenMod.makeCardPath;
@AutoAdd.Ignore
public class TriAttack extends AbstractMultiElementCard implements MagicAnimation {


    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(TriAttack.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderAttack.png");
    // Setting the image as as easy as can possibly be now. You just need to provide the image name

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 1;
    private static final int UPGRADE_COST = 0;
    private static final int DAMAGE = 3; //x3
    private static final int UPGRADE_PLUS_DAMAGE = 2; //x3
    private static final int SCALE = 2; //x3
    private static final int UPGRADE_PLUS_SCALE = 1; //x3

    private final DamageModContainer fire = new DamageModContainer(new FireDamage());
    private final DamageModContainer ice = new DamageModContainer(new IceDamage());
    private final DamageModContainer electric = new DamageModContainer(new ElectricDamage());

    private final FireDamage fireDamage = new FireDamage(AbstractVivianDamageModifier.TipType.DAMAGE, true, false);
    private final IceDamage iceDamage = new IceDamage(AbstractVivianDamageModifier.TipType.DAMAGE, true, false);
    private final ElectricDamage electricDamage = new ElectricDamage(AbstractVivianDamageModifier.TipType.DAMAGE, true, false);

    // /STAT DECLARATION/

    public TriAttack() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = damage = secondMagicNumber = baseSecondMagicNumber = thirdMagicNumber = baseThirdMagicNumber = DAMAGE;
        magicNumber = baseMagicNumber = SCALE;
        DamageModifierManager.addModifier(this, fireDamage);
        DamageModifierManager.addModifier(this, iceDamage);
        DamageModifierManager.addModifier(this, electricDamage);
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DamageAction(m, BindingHelper.makeInfo(fire, p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_LIGHT, true));
        this.addToBot(new DamageAction(m, BindingHelper.makeInfo(ice, p, secondMagicNumber, damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_LIGHT, true));
        this.addToBot(new DamageAction(m, BindingHelper.makeInfo(electric, p, thirdMagicNumber, damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_LIGHT, true));
        this.addToBot(new IntensifyAction(this, magicNumber, IntensifyAction.EffectType.DAMAGE));
    }

    @Override
    public void applyPowers() {
        DamageModifierManager.removeModifier(this, fireDamage);
        DamageModifierManager.removeModifier(this, iceDamage);
        super.applyPowers();
        this.baseThirdMagicNumber = this.baseDamage;
        this.thirdMagicNumber = this.damage;
        this.isThirdMagicNumberModified = this.thirdMagicNumber != this.baseThirdMagicNumber;
        DamageModifierManager.removeModifier(this, electricDamage);
        DamageModifierManager.addModifier(this, iceDamage);
        super.applyPowers();
        this.baseSecondMagicNumber = this.baseDamage;
        this.secondMagicNumber = this.damage;
        this.isSecondMagicNumberModified = this.isDamageModified;
        DamageModifierManager.removeModifier(this, iceDamage);
        DamageModifierManager.addModifier(this, fireDamage);
        super.applyPowers();
        DamageModifierManager.addModifier(this, iceDamage);
        DamageModifierManager.addModifier(this, electricDamage);
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        DamageModifierManager.removeModifier(this, fireDamage);
        DamageModifierManager.removeModifier(this, iceDamage);
        super.calculateCardDamage(mo);
        this.baseThirdMagicNumber = this.baseDamage;
        this.thirdMagicNumber = this.damage;
        this.isThirdMagicNumberModified = this.thirdMagicNumber != this.baseThirdMagicNumber;
        DamageModifierManager.removeModifier(this, electricDamage);
        DamageModifierManager.addModifier(this, iceDamage);
        super.calculateCardDamage(mo);
        this.baseSecondMagicNumber = this.baseDamage;
        this.secondMagicNumber = this.damage;
        this.isSecondMagicNumberModified = this.isDamageModified;
        DamageModifierManager.removeModifier(this, iceDamage);
        DamageModifierManager.addModifier(this, fireDamage);
        super.calculateCardDamage(mo);
        DamageModifierManager.addModifier(this, iceDamage);
        DamageModifierManager.addModifier(this, electricDamage);
    }


    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            //upgradeDamage(UPGRADE_PLUS_DAMAGE);
            //upgradeSecondMagicNumber(UPGRADE_PLUS_DAMAGE);
            //upgradeThirdMagicNumber(UPGRADE_PLUS_DAMAGE);
            upgradeMagicNumber(UPGRADE_PLUS_SCALE);
            initializeDescription();
        }
    }
}
