package ShadowSiren.cards.dualityCards.smoke;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.actions.TriggerBurnAction;
import ShadowSiren.cards.abstractCards.AbstractSmokeCard;
import ShadowSiren.cards.interfaces.MagicAnimation;
import ShadowSiren.cards.uniqueCards.UniqueCard;
import ShadowSiren.characters.Vivian;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class FlameSnapDual extends AbstractSmokeCard implements MagicAnimation, UniqueCard {

    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(FlameSnapDual.class.getSimpleName());
    public static final String IMG = makeCardPath("FlameSnap.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 0;
    private static final int DAMAGE = 3;
    private static final int UPGRADE_PLUS_DMG = 2;
    private static final int TRIGGER = 2;

    // /STAT DECLARATION/


    public FlameSnapDual() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        damage = baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = TRIGGER;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.FIRE));
        this.addToBot(new TriggerBurnAction(m, magicNumber));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            initializeDescription();
            super.upgrade();
        }
    }
}
