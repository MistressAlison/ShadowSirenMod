package ShadowSiren.cards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.blockTypes.FireBlock;
import ShadowSiren.cards.abstractCards.AbstractFireCard;
import ShadowSiren.cards.interfaces.ModularDescription;
import ShadowSiren.cards.interfaces.VigorBlockBuff;
import ShadowSiren.characters.Vivian;
import ShadowSiren.damageModifiers.AbstractVivianDamageModifier;
import com.evacipated.cardcrawl.mod.stslib.blockmods.BlockModifierManager;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class FlameShield extends AbstractFireCard implements VigorBlockBuff {


    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(FlameShield.class.getSimpleName());
    public static final String IMG = makeCardPath("FlamingShield.png");
    // Setting the image as as easy as can possibly be now. You just need to provide the image name

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 1;
    private static final int BLOCK = 7;
    private static final int UPGRADE_PLUS_BLOCK = 3;
    private static final int CARDS = 1;
    private static final int UPGRADE_PLUS_CARDS = 1;

    // /STAT DECLARATION/

    public FlameShield() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET, AbstractVivianDamageModifier.TipType.BLOCK);
        baseBlock = block = BLOCK;
        magicNumber = baseMagicNumber = CARDS;
        BlockModifierManager.addModifier(this, new FireBlock());
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int bonus = 0;
        if (p.hasPower(VigorPower.POWER_ID)) {
            bonus += p.getPower(VigorPower.POWER_ID).amount;
        }
        this.addToBot(new GainBlockAction(p, block + bonus));
        this.addToBot(new DrawCardAction(magicNumber));
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_PLUS_BLOCK);
            //upgradeMagicNumber(UPGRADE_PLUS_CARDS);
            initializeDescription();
        }
    }
}
