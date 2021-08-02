package ShadowSiren.cards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cards.abstractCards.AbstractDynamicCard;
import ShadowSiren.characters.Vivian;
import ShadowSiren.powers.FreezePower;
import com.evacipated.cardcrawl.mod.stslib.powers.StunMonsterPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class Ambush extends AbstractDynamicCard {

    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(Ambush.class.getSimpleName());
    public static final String IMG = makeCardPath("Ambush.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final AbstractCard.CardRarity RARITY = CardRarity.COMMON;
    private static final AbstractCard.CardTarget TARGET = CardTarget.ENEMY;
    private static final AbstractCard.CardType TYPE = CardType.SKILL;
    public static final AbstractCard.CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 0;
    private static final int VIGOR = 4;
    private static final int UPGRADE_PLUS_VIGOR = 2;
    private static final int ENERGY = 1;
    private static final int UPGRADE_PLUS_ENERGY = 1;

    // /STAT DECLARATION/


    public Ambush() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = VIGOR;
        secondMagicNumber = baseSecondMagicNumber = ENERGY;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (!m.isDeadOrEscaped() && (m.getIntentBaseDmg() > 0 || m.hasPower(StunMonsterPower.POWER_ID) || m.hasPower(FreezePower.POWER_ID))) {
            this.addToBot(new GainEnergyAction(secondMagicNumber));
            this.addToBot(new ApplyPowerAction(p, p, new VigorPower(m, magicNumber)));
        }
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeSecondMagicNumber(UPGRADE_PLUS_ENERGY);
            upgradeMagicNumber(UPGRADE_PLUS_VIGOR);
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}