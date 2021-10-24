package ShadowSiren.oldStuff.cards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cards.interfaces.ChargeMagicBuff;
import ShadowSiren.characters.Vivian;
import ShadowSiren.oldStuff.cards.abstractCards.AbstractHyperCard;
import ShadowSiren.oldStuff.cards.dualityCards.hyper.UkuleleDual;
import ShadowSiren.oldStuff.powers.ElectricPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class Ukulele extends AbstractHyperCard implements ChargeMagicBuff {

    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(Ukulele.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderPower.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 2;

    private static final int EFFECT = 2;
    private static final int UPGRADE_PLUS_EFFECT = 1;

    // /STAT DECLARATION/


    public Ukulele() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET, new UkuleleDual());
        magicNumber = baseMagicNumber = EFFECT;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ApplyPowerAction(p, p, new ElectricPower(p, magicNumber)));
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