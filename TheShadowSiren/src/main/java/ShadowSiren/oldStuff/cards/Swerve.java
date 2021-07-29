package ShadowSiren.oldStuff.cards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.oldStuff.cards.abstractCards.AbstractAbyssCard;
import ShadowSiren.oldStuff.cards.dualityCards.abyss.SwerveDual;
import ShadowSiren.characters.Vivian;
import ShadowSiren.powers.DizzyPower;
import ShadowSiren.stances.AbyssStance;
import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class Swerve extends AbstractAbyssCard {

    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(Swerve.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderSkill.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final AbstractCard.CardRarity RARITY = CardRarity.UNCOMMON;
    private static final AbstractCard.CardTarget TARGET = CardTarget.SELF_AND_ENEMY;
    private static final AbstractCard.CardType TYPE = CardType.SKILL;
    public static final AbstractCard.CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 1;
    private static final int BLOCK = 4;
    private static final int UPGRADE_PLUS_BLOCK = 2;
    private static final int DIZZY = 1;
    private static final int UPGRADE_PLUS_DIZZY = 1;

    // /STAT DECLARATION/


    public Swerve() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET, new SwerveDual());
        block = baseBlock = BLOCK;
        magicNumber = baseMagicNumber = DIZZY;
        secondMagicNumber = baseSecondMagicNumber = BLOCK;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ChangeStanceAction(new AbyssStance()));
        this.addToBot(new GainBlockAction(p, secondMagicNumber));
        this.addToBot(new ApplyPowerAction(m, p, new DizzyPower(m, magicNumber)));
    }

    @Override
    protected void applyPowersToBlock() {
        super.applyPowersToBlock();
        secondMagicNumber = block;
        isSecondMagicNumberModified = secondMagicNumber != baseSecondMagicNumber;
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        super.calculateCardDamage(mo);
        int dmg = 0;
        if (!mo.isDeadOrEscaped() && mo.getIntentBaseDmg() > 0) {
            dmg = (int) ReflectionHacks.getPrivate(mo, AbstractMonster.class, "intentDmg");
            if ((boolean)ReflectionHacks.getPrivate(mo, AbstractMonster.class, "isMultiDmg"))
            {
                dmg *= (int)ReflectionHacks.getPrivate(mo, AbstractMonster.class, "intentMultiAmt");
            }
        }
        secondMagicNumber = block + dmg/2;
        isSecondMagicNumberModified = secondMagicNumber != baseSecondMagicNumber;
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_DIZZY);
            upgradeBlock(UPGRADE_PLUS_BLOCK);
            initializeDescription();
            super.upgrade();
        }
    }
}
