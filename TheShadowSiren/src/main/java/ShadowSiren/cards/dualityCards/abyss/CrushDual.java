package ShadowSiren.cards.dualityCards.abyss;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cards.abstractCards.AbstractAbyssCard;
import ShadowSiren.cards.uniqueCards.UniqueCard;
import ShadowSiren.characters.Vivian;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class CrushDual extends AbstractAbyssCard implements UniqueCard {

    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(CrushDual.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderAttack.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final AbstractCard.CardRarity RARITY = CardRarity.UNCOMMON;
    private static final AbstractCard.CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final AbstractCard.CardType TYPE = CardType.ATTACK;
    public static final AbstractCard.CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 1;
    private static final int DAMAGE = 5;
    private static final int UPGRADE_PLUS_DAMAGE = 2;
    private static final int DPE = 1;
    private static final int UPGRADE_PLUS_DPE = 1;

    // /STAT DECLARATION/


    public CrushDual() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        damage = baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = DPE;
        secondMagicNumber = baseSecondMagicNumber = 0;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.LOW, ScreenShake.ShakeDur.SHORT, false);
        this.addToBot(new DamageAllEnemiesAction(p, DamageInfo.createDamageMatrix(damage+secondMagicNumber), damageTypeForTurn, AbstractGameAction.AttackEffect.SMASH));
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        secondMagicNumber = AbstractDungeon.player.exhaustPile.size() * magicNumber;
        isSecondMagicNumberModified = secondMagicNumber != baseSecondMagicNumber;
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        super.calculateCardDamage(mo);
        secondMagicNumber = AbstractDungeon.player.exhaustPile.size() * magicNumber;
        isSecondMagicNumberModified = secondMagicNumber != baseSecondMagicNumber;
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DAMAGE);
            upgradeMagicNumber(UPGRADE_PLUS_DPE);
            initializeDescription();
            super.upgrade();
        }
    }
}
