package ShadowSiren.cards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cardModifiers.BideModifier;
import ShadowSiren.cards.abstractCards.AbstractShadowCard;
import ShadowSiren.cards.dualityCards.veil.SpookDual;
import ShadowSiren.characters.Vivian;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
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
    private static final int BLOCK = 5;
    private static final int UPGRADE_PLUS_BLOCK = 2;
    private static final int VULN = 1;
    private static final int UPGRADE_PLUS_VURN = 1;
    private static final int BPD = 2;
    private static final int UPGRADE_PLUS_BPD = 1;

    // /STAT DECLARATION/


    public Spook() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET, new SpookDual());
        block = baseBlock = 0;
        magicNumber = baseMagicNumber = VULN;
        secondMagicNumber = baseSecondMagicNumber = BPD;
        //CardModifierManager.addModifier(this, new BideModifier(1, 0, 3, 1, 1, 0));
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        //Block
        this.addToBot(new GainBlockAction(p, block));
        //Single vuln
        this.addToBot(new ApplyPowerAction(m, p, new VulnerablePower(m, magicNumber, false)));

        /*this.addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                int amount = block;
                for (AbstractPower pow : m.powers) {
                    if (pow.type == AbstractPower.PowerType.DEBUFF) {
                        amount += secondMagicNumber;
                    }
                }
                this.addToTop(new GainBlockAction(p, p, amount));
                this.isDone = true;
            }
        });*/
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        super.calculateCardDamage(mo);
        for (AbstractPower pow : mo.powers) {
            if (pow.type == AbstractPower.PowerType.DEBUFF) {
                block += secondMagicNumber;
            }
        }
        isBlockModified = block != baseBlock;
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_VURN);
            //upgradeBlock(UPGRADE_PLUS_BLOCK);
            upgradeSecondMagicNumber(UPGRADE_PLUS_BPD);
            initializeDescription();
            super.upgrade();
        }
    }
}