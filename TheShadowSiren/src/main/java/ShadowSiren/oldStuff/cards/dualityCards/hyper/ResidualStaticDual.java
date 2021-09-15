package ShadowSiren.oldStuff.cards.dualityCards.hyper;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.oldStuff.cards.abstractCards.AbstractHyperCard;
import ShadowSiren.cards.uniqueCards.UniqueCard;
import ShadowSiren.characters.Vivian;
import ShadowSiren.patches.ChargeCounterPatches;
import ShadowSiren.powers.ChargePower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class ResidualStaticDual extends AbstractHyperCard implements UniqueCard {

    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(ResidualStaticDual.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderAttack.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF_AND_ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 1;
    private static final int DAMAGE = 6;
    private static final int UPGRADE_PLUS_DMG = 3;
    private static final int CHARGE = 1;
    private static final int DPC = 2;
    private static final int UPGRADE_PLUS_DPC = 1;

    // /STAT DECLARATION/


    public ResidualStaticDual() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        damage = baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = CHARGE;
        secondMagicNumber = baseSecondMagicNumber = DPC;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_LIGHT, true));
        this.addToBot(new SFXAction("ORB_LIGHTNING_CHANNEL", 0.1F));
        this.addToBot(new ApplyPowerAction(p, p, new ChargePower(p, magicNumber)));
    }

    @Override
    public void applyPowers() {
        baseDamage += secondMagicNumber * ChargeCounterPatches.getChargesThisCombat(AbstractDungeon.player);
        super.applyPowers();
        baseDamage -= secondMagicNumber * ChargeCounterPatches.getChargesThisCombat(AbstractDungeon.player);
        isDamageModified = damage != baseDamage;
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        baseDamage += secondMagicNumber * ChargeCounterPatches.getChargesThisCombat(AbstractDungeon.player);
        super.calculateCardDamage(mo);
        baseDamage -= secondMagicNumber * ChargeCounterPatches.getChargesThisCombat(AbstractDungeon.player);
        isDamageModified = damage != baseDamage;
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            upgradeSecondMagicNumber(UPGRADE_PLUS_DPC);
            initializeDescription();
            super.upgrade();
        }
    }
}