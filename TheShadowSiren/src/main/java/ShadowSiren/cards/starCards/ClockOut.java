package ShadowSiren.cards.starCards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cards.abstractCards.AbstractStarCard;
import ShadowSiren.cards.tempCards.TempCard;
import ShadowSiren.characters.Vivian;
import ShadowSiren.util.GameSpeedController;
import ShadowSiren.vfx.InversionSlowMotionEffect;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class ClockOut extends AbstractStarCard implements TempCard {

    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(ClockOut.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderSkill.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 2;
    private static final int EFFECT = 3;
    private static final int UPGRADE_PLUS_EFFECT = 1;
    // /STAT DECLARATION/


    public ClockOut() {
        this(false);
    }

    public ClockOut(boolean showStarCost) {
        super(ID, IMG, TYPE, COLOR, TARGET, showStarCost);
        baseMagicNumber = magicNumber = EFFECT;
        purgeOnUse = true;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new GameSpeedController.SlowMotionAction(new InversionSlowMotionEffect(10F, 4F)));
        for (AbstractMonster aM : AbstractDungeon.getMonsters().monsters) {
            if (!aM.isDeadOrEscaped()) {
                this.addToBot(new ApplyPowerAction(aM, p, new StrengthPower(aM, -magicNumber), -magicNumber, true));
            }
        }
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_EFFECT);
            initializeDescription();
        }
    }

    @Override
    public int getSpawnCost() {
        return COST;
    }
}
