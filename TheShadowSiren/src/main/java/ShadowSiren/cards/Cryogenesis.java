package ShadowSiren.cards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cards.abstractCards.AbstractElectricCard;
import ShadowSiren.cards.abstractCards.AbstractIceCard;
import ShadowSiren.characters.Vivian;
import ShadowSiren.damageModifiers.AbstractVivianDamageModifier;
import ShadowSiren.powers.CryogenesisPower;
import ShadowSiren.powers.PiezoCapacitorPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class Cryogenesis extends AbstractIceCard {

    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(Cryogenesis.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderPower.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 2;
    private static final int UPGRADE_COST = 1;
    private static final int BLOCK_BOOST = 3;
    private static final int UPGRADE_PLUS_BLOCK_BOOST = 2;

    // /STAT DECLARATION/


    public Cryogenesis() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET, AbstractVivianDamageModifier.TipType.BLOCK);
        magicNumber = baseMagicNumber = BLOCK_BOOST;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ApplyPowerAction(p, p, new CryogenesisPower(p, magicNumber)));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADE_COST);
            initializeDescription();
        }
    }
}