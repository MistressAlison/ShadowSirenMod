package ShadowSiren.oldStuff.cards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cards.interfaces.VigorMagicBuff;
import ShadowSiren.characters.Vivian;
import ShadowSiren.oldStuff.cards.abstractCards.AbstractSmokeCard;
import ShadowSiren.oldStuff.cards.dualityCards.smoke.FoxfireDual;
import ShadowSiren.oldStuff.powers.BurnPower;
import ShadowSiren.oldStuff.powers.HexingPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class Foxfire extends AbstractSmokeCard implements VigorMagicBuff {

    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(Foxfire.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderSkill.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final AbstractCard.CardRarity RARITY = CardRarity.RARE;
    private static final AbstractCard.CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final AbstractCard.CardType TYPE = CardType.SKILL;
    public static final AbstractCard.CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 1;
    private static final int BURN = 6;
    private static final int UPGRADE_PLUS_BURN = 3;
    private static final int WEAK = 2;
    private static final int UPGRADE_PLUS_WEAK = 1;
    private static final int TRIGGER = 1;
    private static final int UPGRADE_PLUS_TRIGGER = 1;

    // /STAT DECLARATION/

    public Foxfire() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET, new FoxfireDual());
        magicNumber = baseMagicNumber = BURN;
        secondMagicNumber = baseSecondMagicNumber = WEAK;
        //secondMagicNumber = baseSecondMagicNumber = TRIGGER;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (AbstractMonster aM : AbstractDungeon.getMonsters().monsters) {
            if (!aM.isDeadOrEscaped()) {
                this.addToBot(new ApplyPowerAction(aM, p, new BurnPower(aM, p, magicNumber)));
                this.addToBot(new ApplyPowerAction(aM, p, new HexingPower(aM, p, secondMagicNumber)));
                //this.addToBot(new TriggerBurnAction(aM, secondMagicNumber));
            }
        }
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_BURN);
            upgradeSecondMagicNumber(UPGRADE_PLUS_WEAK);
            //upgradeSecondMagicNumber(UPGRADE_PLUS_TRIGGER);
            initializeDescription();
            super.upgrade();
        }
    }
}