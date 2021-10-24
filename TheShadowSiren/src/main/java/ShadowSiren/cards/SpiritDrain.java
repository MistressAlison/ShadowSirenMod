package ShadowSiren.cards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cards.abstractCards.AbstractShadowCard;
import ShadowSiren.characters.Vivian;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class SpiritDrain extends AbstractShadowCard {

    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(SpiritDrain.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderSkill.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final AbstractCard.CardRarity RARITY = CardRarity.COMMON;
    private static final AbstractCard.CardTarget TARGET = CardTarget.ALL;
    private static final AbstractCard.CardType TYPE = CardType.SKILL;
    public static final AbstractCard.CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 1;
    private static final int AMOUNT = 2;
    private static final int UPGRADE_PLUS_AMOUNT = 1;

    // /STAT DECLARATION/


    public SpiritDrain() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = AMOUNT;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        //int sum = 0;
        for (AbstractMonster aM : AbstractDungeon.getMonsters().monsters) {
            if (!aM.isDeadOrEscaped()) {
                this.addToBot(new ApplyPowerAction(aM, p, new StrengthPower(aM, -this.magicNumber), -this.magicNumber, true, AbstractGameAction.AttackEffect.NONE));
                if (!aM.hasPower(ArtifactPower.POWER_ID)) {
                    this.addToBot(new ApplyPowerAction(aM, p, new GainStrengthPower(aM, this.magicNumber), this.magicNumber, true, AbstractGameAction.AttackEffect.NONE));
                    //sum += magicNumber;
                }
            }
        }
        this.addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, magicNumber)));
        this.addToBot(new ApplyPowerAction(p, p, new LoseStrengthPower(p, magicNumber)));
//        if (sum > 0) {
//            this.addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, sum)));
//            this.addToBot(new ApplyPowerAction(p, p, new LoseStrengthPower(p, sum)));
//        }
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_AMOUNT);
            initializeDescription();
        }
    }
}