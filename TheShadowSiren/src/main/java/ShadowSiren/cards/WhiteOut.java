package ShadowSiren.cards;

import IconsAddon.util.DamageModifierHelper;
import IconsAddon.util.DamageModifierManager;
import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cards.abstractCards.AbstractMultiElementCard;
import ShadowSiren.characters.Vivian;
import ShadowSiren.damageModifiers.IceDamage;
import ShadowSiren.damageModifiers.ShadowDamage;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.BlizzardEffect;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class WhiteOut extends AbstractMultiElementCard {


    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(WhiteOut.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderAttack.png");
    // Setting the image as as easy as can possibly be now. You just need to provide the image name

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 1;
    private static final int DAMAGE = 15;
    private static final int UPGRADE_PLUS_DMG = 5;

    // /STAT DECLARATION/

    public WhiteOut() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = damage = DAMAGE;
        isMultiDamage = true;
        exhaust = true;
        DamageModifierManager.addModifier(this, new IceDamage());
        DamageModifierManager.addModifier(this, new ShadowDamage());
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new VFXAction(new BlizzardEffect(damage, AbstractDungeon.getMonsters().shouldFlipVfx()), 0.3F));
        this.addToBot(DamageModifierHelper.makeModifiedDamageAllEnemiesAction(this, p, damage, damageTypeForTurn, AbstractGameAction.AttackEffect.BLUNT_LIGHT));
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
