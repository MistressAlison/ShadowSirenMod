package ShadowSiren.cards.starCards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cards.abstractCards.AbstractStarCard;
import ShadowSiren.cards.tempCards.TempCard;
import ShadowSiren.characters.Vivian;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.SanctityEffect;
import com.megacrit.cardcrawl.vfx.combat.ScreenOnFireEffect;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class SuperNova extends AbstractStarCard implements TempCard {

    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(SuperNova.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderAttack.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 5;
    private static final int DAMAGE = 8;
    private static final int UPGRADE_PLUS_DAMAGE = 4;
    private static final int HITS = 5;
    // /STAT DECLARATION/


    public SuperNova() {
        this(false);
    }

    public SuperNova(boolean showStarCost) {
        super(ID, IMG, TYPE, COLOR, TARGET, showStarCost);
        damage = baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = HITS;
        isMultiDamage = true;
        purgeOnUse = true;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new VFXAction(new SanctityEffect(p.hb.cX, p.hb.cY), 0.4F));
        this.addToBot(new VFXAction(new ScreenOnFireEffect()));
        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.HIGH, ScreenShake.ShakeDur.MED, false);
        for (int i = 0 ; i < magicNumber ; i++) {
            this.addToBot(new DamageAllEnemiesAction(p, multiDamage, damageTypeForTurn, AbstractGameAction.AttackEffect.NONE));
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

    @Override
    public int getSpawnCost() {
        return COST;
    }
}
