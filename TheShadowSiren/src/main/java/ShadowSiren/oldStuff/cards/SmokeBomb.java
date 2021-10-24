package ShadowSiren.oldStuff.cards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cardModifiers.BideModifier;
import ShadowSiren.characters.Vivian;
import ShadowSiren.oldStuff.cards.abstractCards.AbstractSmokeCard;
import ShadowSiren.oldStuff.cards.dualityCards.smoke.SmokeBombDual;
import ShadowSiren.stances.SmokeStance;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class SmokeBomb extends AbstractSmokeCard {

    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(SmokeBomb.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderSkill.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final AbstractCard.CardRarity RARITY = CardRarity.UNCOMMON;
    private static final AbstractCard.CardTarget TARGET = CardTarget.SELF;
    private static final AbstractCard.CardType TYPE = CardType.SKILL;
    public static final AbstractCard.CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 1;
    private static final int BLOCK = 8;

    // /STAT DECLARATION/


    public SmokeBomb() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET, new SmokeBombDual());
        block = baseBlock = BLOCK;
        exhaust = true;
        CardModifierManager.addModifier(this, new BideModifier(1, 0, 3, 0));
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ChangeStanceAction(new SmokeStance()));
        this.addToBot(new GainBlockAction(p, block));
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
