package ShadowSiren.cards;

import IconsAddon.util.DamageModifierHelper;
import ShadowSiren.ShadowSirenMod;
import ShadowSiren.actions.ElementalBurstAction;
import ShadowSiren.cards.abstractCards.AbstractDynamicCard;
import ShadowSiren.cards.abstractCards.AbstractInertCard;
import ShadowSiren.characters.Vivian;
import ShadowSiren.powers.ElementalPower;
import ShadowSiren.util.ParticleOrbitRenderer;
import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class ElementalBurst extends AbstractDynamicCard {

    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(ElementalBurst.class.getSimpleName());
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


    public ElementalBurst() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        damage = baseDamage = DAMAGE;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int effect = ElementalPower.numActiveElements();
        if (effect > 0) {
            this.addToBot(new ElementalBurstAction(m, new DamageInfo(p, damage, damageTypeForTurn), effect));
        }
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DAMAGE);
            initializeDescription();
        }
    }
}
