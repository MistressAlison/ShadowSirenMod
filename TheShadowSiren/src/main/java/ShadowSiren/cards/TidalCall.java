package ShadowSiren.cards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cards.abstractCards.AbstractAbyssCard;
import ShadowSiren.cards.dualityCards.abyss.TidalCallDual;
import ShadowSiren.characters.Vivian;
import ShadowSiren.powers.DrenchPower;
import ShadowSiren.stances.AbyssStance;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class TidalCall extends AbstractAbyssCard {

    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(TidalCall.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderAttack.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final AbstractCard.CardRarity RARITY = CardRarity.UNCOMMON;
    private static final AbstractCard.CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final AbstractCard.CardType TYPE = CardType.ATTACK;
    public static final AbstractCard.CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 1;
    private static final int DAMAGE = 3;
    private static final int UPGRADE_PLUS_DAMAGE = 2;
    private static final int WATER = 2;
    private static final int UPGRADE_PLUS_WATER = 1;

    // /STAT DECLARATION/


    public TidalCall() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET, new TidalCallDual());
        damage = baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = WATER;
        isMultiDamage = true;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ChangeStanceAction(new AbyssStance()));
        this.addToBot(new DamageAllEnemiesAction(p, multiDamage, damageTypeForTurn, AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters) {
            if (!mo.isDeadOrEscaped()) {
                this.addToBot(new ApplyPowerAction(mo, p, new DrenchPower(mo, magicNumber)));
            }
        }
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DAMAGE);
            upgradeMagicNumber(UPGRADE_PLUS_WATER);
            initializeDescription();
            super.upgrade();
        }
    }
}
