package ShadowSiren.cards.dualityCards.veil;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cards.abstractCards.AbstractShadowCard;
import ShadowSiren.cards.interfaces.ChargeMagicBuff;
import ShadowSiren.cards.uniqueCards.UniqueCard;
import ShadowSiren.characters.Vivian;
import ShadowSiren.powers.ElectricPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class UkuleleDual extends AbstractShadowCard implements ChargeMagicBuff, UniqueCard {

    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(UkuleleDual.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderPower.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 2;

    private static final int EFFECT = 3;
    private static final int UPGRADE_PLUS_EFFECT = 1;

    // /STAT DECLARATION/


    public UkuleleDual() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
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