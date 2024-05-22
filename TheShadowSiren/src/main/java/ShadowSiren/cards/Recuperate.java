package ShadowSiren.cards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.actions.ReduceRandomCardToZeroForTurnAction;
import ShadowSiren.blockTypes.ShadowBlock;
import ShadowSiren.cards.abstractCards.AbstractShadowCard;
import ShadowSiren.characters.Vivian;
import ShadowSiren.damageModifiers.AbstractVivianDamageModifier;
import com.evacipated.cardcrawl.mod.stslib.blockmods.BlockModifierManager;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class Recuperate extends AbstractShadowCard {

    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(Recuperate.class.getSimpleName());
    public static final String IMG = makeCardPath("Recuperate.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final AbstractCard.CardRarity RARITY = CardRarity.UNCOMMON;
    private static final AbstractCard.CardTarget TARGET = CardTarget.SELF;
    private static final AbstractCard.CardType TYPE = CardType.SKILL;
    public static final AbstractCard.CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 1;
    private static final int BLOCK = 6;
    private static final int UPGRADE_PLUS_BLOCK = 3;
    private static final int CARDS = 1;

    // /STAT DECLARATION/


    public Recuperate() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET, AbstractVivianDamageModifier.TipType.BLOCK);
        block = baseBlock = BLOCK;
        magicNumber = baseMagicNumber = CARDS;
        BlockModifierManager.addModifier(this, new ShadowBlock());
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new GainBlockAction(p, block));
        this.addToBot(new ReduceRandomCardToZeroForTurnAction(p.hand));
//        int cleansed = 0;
//        for (AbstractPower pow : p.powers) {
//            if (pow.type == AbstractPower.PowerType.DEBUFF && !(pow instanceof InvisiblePower)) {
//                cleansed++;
//                this.addToBot(new RemoveSpecificPowerAction(p, p, pow));
//                if (cleansed == magicNumber) {
//                    break;
//                }
//            }
//        }
//        if (cleansed < magicNumber) {
//            this.addToBot(new DrawCardAction(magicNumber-cleansed));
//        }
    }
//
//    private boolean hasDebuff() {
//        for (AbstractPower pow : AbstractDungeon.player.powers) {
//            if (pow.type == AbstractPower.PowerType.DEBUFF) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public void triggerOnGlowCheck() {
//        super.triggerOnGlowCheck();
//        if (hasDebuff()) {
//            this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
//        } else {
//            this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
//        }
//    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_PLUS_BLOCK);
            initializeDescription();
        }
    }

//    @Override
//    public void changeDescription() {
//        if (DESCRIPTION != null) {
//            if (magicNumber > 1) {
//                rawDescription = UPGRADE_DESCRIPTION;
//            } else {
//                rawDescription = DESCRIPTION;
//            }
//        }
//    }
}