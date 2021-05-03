package ShadowSiren.powers;

import ShadowSiren.ShadowSirenMod;
import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class ElementalEquilibriumPower extends AbstractPower implements CloneablePowerInterface {

    public static final String POWER_ID = ShadowSirenMod.makeID("ElementalEquilibriumPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final int BURN_CHANCE = 50;
    private static final int BURN_AMOUNT = 6;
    private static final int CHARGE_CHANCE = 25;
    private static final int CHARGE_AMOUNT = 1;
    private static final int ELECTRIC_CHANCE = 10;
    private static final int ELECTRIC_AMOUNT = 1;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    //private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("placeholder_power84.png"));
    //private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("placeholder_power32.png"));

    //TODO this entire thing is awful
    public ElementalEquilibriumPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;

        this.type = PowerType.BUFF;
        this.isTurnBased = false;

        // We load those txtures here.
        this.loadRegion("loop");
        //this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        //this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        super.onApplyPower(power, target, source);
        if (power instanceof FreezePower) {
            int val = (amount * BURN_CHANCE) / 100; //integer division will get us the quotient
            //Modulo random chance
            if (AbstractDungeon.cardRandomRng.random(100) < (amount * BURN_CHANCE) % 100) {
                val++;
            }
            //If we passed the random check and/or the quotient was 1 or more, apply Burn
            if (val > 0) {
                //We use val in this case so that 150% chance to apply means a 50% chance to apply twice, but always apply at least once.
                this.addToTop(new ApplyPowerAction(target, owner, new BurnPower(target, owner, BURN_AMOUNT * val)));
                flash();
            }
        } else if (power instanceof BurnPower) {
            int val = (amount * CHARGE_CHANCE) / 100; //integer division will get us the quotient
            //Modulo random chance
            if (AbstractDungeon.cardRandomRng.random(100) < (amount * CHARGE_CHANCE) % 100) {
                val++;
            }
            //If we passed the random check and/or the quotient was 1 or more, apply Charge
            if (val > 0) {
                //We use val in this case so that 150% chance to apply means a 50% chance to apply twice, but always apply at least once.
                this.addToTop(new ApplyPowerAction(owner, owner, new ChargePower(owner, CHARGE_AMOUNT * val)));
                flash();
            }
        } else if (power instanceof ChargePower) {
            int val = (amount * ELECTRIC_CHANCE) / 100; //integer division will get us the quotient
            //Modulo random chance
            if (AbstractDungeon.cardRandomRng.random(100) < (amount * ELECTRIC_CHANCE) % 100) {
                val++;
            }
            //If we passed the random check and/or the quotient was 1 or more, apply Charge
            if (val > 0) {
                //We use val in this case so that 150% chance to apply means a 50% chance to apply twice, but always apply at least once.
                this.addToTop(new ApplyPowerAction(owner, owner, new ElectricPower(owner, ELECTRIC_AMOUNT * val)));
                flash();
            }
        }
    }

    @Override
    public void updateDescription() {
        description =
                DESCRIPTIONS[0] + (amount * BURN_CHANCE) + DESCRIPTIONS[1] + BURN_AMOUNT +
                DESCRIPTIONS[2] + (amount * CHARGE_CHANCE) + DESCRIPTIONS[3] + CHARGE_AMOUNT +
                DESCRIPTIONS[4] + (amount * ELECTRIC_CHANCE) + DESCRIPTIONS[5] + ELECTRIC_AMOUNT + DESCRIPTIONS[6];
    }

    @Override
    public AbstractPower makeCopy() {
        return new ElementalEquilibriumPower(owner, amount);
    }
}