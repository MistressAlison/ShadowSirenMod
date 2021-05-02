package ShadowSiren.cards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cards.abstractCards.AbstractDynamicCard;
import ShadowSiren.characters.Vivian;
import ShadowSiren.powers.HexingPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class HexBlast extends AbstractDynamicCard {

    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(HexBlast.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderAttack.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 2;
    private static final int DAMAGE = 13;
    private static final int HEX = 5;
    private static final int UPGRADE_PLUS_HEX = 1;
    private static final int HEX_PER_DEBUFF = 3;
    private static final int UPGRADE_PLUS_HEX_PER_DEBUFF = 1;


    // /STAT DECLARATION/


    public HexBlast() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        damage = baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = HEX;
        secondMagicNumber = baseSecondMagicNumber = HEX_PER_DEBUFF;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.LIGHTNING));
        int debuffs = 0;
        for (AbstractPower pow : m.powers) {
            if (pow.type == AbstractPower.PowerType.DEBUFF) {
                debuffs++;
            }
        }
        this.addToBot(new ApplyPowerAction(m, p, new HexingPower(m, p, magicNumber+debuffs*secondMagicNumber)));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_HEX);
            upgradeSecondMagicNumber(UPGRADE_PLUS_HEX_PER_DEBUFF);
            initializeDescription();
        }
    }
}