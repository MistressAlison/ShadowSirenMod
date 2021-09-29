package ShadowSiren.cards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cards.abstractCards.AbstractIceCard;
import ShadowSiren.cards.tempCards.IceShard;
import ShadowSiren.characters.Vivian;
import ShadowSiren.damageModifiers.AbstractVivianDamageModifier;
import ShadowSiren.powers.ChillPower;
import ShadowSiren.util.XCostGrabber;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class FlashFreeze extends AbstractIceCard {


    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(FlashFreeze.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderSkill.png");
    // Setting the image as as easy as can possibly be now. You just need to provide the image name

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = -1;
    private static final int BASE_EFFECT = 0;
    private static final int CHILL = 2;
    private static final int UPGRADE_PLUS_CHILL = 1;

    // /STAT DECLARATION/

    public FlashFreeze() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET, AbstractVivianDamageModifier.TipType.DAMAGE_AND_BLOCK);
//        secondMagicNumber = baseSecondMagicNumber = thirdMagicNumber = baseThirdMagicNumber = BASE_EFFECT;
        magicNumber = baseMagicNumber = CHILL;
        this.cardsToPreview = new IceShard();
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int effect = XCostGrabber.getXCostAmount(this);

        if (effect > 0) {
            AbstractDungeon.effectsQueue.add(new BorderFlashEffect(Color.WHITE.cpy(), true));
            for (int i = 0 ; i < effect ; i++) {
                for (AbstractMonster aM : AbstractDungeon.getMonsters().monsters) {
                    if (!aM.isDeadOrEscaped()) {
                        this.addToBot(new ApplyPowerAction(aM, p, new ChillPower(aM, magicNumber), magicNumber, true));
                        aM.tint.color = Color.BLUE.cpy();
                        aM.tint.changeColor(Color.WHITE.cpy());
                    }
                }
            }
            this.addToBot(new MakeTempCardInHandAction(cardsToPreview.makeStatEquivalentCopy(), effect));
        }

        if (!this.freeToPlayOnce) {
            p.energy.use(EnergyPanel.totalCount);
        }
    }

//    @Override
//    public void applyPowers() {
//        super.applyPowers();
//        updateExtraValues();
//        initializeDescription();
//    }
//
//    @Override
//    public void calculateCardDamage(AbstractMonster mo) {
//        super.calculateCardDamage(mo);
//        updateExtraValues();
//        initializeDescription();
//    }
//
//    private void updateExtraValues() {
//        secondMagicNumber = XCostGrabber.getXCostAmount(this, true) * magicNumber;
//        isSecondMagicNumberModified = secondMagicNumber != baseSecondMagicNumber;
//        thirdMagicNumber = XCostGrabber.getXCostAmount(this, true);
//        isThirdMagicNumberModified = thirdMagicNumber != baseThirdMagicNumber;
//    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            //upgradeMagicNumber(UPGRADE_PLUS_CHILL);
            cardsToPreview.upgrade();
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
