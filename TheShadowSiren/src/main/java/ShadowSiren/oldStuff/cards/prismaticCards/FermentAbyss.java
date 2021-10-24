package ShadowSiren.oldStuff.cards.prismaticCards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cards.uniqueCards.UniqueCard;
import ShadowSiren.characters.Vivian;
import ShadowSiren.oldStuff.cards.abstractCards.prismatics.AbstractPrismaticAbyssCard;
import ShadowSiren.powers.ChillPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class FermentAbyss extends AbstractPrismaticAbyssCard implements UniqueCard {

    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(FermentAbyss.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderSkill.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 1;
    private static final int UPGRADE_COST = 0;

    private static final int WEAK = 1;
    private static final int UPGRADE_PLUS_WEAK = 1;
    private static final int CHILL = 3;
    private static final int UPGRADE_PLUS_CHILL = 1;

    // /STAT DECLARATION/
    public FermentAbyss() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = WEAK;
        secondMagicNumber = baseSecondMagicNumber = CHILL;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (AbstractMonster mon : AbstractDungeon.getMonsters().monsters) {
            if (!mon.isDeadOrEscaped()) {
                //Apply 1 fast and 1 slow so
                this.addToBot(new ApplyPowerAction(mon, p, new WeakPower(mon, magicNumber, false), magicNumber, true));
                this.addToBot(new ApplyPowerAction(mon, p, new ChillPower(mon, secondMagicNumber)));
            }
        }
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_WEAK);
            upgradeSecondMagicNumber(UPGRADE_PLUS_CHILL);
            initializeDescription();
            super.upgrade();
        }
    }
}