package ShadowSiren.cards;

import IconsAddon.util.BlockModifierManager;
import ShadowSiren.ShadowSirenMod;
import ShadowSiren.blockTypes.ElectricBlock;
import ShadowSiren.blockTypes.FireBlock;
import ShadowSiren.cards.abstractCards.AbstractElectricCard;
import ShadowSiren.cards.abstractCards.AbstractFireCard;
import ShadowSiren.characters.Vivian;
import ShadowSiren.damageModifiers.AbstractVivianDamageModifier;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class StaticBarrier extends AbstractElectricCard {


    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(StaticBarrier.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderSkill.png");
    // Setting the image as as easy as can possibly be now. You just need to provide the image name

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 2;
    private static final int BLOCK = 10;
    private static final int UPGRADE_PLUS_BLOCK = 4;

    // /STAT DECLARATION/

    public StaticBarrier() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET, AbstractVivianDamageModifier.TipType.BLOCK);
        baseBlock = block = BLOCK;
        BlockModifierManager.addModifier(this, new ElectricBlock());
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new GainBlockAction(p, block));
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
