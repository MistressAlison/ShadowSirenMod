package ShadowSiren.cards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cards.abstractCards.AbstractDynamicCard;
import ShadowSiren.cards.abstractCards.AbstractHugeCard;
import ShadowSiren.cards.dualityCards.huge.QuickFistDual;
import ShadowSiren.cards.interfaces.FistAttack;
import ShadowSiren.characters.Vivian;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class QuickFist extends AbstractHugeCard implements FistAttack {

    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(QuickFist.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderAttack.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = -1;

    private static final int HITS = 3;
    private static final int UPGRADE_PLUS_HITS = 1;

    // /STAT DECLARATION/


    public QuickFist() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET, new QuickFistDual());
        damage = baseDamage = secondMagicNumber = baseSecondMagicNumber = 0;
        magicNumber = baseMagicNumber = HITS;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (AbstractDungeon.actionManager.cardsPlayedThisTurn.size() == 1) {
            baseDamage += 3;
            calculateCardDamage(m);
            baseDamage -= 3;
        }

        int effect = EnergyPanel.totalCount;

        if (this.energyOnUse != -1) {
            effect = this.energyOnUse;
        }

        if (p.hasRelic("Chemical X")) {
            effect += 2;
            p.getRelic("Chemical X").flash();
        }

        for(int i = 0; i < magicNumber; ++i) {
            this.addToBot(new DamageAction(m, new DamageInfo(p, damage+effect, damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_LIGHT, true));
        }

        if (!this.freeToPlayOnce) {
            p.energy.use(EnergyPanel.totalCount);
        }
    }

    @Override
    public void applyPowers() {
        secondMagicNumber = baseSecondMagicNumber = EnergyPanel.totalCount;
        if (AbstractDungeon.player.hasRelic("Chemical X")) {
            secondMagicNumber += 2;
        }
        if (AbstractDungeon.actionManager.cardsPlayedThisTurn.size() == 0) {
            baseDamage += 3;
        }
        super.applyPowers();
        if (AbstractDungeon.actionManager.cardsPlayedThisTurn.size() == 0) {
            baseDamage -= 3;
        }
        secondMagicNumber += damage;
        secondMagicNumber *= magicNumber;
        isSecondMagicNumberModified = secondMagicNumber != baseSecondMagicNumber;
        isDamageModified = damage != baseDamage;
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        secondMagicNumber = baseSecondMagicNumber = EnergyPanel.totalCount;
        if (AbstractDungeon.player.hasRelic("Chemical X")) {
            secondMagicNumber += 2;
        }
        if (AbstractDungeon.actionManager.cardsPlayedThisTurn.size() == 0) {
            baseDamage += 3;
        }
        super.calculateCardDamage(mo);
        if (AbstractDungeon.actionManager.cardsPlayedThisTurn.size() == 0) {
            baseDamage -= 3;
        }
        secondMagicNumber += damage;
        secondMagicNumber *= magicNumber;
        isSecondMagicNumberModified = secondMagicNumber != baseSecondMagicNumber;
        isDamageModified = damage != baseDamage;
    }

    @Override
    public void triggerOnGlowCheck() {
        super.triggerOnGlowCheck();
        if (AbstractDungeon.actionManager.cardsPlayedThisTurn.size() == 0) {
            this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
        }
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_HITS);
            initializeDescription();
            super.upgrade();
        }
    }
}