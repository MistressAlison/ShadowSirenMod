package ShadowSiren.cards;

import IconsAddon.util.BlockModifierManager;
import ShadowSiren.ShadowSirenMod;
import ShadowSiren.blockTypes.ShadowBlock;
import ShadowSiren.cards.abstractCards.AbstractShadowCard;
import ShadowSiren.characters.Vivian;
import ShadowSiren.damageModifiers.AbstractVivianDamageModifier;
import ShadowSiren.util.XCostGrabber;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.LoseDexterityPower;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class ArcaneShelter extends AbstractShadowCard {

    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(ArcaneShelter.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderSkill.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = -1;
    private static final int BLOCK = 5;
    private static final int UPGRADE_PLUS_BLOCK = 2;
    private static final int BASE_EFFECT = 0;
    private static final int DEX = 1;

    // /STAT DECLARATION/

    public ArcaneShelter() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET, AbstractVivianDamageModifier.TipType.BLOCK);
        secondMagicNumber = baseSecondMagicNumber = thirdMagicNumber = baseThirdMagicNumber = BASE_EFFECT;
        block = baseBlock = BLOCK;
        magicNumber = baseMagicNumber = DEX;
        BlockModifierManager.addModifier(this, new ShadowBlock());
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int effect = XCostGrabber.getXCostAmount(this);

        if (effect > 0) {
            for (int i = 0 ; i < effect ; i++) {
                this.addToBot(new GainBlockAction(p, block));
            }
            this.addToBot(new ApplyPowerAction(p, p, new DexterityPower(p, effect), effect));
            this.addToBot(new ApplyPowerAction(p, p, new LoseDexterityPower(p, effect), effect));
        }

        if (!this.freeToPlayOnce) {
            p.energy.use(EnergyPanel.totalCount);
        }
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        updateExtraValues();
        initializeDescription();
    }

    private void updateExtraValues() {
        secondMagicNumber = XCostGrabber.getXCostAmount(this, true) * block;
        isSecondMagicNumberModified = secondMagicNumber != baseSecondMagicNumber;
        thirdMagicNumber = XCostGrabber.getXCostAmount(this, true);
        isThirdMagicNumberModified = thirdMagicNumber != baseThirdMagicNumber;
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_PLUS_BLOCK);
            initializeDescription();
        }
    }
}