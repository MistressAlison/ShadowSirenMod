package ShadowSiren.powers;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.characters.Vivian;
import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.SanctityEffect;

public class VeilPower extends AbstractPower implements CloneablePowerInterface {

    public static final String POWER_ID = ShadowSirenMod.makeID("VeilPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    //private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("placeholder_power84.png"));
    //private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("placeholder_power32.png"));

    public VeilPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.priority = 100;

        this.type = AbstractPower.PowerType.BUFF;
        this.isTurnBased = true;

        // We load those txtures here.
        this.loadRegion("modeShift");
        //this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        //this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void onInitialApplication() {
        if (owner instanceof Vivian) {
            ((Vivian) owner).playAnimation("hide");
        }
    }

    @Override
    public void onRemove() {
        if (owner instanceof Vivian) {
            this.addToTop(new VFXAction(new SanctityEffect(owner.hb.cX, owner.hb.cY), 0.4F));
            ((Vivian) owner).playAnimation("happy");
        }
    }

    @Override
    public void atEndOfRound() {
        if (amount == 1) {
            this.addToTop(new RemoveSpecificPowerAction(owner, owner, this));
        } else {
            this.addToTop(new ReducePowerAction(owner, owner, this, 1));
        }

    }

    public float atDamageReceive(float damage, DamageInfo.DamageType type) {
        return damage * 0.5f;
    }

    @Override
    public void updateDescription() {
        if (amount == 1) {
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
        } else {
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[2];
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new VeilPower(owner, amount);
    }
}
