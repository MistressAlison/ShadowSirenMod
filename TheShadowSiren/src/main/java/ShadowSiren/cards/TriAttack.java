package ShadowSiren.cards;

import IconsAddon.util.DamageModifierHelper;
import IconsAddon.util.DamageModifierManager;
import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cards.abstractCards.AbstractMultiElementCard;
import ShadowSiren.cards.interfaces.MagicAnimation;
import ShadowSiren.characters.Vivian;
import ShadowSiren.damageModifiers.ElectricDamage;
import ShadowSiren.damageModifiers.FireDamage;
import ShadowSiren.damageModifiers.IceDamage;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

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
    private static final int DAMAGE = 10;
    private static final int UPGRADE_PLUS_DMG = 4;

    // /STAT DECLARATION/

    public TriAttack() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = damage = secondMagicNumber = baseSecondMagicNumber = DAMAGE;
        DamageModifierManager.addModifier(this, new FireDamage());
        DamageModifierManager.addModifier(this, new IceDamage());
        DamageModifierManager.addModifier(this, new ElectricDamage());
        this.exhaust = true;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DamageAction(m, DamageModifierHelper.makeBoundDamageInfo(this, p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.NONE));
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
