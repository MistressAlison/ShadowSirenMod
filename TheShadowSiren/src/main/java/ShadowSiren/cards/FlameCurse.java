package ShadowSiren.cards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cards.abstractCards.AbstractDynamicCard;
import ShadowSiren.cards.interfaces.VigorMagicBuff;
import ShadowSiren.characters.Vivian;
import ShadowSiren.powers.BurnPower;
import ShadowSiren.powers.HexingPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class FlameCurse extends AbstractDynamicCard implements VigorMagicBuff {

    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(FlameCurse.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderSkill.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final AbstractCard.CardRarity RARITY = CardRarity.COMMON;
    private static final AbstractCard.CardTarget TARGET = CardTarget.ENEMY;
    private static final AbstractCard.CardType TYPE = CardType.SKILL;
    public static final AbstractCard.CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 1;
    private static final int BURN = 3;
    private static final int HEX = 3;

    // /STAT DECLARATION/


    public FlameCurse() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = BURN;
        secondMagicNumber = baseSecondMagicNumber = HEX;
        exhaust = true;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (upgraded) {
            for (AbstractMonster aM : AbstractDungeon.getMonsters().monsters) {
                if (!aM.isDeadOrEscaped()) {
                    this.addToBot(new ApplyPowerAction(aM, p, new BurnPower(aM, p, magicNumber)));
                    this.addToBot(new ApplyPowerAction(aM, p, new HexingPower(aM, p, secondMagicNumber)));
                }
            }
        } else {
            this.addToBot(new ApplyPowerAction(m, p, new BurnPower(m, p, magicNumber)));
            this.addToBot(new ApplyPowerAction(m, p, new HexingPower(m, p, secondMagicNumber)));
        }
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            target = CardTarget.ALL_ENEMY;
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}