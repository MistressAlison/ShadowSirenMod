package ShadowSiren.cards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cards.abstractCards.AbstractDynamicCard;
import ShadowSiren.characters.Vivian;
import ShadowSiren.util.StarBarManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class Appeal extends AbstractDynamicCard {

    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(Appeal.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderSkill.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final AbstractCard.CardRarity RARITY = CardRarity.UNCOMMON;
    private static final AbstractCard.CardTarget TARGET = CardTarget.SELF;
    private static final AbstractCard.CardType TYPE = CardType.SKILL;
    public static final AbstractCard.CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 1;
    private static final int EFFECT = 10;
    private static final int UPGRADE_PLUS_EFFECT = 5;

    // /STAT DECLARATION/


    public Appeal() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = EFFECT;
        secondMagicNumber = baseSecondMagicNumber = EFFECT * 10;
        exhaust = true;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                StarBarManager.addProgress(magicNumber);
                this.isDone = true;
            }
        });
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        setupSecondMagicNumber();
    }

    private void setupSecondMagicNumber() {
        baseSecondMagicNumber = baseMagicNumber * 10;
        secondMagicNumber = magicNumber * 10;
        isSecondMagicNumberModified = baseSecondMagicNumber != secondMagicNumber;
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_EFFECT);
            upgradeSecondMagicNumber(UPGRADE_PLUS_EFFECT*10);
            initializeDescription();
        }
    }
}