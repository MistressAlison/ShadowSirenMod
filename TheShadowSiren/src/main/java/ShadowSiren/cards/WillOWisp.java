package ShadowSiren.cards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cards.abstractCards.AbstractSmokeCard;
import ShadowSiren.cards.dualityCards.smoke.WillOWispDual;
import ShadowSiren.cards.interfaces.VigorMagicBuff;
import ShadowSiren.characters.Vivian;
import ShadowSiren.powers.BurnPower;
import ShadowSiren.powers.FoxFirePower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class WillOWisp extends AbstractSmokeCard implements VigorMagicBuff {

    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(WillOWisp.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderAttack.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final AbstractCard.CardRarity RARITY = CardRarity.RARE;
    private static final AbstractCard.CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final AbstractCard.CardType TYPE = CardType.ATTACK;
    public static final AbstractCard.CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 1;
    private static final int BURN = 3;
    private static final int UPGRADE_PLUS_BURN = 1;

    // /STAT DECLARATION/

    public WillOWisp() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET, new WillOWispDual());
        magicNumber = baseMagicNumber = BURN;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (AbstractMonster aM : AbstractDungeon.getMonsters().monsters) {
            if (!aM.isDeadOrEscaped()) {
                int damAmount = 0;
                if (aM.hasPower(BurnPower.POWER_ID)) {
                    damAmount += aM.getPower(BurnPower.POWER_ID).amount;
                }
                if (aM.hasPower(FoxFirePower.POWER_ID)) {
                    damAmount += aM.getPower(FoxFirePower.POWER_ID).amount;
                }
                if (damAmount > 0) {
                    this.addToBot(new DamageAction(aM, new DamageInfo(p, damAmount, damageTypeForTurn), AbstractGameAction.AttackEffect.FIRE, true));
                }
                this.addToBot(new ApplyPowerAction(aM, p, new BurnPower(aM, p, magicNumber)));
            }
        }
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_BURN);
            initializeDescription();
            super.upgrade();
        }
    }
}