package ShadowSiren.powers;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cards.interfaces.ChargeMagicBuff;
import ShadowSiren.cards.interfaces.ChargeMultiEffect;
import ShadowSiren.patches.ChargeCounterPatches;
import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.colorless.RitualDagger;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.RitualPower;

public class ChargePower extends AbstractPower implements CloneablePowerInterface {

    public static final String POWER_ID = ShadowSirenMod.makeID("ChargePower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    //private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("placeholder_power84.png"));
    //private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("placeholder_power32.png"));

    public ChargePower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;

        this.type = PowerType.BUFF;
        this.isTurnBased = false;

        // We load those txtures here.
        this.loadRegion("static_discharge");
        //this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        //this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public float modifyBlock(float blockAmount, AbstractCard card) {
        if (card instanceof RitualDagger) {
            return super.modifyBlock(blockAmount, card);
        }
        /*if (card instanceof ChargeMagicBuff) {
            card.magicNumber = card.baseMagicNumber + amount;
            card.isMagicNumberModified = card.magicNumber != card.baseMagicNumber;
            card.initializeDescription();
        }*/
        return blockAmount * calcMainEffect(card);
    }

    @Override
    public float atDamageFinalGive(float damage, DamageInfo.DamageType type, AbstractCard card) {
        if (type != DamageInfo.DamageType.NORMAL) {
            return super.atDamageFinalGive(damage, type, card);
        }
        return damage * calcMainEffect(card);
    }

    private float calcMainEffect(AbstractCard card) {
        return 1 + ((card instanceof ChargeMultiEffect ? ((ChargeMultiEffect) card).getChargeMultiplier() : 1) * 0.5f) * amount;
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.baseBlock > 0 || card.type == AbstractCard.CardType.ATTACK || card instanceof ChargeMagicBuff) {
            this.flash();
            this.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
        }
    }

    @Override
    public void onInitialApplication() {
        super.onInitialApplication();
        this.addToTop(new SFXAction("ORB_LIGHTNING_CHANNEL", 0.1F));
        if (this.owner instanceof AbstractPlayer) {
            ChargeCounterPatches.incrementChargesThisCombat((AbstractPlayer) this.owner, this.amount);
        }
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        this.addToTop(new SFXAction("ORB_LIGHTNING_CHANNEL", 0.1F));
        if (this.owner instanceof AbstractPlayer) {
            ChargeCounterPatches.incrementChargesThisCombat((AbstractPlayer) this.owner, stackAmount);
        }
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + (amount*50) + DESCRIPTIONS[1];
    }

    @Override
    public AbstractPower makeCopy() {
        return new ChargePower(owner, amount);
    }
}
