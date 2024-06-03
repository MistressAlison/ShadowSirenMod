package ShadowSiren.powers;

import ShadowSiren.ShadowSirenMod;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.FrostOrbActivateEffect;

public class ChillPower extends AbstractPower implements CloneablePowerInterface {

    public static final String POWER_ID = ShadowSirenMod.makeID("ChillPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public static final int REDUCTION_PERCENT = 10;
    public static final int MAX_STACKS = 10;
    private static final float DECAY_RATE = 1/2f;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    //private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("placeholder_power84.png"));
    //private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("placeholder_power32.png"));

    public ChillPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;

        this.type = PowerType.DEBUFF;
        this.isTurnBased = true;
        this.priority = 999;

        // We load those txtures here.
        this.loadRegion("skillBurn");
        //this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        //this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void playApplyPowerSfx() {
        this.addToTop(new SFXAction("ORB_FROST_EVOKE", 0.1F));
        AbstractDungeon.effectsQueue.add(new FrostOrbActivateEffect(owner.hb_x, owner.hb_y));
        owner.tint.color = Color.BLUE.cpy();
        owner.tint.changeColor(Color.WHITE.cpy());
    }

    @Override
    public void onInitialApplication() {
        super.onInitialApplication();
        freezeCheck();
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        freezeCheck();
    }

    public void freezeCheck() {
        if (amount >= MAX_STACKS) {
            int effect = amount / MAX_STACKS;
            this.addToTop(new ReducePowerAction(owner, owner, this, effect * MAX_STACKS));
            //this.addToTop(new ApplyPowerAction(owner, owner, new FrostyPower(owner, effect)));
            this.addToTop(new ApplyPowerAction(owner, owner, new FreezePower(owner, effect)));
        }
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        if (type == DamageInfo.DamageType.NORMAL) {
            return Math.max(0, damage * (1 - (REDUCTION_PERCENT*amount)/100f));
        } else {
            return damage;
        }
    }

    @Override
    public float atDamageReceive(float damage, DamageInfo.DamageType damageType) {
        if (damageType == DamageInfo.DamageType.NORMAL) {
            AbstractPower cryo = AbstractDungeon.player.getPower(CryogenesisPower.POWER_ID);
            if (cryo != null) {
                damage *= (1 + (REDUCTION_PERCENT*amount)/100f);
            }
        }
        return damage;
    }

    @Override
    public void atEndOfRound() {
        this.flashWithoutSound();
        this.addToTop(new RemoveSpecificPowerAction(owner, owner, this));
    }

    public void onIceBlockTrigger(AbstractCreature source) {
        if (amount > 0) {
            this.flash();
            this.addToTop(new DamageAction(owner, new DamageInfo(source, amount, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.NONE, true));
        }
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + REDUCTION_PERCENT*amount + DESCRIPTIONS[1] + MAX_STACKS + DESCRIPTIONS[2];
    }

    @Override
    public AbstractPower makeCopy() {
        return new ChillPower(owner, amount);
    }
}
