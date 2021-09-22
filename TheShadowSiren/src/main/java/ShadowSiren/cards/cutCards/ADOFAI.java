package ShadowSiren.cards.cutCards;

import IconsAddon.util.DamageModifierHelper;
import IconsAddon.util.DamageModifierManager;
import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cards.abstractCards.AbstractMultiElementCard;
import ShadowSiren.cards.interfaces.MagicAnimation;
import ShadowSiren.characters.Vivian;
import ShadowSiren.damageModifiers.AbstractVivianDamageModifier;
import ShadowSiren.damageModifiers.FireDamage;
import ShadowSiren.damageModifiers.IceDamage;
import basemod.AutoAdd;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static ShadowSiren.ShadowSirenMod.makeCardPath;
@AutoAdd.Ignore
public class ADOFAI extends AbstractMultiElementCard implements MagicAnimation {


    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(ADOFAI.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderAttack.png");
    // Setting the image as as easy as can possibly be now. You just need to provide the image name

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 1;
    private static final int DAMAGE = 5;
    private static final int UPGRADE_PLUS_DMG = 2;

    private final Object fire = new Object();
    private final Object ice = new Object();

    private final FireDamage fireDamage = new FireDamage(AbstractVivianDamageModifier.TipType.DAMAGE, true, false);
    private final IceDamage iceDamage = new IceDamage(AbstractVivianDamageModifier.TipType.DAMAGE, true, false);

    // /STAT DECLARATION/

    public ADOFAI() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = damage = secondMagicNumber = baseSecondMagicNumber = DAMAGE;
        DamageModifierManager.addModifier(this, fireDamage);
        DamageModifierManager.addModifier(this, iceDamage);
        DamageModifierManager.addModifier(fire, new FireDamage());
        DamageModifierManager.addModifier(ice, new IceDamage());
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DamageAction(m, DamageModifierHelper.makeBoundDamageInfo(fire, p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        this.addToBot(new DamageAction(m, DamageModifierHelper.makeBoundDamageInfo(ice, p, secondMagicNumber, damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
    }

    @Override
    public void applyPowers() {
        DamageModifierManager.removeModifier(this, fireDamage);
        super.applyPowers();
        this.baseSecondMagicNumber = this.baseDamage;
        this.secondMagicNumber = this.damage;
        this.isSecondMagicNumberModified = this.isDamageModified;
        DamageModifierManager.removeModifier(this, iceDamage);
        DamageModifierManager.addModifier(this, fireDamage);
        super.applyPowers();
        DamageModifierManager.addModifier(this, iceDamage);
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        DamageModifierManager.removeModifier(this, fireDamage);
        super.calculateCardDamage(mo);
        this.baseSecondMagicNumber = this.baseDamage;
        this.secondMagicNumber = this.damage;
        this.isSecondMagicNumberModified = this.isDamageModified;
        DamageModifierManager.removeModifier(this, iceDamage);
        DamageModifierManager.addModifier(this, fireDamage);
        super.calculateCardDamage(mo);
        DamageModifierManager.addModifier(this, iceDamage);
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
