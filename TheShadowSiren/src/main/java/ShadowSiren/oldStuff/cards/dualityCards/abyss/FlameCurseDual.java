package ShadowSiren.oldStuff.cards.dualityCards.abyss;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.oldStuff.cards.abstractCards.AbstractAbyssCard;
import ShadowSiren.cards.uniqueCards.UniqueCard;
import ShadowSiren.characters.Vivian;
import ShadowSiren.powers.ChillPower;
import ShadowSiren.oldStuff.powers.HexingPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class FlameCurseDual extends AbstractAbyssCard implements UniqueCard {

    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(FlameCurseDual.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderSkill.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final AbstractCard.CardRarity RARITY = CardRarity.COMMON;
    private static final AbstractCard.CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final AbstractCard.CardType TYPE = CardType.SKILL;
    public static final AbstractCard.CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 1;
    private static final int UPGRADE_COST = 0;
    private static final int CHILL = 3;
    private static final int HEX = 3;

    // /STAT DECLARATION/

    public FlameCurseDual() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = CHILL;
        secondMagicNumber = baseSecondMagicNumber = HEX;
        exhaust = true;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (AbstractMonster aM : AbstractDungeon.getMonsters().monsters) {
            if (!aM.isDeadOrEscaped()) {
                this.addToBot(new ApplyPowerAction(aM, p, new ChillPower(aM, magicNumber)));
                this.addToBot(new ApplyPowerAction(aM, p, new HexingPower(aM, p, secondMagicNumber)));
            }
        }
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADE_COST);
            initializeDescription();
            super.upgrade();
        }
    }
}