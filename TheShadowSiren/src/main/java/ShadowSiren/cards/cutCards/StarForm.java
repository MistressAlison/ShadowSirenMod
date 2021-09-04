package ShadowSiren.cards.cutCards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cards.abstractCards.AbstractDynamicCard;
import ShadowSiren.cards.tempCards.TempCard;
import ShadowSiren.characters.Vivian;
import ShadowSiren.powers.StarFormPower;
import basemod.AutoAdd;
import basemod.helpers.BaseModCardTags;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static ShadowSiren.ShadowSirenMod.makeCardPath;
@AutoAdd.Ignore
public class StarForm extends AbstractDynamicCard implements TempCard {
    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(StarForm.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderPower.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 3;
    private static final int EFFECT = 3;
    private static final int UPGRADE_PLUS_EFFECT = 2;

    // /STAT DECLARATION/


    public StarForm() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = EFFECT;
        //this.tags.add(BaseModCardTags.FORM);
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ApplyPowerAction(p, p, new StarFormPower(p, magicNumber)));
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
}
