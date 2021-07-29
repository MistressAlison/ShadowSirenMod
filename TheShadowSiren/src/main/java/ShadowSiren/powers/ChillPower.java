package ShadowSiren.powers;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.relics.DataCollector;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.blue.Chill;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.FrostOrbActivateEffect;

public class ChillPower extends AbstractPower implements CloneablePowerInterface {

    public static final String POWER_ID = ShadowSirenMod.makeID("ChillPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public static final int REDUCTION_PERCENT = 1;
    public static final int ROLLOVER_STACK = 50;
    private static final float DECAY_RATE = 1/3f;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    //private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("placeholder_power84.png"));
    //private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("placeholder_power32.png"));

    //TODO its OP, lol
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
        if (AbstractDungeon.player.hasRelic(DataCollector.ID)) {
            DataCollector dataCollector = (DataCollector) AbstractDungeon.player.getRelic(DataCollector.ID);
            dataCollector.onApplyChill(amount);
        }
        //freezeCheck();
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        if (AbstractDungeon.player.hasRelic(DataCollector.ID)) {
            DataCollector dataCollector = (DataCollector) AbstractDungeon.player.getRelic(DataCollector.ID);
            dataCollector.onApplyChill(amount);
        }
        //freezeCheck();
    }

    public void freezeCheck() {
        this.addToTop(new AbstractGameAction() {
            @Override
            public void update() {
                if (owner.getPower(ChillPower.POWER_ID).amount >= ROLLOVER_STACK) {
                    int effect = amount / ROLLOVER_STACK;
                    this.addToTop(new ReducePowerAction(owner, owner, ChillPower.this, effect*ROLLOVER_STACK));
                    this.addToTop(new ApplyPowerAction(owner, owner, new FreezePower((AbstractMonster) owner, effect)));
                    if (AbstractDungeon.player.hasRelic(DataCollector.ID)) {
                        DataCollector dataCollector = (DataCollector) AbstractDungeon.player.getRelic(DataCollector.ID);
                        dataCollector.onChillRollover(effect);
                    }
                }
                this.isDone = true;
            }
        });
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        if (type == DamageInfo.DamageType.NORMAL) {
            float multiplier = REDUCTION_PERCENT/100f;
            return Math.max(0, damage * (1 - (multiplier * this.amount)));
        } else {
            return damage;
        }
    }

    @Override
    public void atEndOfRound() {
        this.flashWithoutSound();
        if (this.amount <= 1) {
            this.addToTop(new RemoveSpecificPowerAction(owner, owner, this));
        } else {
            int reduce = Math.max(1, (int)(this.amount * DECAY_RATE));
            this.addToTop(new ReducePowerAction(owner, owner, this, reduce));
            updateDescription();
        }
        //if (!attackedThisTurn) {
        //}
        //attackedThisTurn = false;
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + (REDUCTION_PERCENT*amount) + DESCRIPTIONS[1];
    }

    @Override
    public AbstractPower makeCopy() {
        return new ChillPower(owner, amount);
    }
}
