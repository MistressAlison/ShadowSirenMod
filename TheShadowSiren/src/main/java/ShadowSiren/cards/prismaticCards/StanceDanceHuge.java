package ShadowSiren.cards.prismaticCards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.actions.EnterAnyStanceAction;
import ShadowSiren.cards.abstractCards.prismatics.AbstractPrismaticHugeCard;
import ShadowSiren.cards.abstractCards.prismatics.AbstractPrismaticHyperCard;
import ShadowSiren.cards.uniqueCards.UniqueCard;
import ShadowSiren.characters.Vivian;
import ShadowSiren.patches.StanceCounterPatches;
import ShadowSiren.powers.ElectricPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class StanceDanceHuge extends AbstractPrismaticHugeCard implements UniqueCard {

    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(StanceDanceHuge.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderAttack.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final AbstractCard.CardRarity RARITY = CardRarity.RARE;
    private static final AbstractCard.CardTarget TARGET = CardTarget.ENEMY;
    private static final AbstractCard.CardType TYPE = CardType.ATTACK;
    public static final AbstractCard.CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 1;
    private static final int DAMAGE = 2;
    private static final int UPGRADE_PLUS_DAMAGE = 1;


    // /STAT DECLARATION/


    public StanceDanceHuge() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        damage = baseDamage = DAMAGE;
        secondMagicNumber = baseSecondMagicNumber = 0;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new EnterAnyStanceAction());
        if (secondMagicNumber > 0) {
            this.addToBot(new DamageAction(m, new DamageInfo(p, damage*secondMagicNumber, damageTypeForTurn), AbstractGameAction.AttackEffect.SMASH));
        }
    }

    @Override
    protected void applyPowersToBlock() {
        super.applyPowersToBlock();
        secondMagicNumber = StanceCounterPatches.getCombatUniqueStances(AbstractDungeon.player);
        isSecondMagicNumberModified = secondMagicNumber != baseSecondMagicNumber;
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DAMAGE);
            initializeDescription();
            super.upgrade();
        }
    }
}