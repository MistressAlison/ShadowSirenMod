package ShadowSiren.oldStuff.cards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.characters.Vivian;
import ShadowSiren.oldStuff.cards.abstractCards.AbstractHugeCard;
import ShadowSiren.oldStuff.cards.dualityCards.huge.BigGlovesDual;
import ShadowSiren.stances.HugeStance;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class BigGloves extends AbstractHugeCard {

    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(BigGloves.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderSkill.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 1;

    private static final int EFFECT = 5;
    private static final int UPGRADE_PLUS_EFFECT = 2;

    // /STAT DECLARATION/

    public BigGloves() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET, new BigGlovesDual());
        magicNumber = baseMagicNumber = EFFECT;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ChangeStanceAction(new HugeStance()));
        this.addToBot(new ApplyPowerAction(p, p, new VigorPower(p, magicNumber)));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_EFFECT);
            initializeDescription();
            super.upgrade();
        }
    }
}