package ShadowSiren.oldStuff.cards.tempCards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cards.tempCards.TempCard;
import ShadowSiren.characters.Vivian;
import ShadowSiren.oldStuff.cards.abstractCards.AbstractSmokeCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class Ash extends AbstractSmokeCard implements TempCard {

    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(Ash.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderSkill.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final AbstractCard.CardRarity RARITY = CardRarity.COMMON;
    private static final AbstractCard.CardTarget TARGET = CardTarget.NONE;
    private static final AbstractCard.CardType TYPE = CardType.SKILL;
    public static final AbstractCard.CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = -2;

    // /STAT DECLARATION/


    public Ash() {
        this(null);
    }

    public Ash(AbstractCreature cr) {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        exhaust = true;
        isEthereal = true;
        if (cr != null) {
            name = EXTENDED_DESCRIPTION[0] + cr.name + EXTENDED_DESCRIPTION[1];
            //rawDescription = EXTENDED_DESCRIPTION[2] + cr.name + EXTENDED_DESCRIPTION[3];
            //initializeDescription();
        }
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {}

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            initializeDescription();
        }
    }
}
