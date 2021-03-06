package ShadowSiren.oldStuff.cards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.characters.Vivian;
import ShadowSiren.oldStuff.cards.abstractCards.AbstractShadowCard;
import ShadowSiren.oldStuff.cards.dualityCards.veil.SpookDual;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class Spook extends AbstractShadowCard {

    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(Spook.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderSkill.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final AbstractCard.CardRarity RARITY = CardRarity.COMMON;
    private static final AbstractCard.CardTarget TARGET = CardTarget.SELF_AND_ENEMY;
    private static final AbstractCard.CardType TYPE = CardType.SKILL;
    public static final AbstractCard.CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 1;
    private static final int BLOCK = 6;
    private static final int UPGRADE_PLUS_BLOCK = 2;
    private static final int VULN = 1;
    private static final int UPGRADE_PLUS_VURN = 1;

    // /STAT DECLARATION/


    public Spook() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET, new SpookDual());
        block = baseBlock = BLOCK;
        magicNumber = baseMagicNumber = VULN;
        //CardModifierManager.addModifier(this, new BideModifier(1, 0, 3, 1, 1, 0));
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        //Block
        this.addToBot(new GainBlockAction(p, block));
        //Single vuln
        this.addToBot(new ApplyPowerAction(m, p, new VulnerablePower(m, magicNumber, false)));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_VURN);
            upgradeBlock(UPGRADE_PLUS_BLOCK);
            initializeDescription();
            super.upgrade();
        }
    }
}