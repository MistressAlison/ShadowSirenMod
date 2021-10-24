package ShadowSiren.oldStuff.cards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.characters.Vivian;
import ShadowSiren.oldStuff.cards.abstractCards.AbstractAbyssCard;
import ShadowSiren.oldStuff.cards.dualityCards.abyss.AbyssalSurgeDual;
import ShadowSiren.stances.AbyssStance;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class AbyssalSurge extends AbstractAbyssCard {

    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(AbyssalSurge.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderSkill.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final AbstractCard.CardRarity RARITY = CardRarity.UNCOMMON;
    private static final AbstractCard.CardTarget TARGET = CardTarget.SELF;
    private static final AbstractCard.CardType TYPE = CardType.SKILL;
    public static final AbstractCard.CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 0;
    private static final int ENERGY = 1;

    // /STAT DECLARATION/


    public AbyssalSurge() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET, new AbyssalSurgeDual());
        exhaust = true;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ChangeStanceAction(new AbyssStance()));
        this.addToBot(new GainEnergyAction(ENERGY));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            rawDescription = UPGRADE_DESCRIPTION;
            exhaust = false;
            initializeDescription();
            super.upgrade();
        }
    }
}
