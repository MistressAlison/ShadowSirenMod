package ShadowSiren.cards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.blockTypes.FireBlock;
import ShadowSiren.cards.abstractCards.AbstractFireCard;
import ShadowSiren.cards.interfaces.VigorBlockBuff;
import ShadowSiren.cards.tempCards.Ashes;
import ShadowSiren.characters.Vivian;
import ShadowSiren.damageModifiers.AbstractVivianDamageModifier;
import com.evacipated.cardcrawl.mod.stslib.blockmods.BlockModifierManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class Pyre extends AbstractFireCard implements VigorBlockBuff {


    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(Pyre.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderSkill.png");
    // Setting the image as as easy as can possibly be now. You just need to provide the image name

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 0;
    private static final int BLOCK = 5;
    private static final int UPGRADE_PLUS_BLOCK = 3;
    private static final int VIGOR = 4;
    private static final int UPGRADE_PLUS_VIGOR = 2;

    // /STAT DECLARATION/

    public Pyre() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET, AbstractVivianDamageModifier.TipType.BLOCK);
        baseBlock = block = BLOCK;
        magicNumber = baseMagicNumber = VIGOR;
        this.cardsToPreview = new Ashes();
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
        this.addToBot(new ApplyPowerAction(p, p, new VigorPower(p, magicNumber)));
        this.addToBot(new MakeTempCardInDrawPileAction(cardsToPreview, 1, true, true));
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_PLUS_BLOCK);
            initializeDescription();
        }
    }
}
