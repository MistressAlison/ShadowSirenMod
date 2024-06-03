package ShadowSiren.powers;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cardModifiers.ChargeModifier;
import ShadowSiren.cards.interfaces.ChargeMultiEffect;
import ShadowSiren.patches.ChargeCounterPatches;
import basemod.helpers.CardModifierManager;
import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.colorless.RitualDagger;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

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
    public float atDamageGive(float damage, DamageInfo.DamageType type, AbstractCard card) {
        return damage + (int)(amount * (card instanceof ChargeMultiEffect ? ((ChargeMultiEffect) card).getChargeMultiplier() : 1));
    }

    @Override
    public float modifyBlock(float blockAmount, AbstractCard card) {
        return blockAmount + (int)(amount * (card instanceof ChargeMultiEffect ? ((ChargeMultiEffect) card).getChargeMultiplier() : 1));
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        boolean activated = (card.baseDamage >= 0) || (card.baseBlock >= 0 && !(card instanceof RitualDagger));
        if (activated) {
            this.addToBot(new AbstractGameAction() {
                @Override
                public void update() {
                    int effect = (int)(ChargePower.this.amount * (card instanceof ChargeMultiEffect ? ((ChargeMultiEffect) card).getChargeMultiplier() : 1));
                    CardModifierManager.addModifier(card, new ChargeModifier(effect));
                    this.isDone = true;
                }
            });
            this.addToBot(new RemoveSpecificPowerAction(owner, owner, this));
            card.applyPowers();
            card.superFlash();
        }
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    @Override
    public void playApplyPowerSfx() {
        this.addToTop(new SFXAction("ORB_LIGHTNING_CHANNEL", 0.1F));
    }

    @Override
    public void onInitialApplication() {
        super.onInitialApplication();
        if (this.owner instanceof AbstractPlayer) {
            ChargeCounterPatches.incrementChargesThisCombat((AbstractPlayer) this.owner, this.amount);
        }
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        if (this.owner instanceof AbstractPlayer) {
            ChargeCounterPatches.incrementChargesThisCombat((AbstractPlayer) this.owner, stackAmount);
        }
    }

    /*@Override
    public float modifyBlock(float blockAmount, AbstractCard card) {
        if (card instanceof RitualDagger) {
            return super.modifyBlock(blockAmount, card);
        }
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
        return 1 + ((card instanceof ChargeMultiEffect ? ((ChargeMultiEffect) card).getChargeMultiplier() : 1) * EFFECT_PERCENT/100f);
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.baseBlock > 0 || card.type == AbstractCard.CardType.ATTACK || card instanceof ChargeMagicBuff) {
            this.flash();
            if (this.amount == 1) {
                this.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
            } else {
                this.addToTop(new ReducePowerAction(this.owner, this.owner, this, 1));
            }
        }
    }

    @Override
    public void updateDescription() {
        if (amount == 1) {
            description = DESCRIPTIONS[0] + EFFECT_PERCENT + DESCRIPTIONS[1];
        } else {
            description = DESCRIPTIONS[2] + amount + DESCRIPTIONS[3] + EFFECT_PERCENT + DESCRIPTIONS[1];
        }
    }*/

    @Override
    public AbstractPower makeCopy() {
        return new ChargePower(owner, amount);
    }
}
