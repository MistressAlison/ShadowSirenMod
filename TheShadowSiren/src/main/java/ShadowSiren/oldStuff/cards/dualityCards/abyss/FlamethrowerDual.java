package ShadowSiren.oldStuff.cards.dualityCards.abyss;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cards.uniqueCards.UniqueCard;
import ShadowSiren.characters.Vivian;
import ShadowSiren.oldStuff.cards.abstractCards.AbstractAbyssCard;
import ShadowSiren.powers.ChillPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class FlamethrowerDual extends AbstractAbyssCard implements UniqueCard {

    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(FlamethrowerDual.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderSkill.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final AbstractCard.CardRarity RARITY = CardRarity.UNCOMMON;
    private static final AbstractCard.CardTarget TARGET = CardTarget.NONE;
    private static final AbstractCard.CardType TYPE = CardType.SKILL;
    public static final AbstractCard.CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = -2;
    private static final int WATER = 1;
    private static final int UPGRADE_PLUS_WATER = 1;

    // /STAT DECLARATION/


    public FlamethrowerDual() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = WATER;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {}

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        //this.cantUseMessage = cardStrings.EXTENDED_DESCRIPTION[0];
        return false;
    }

    @Override
    public void onPlayCard(AbstractCard c, AbstractMonster m) {
        super.onPlayCard(c, m);
        if(inHand() && c != this) { //Dont activate when playing itself
            if (m == null) {
                m = AbstractDungeon.getRandomMonster();
            }
            //this.addToBot(new ApplyPowerAction(m, AbstractDungeon.player, new DrenchPower(m, magicNumber), magicNumber, true));
            this.addToBot(new ApplyPowerAction(m, AbstractDungeon.player, new ChillPower(m, magicNumber), magicNumber, true));
        }
    }

    private boolean inHand() {
        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            if (c == this) return true;
        }
        return false;
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_WATER);
            initializeDescription();
            super.upgrade();
        }
    }
}