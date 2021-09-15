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
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class ParadigmShiftDual extends AbstractHyperCard implements UniqueCard {

    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(ParadigmShiftDual.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderAttack.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 5;
    private static final int DAMAGE = 20;
    private static final int UPGRADE_PLUS_DMG = 6;

    private static final int CHARGE = 1;
    private static final int UPGRADE_PLUS_CHARGE = 1;

    // /STAT DECLARATION/


    public ParadigmShiftDual() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        damage = baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = CHARGE;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SMASH));
        this.addToBot(new ApplyPowerAction(p, p, new ChargePower(p, magicNumber)));
    }

    @Override
    public void applyPowers() {
        setCostForTurn(Math.max(0,this.cost - ChargeCounterPatches.getChargesThisCombat(AbstractDungeon.player)));
        super.applyPowers();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        setCostForTurn(Math.max(0,this.cost - ChargeCounterPatches.getChargesThisCombat(AbstractDungeon.player)));
        super.calculateCardDamage(mo);
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            //upgradeDamage(UPGRADE_PLUS_DMG);
            upgradeMagicNumber(UPGRADE_PLUS_CHARGE);
            initializeDescription();
            super.upgrade();
        }
    }
}