package ShadowSiren.cards.dualityCards.huge;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cards.abstractCards.AbstractHugeCard;
import ShadowSiren.cards.interfaces.FistAttack;
import ShadowSiren.cards.uniqueCards.UniqueCard;
import ShadowSiren.characters.Vivian;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class QuickFistDual extends AbstractHugeCard implements FistAttack, UniqueCard {

    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(QuickFistDual.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderAttack.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = -1;

    private static final int DAMAGE = 5;
    private static final int UPGRADE_PLUS_DAMAGE = 2;

    // /STAT DECLARATION/


    public QuickFistDual() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        damage = baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = 0;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int effect = EnergyPanel.totalCount;

        if (this.energyOnUse != -1) {
            effect = this.energyOnUse;
        }

        if (p.hasRelic("Chemical X")) {
            effect += 2;
            p.getRelic("Chemical X").flash();
        }

        for(int i = 0; i < magicNumber; ++i) {
            this.addToBot(new DamageAction(m, new DamageInfo(p, damage*effect, damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY, true));
        }

        if (!this.freeToPlayOnce) {
            p.energy.use(EnergyPanel.totalCount);
        }
    }

    @Override
    public void applyPowers() {
        magicNumber = baseMagicNumber = EnergyPanel.totalCount;
        if (AbstractDungeon.player.hasRelic("Chemical X")) {
            secondMagicNumber += 2;
        }
        super.applyPowers();
        magicNumber *= damage;
        isMagicNumberModified = magicNumber != baseMagicNumber;
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        magicNumber = baseMagicNumber = EnergyPanel.totalCount;
        if (AbstractDungeon.player.hasRelic("Chemical X")) {
            secondMagicNumber += 2;
        }
        super.calculateCardDamage(mo);
        magicNumber *= damage;
        isMagicNumberModified = magicNumber != baseMagicNumber;
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DAMAGE);
            initializeDescription();
            super.upgrade();
        }
    }
}