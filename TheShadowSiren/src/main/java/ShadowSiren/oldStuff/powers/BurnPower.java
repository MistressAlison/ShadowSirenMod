package ShadowSiren.oldStuff.powers;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.actions.BurnToAshEffect;
import ShadowSiren.relics.DataCollector;
import basemod.ReflectionHacks;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.HealthBarRenderPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;

public class BurnPower extends AbstractPower implements CloneablePowerInterface, HealthBarRenderPower {

    public static final String POWER_ID = ShadowSirenMod.makeID("BurnPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private final AbstractCreature source;
    private static final float DECAY_RATE = 1/3f;
    private static final int DETONATE_MULTIPLIER = 3;
    private final Color hpBarColor = new Color(-2686721);

    //private boolean attackedThisTurn;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    //private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("placeholder_power84.png"));
    //private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("placeholder_power32.png"));

    public BurnPower(AbstractCreature owner, AbstractCreature source, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.source = source;

        this.type = PowerType.DEBUFF;
        this.isTurnBased = true;

        // We load those txtures here.
        //this.loadRegion("cExplosion");
        this.loadRegion("explosive");
        //this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        //this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void onInitialApplication() {
        super.onInitialApplication();
        //drenchInteraction();
        if (AbstractDungeon.player.hasRelic(DataCollector.ID) && source == AbstractDungeon.player) {
            DataCollector dataCollector = (DataCollector) AbstractDungeon.player.getRelic(DataCollector.ID);
            dataCollector.onApplyBurn(this.amount);
        }
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        //drenchInteraction();
        if (AbstractDungeon.player.hasRelic(DataCollector.ID) && source == AbstractDungeon.player) {
            DataCollector dataCollector = (DataCollector) AbstractDungeon.player.getRelic(DataCollector.ID);
            dataCollector.onApplyBurn(stackAmount);
        }
    }

    /*
    private void drenchInteraction() {
        if (owner.hasPower(DrenchPower.POWER_ID)) {
            DrenchPower drench = (DrenchPower) owner.getPower(DrenchPower.POWER_ID);
            boolean equal = drench.amount == this.amount;
            int limit = Math.min(drench.amount, this.amount);
            this.flashWithoutSound();
            drench.flashWithoutSound();
            for (int i = 0; i < limit; i++) {
                if (AbstractDungeon.player.hasRelic(DataCollector.ID) && source == AbstractDungeon.player) {
                    DataCollector dataCollector = (DataCollector) AbstractDungeon.player.getRelic(DataCollector.ID);
                    dataCollector.onBurnDamage(amount);
                }
                this.addToTop(new DamageAction(owner, new DamageInfo(source, amount, DamageInfo.DamageType.HP_LOSS), AbstractGameAction.AttackEffect.FIRE, true));
            }
            if (equal) {
                this.addToTop(new RemoveSpecificPowerAction(owner, owner, this));
                this.addToTop(new RemoveSpecificPowerAction(owner, owner, drench));
            } else {
                if (limit == this.amount) {
                    this.addToTop(new RemoveSpecificPowerAction(owner, owner, this));
                    this.addToTop(new ReducePowerAction(owner, owner, drench, limit));
                } else {
                    this.addToTop(new RemoveSpecificPowerAction(owner, owner, drench));
                    this.addToTop(new ReducePowerAction(owner, owner, this, limit));
                    updateDescription();
                }
            }
        }
    }*/

    public void detonate() {
        this.flashWithoutSound();
        if (AbstractDungeon.player.hasRelic(DataCollector.ID) && source == AbstractDungeon.player) {
            DataCollector dataCollector = (DataCollector) AbstractDungeon.player.getRelic(DataCollector.ID);
            dataCollector.onBurnDamage(amount*2);
        }
        this.addToBot(new VFXAction(new ExplosionSmallEffect(owner.hb.cX, owner.hb.cY), 0.1F));
        this.addToBot(new SFXAction("GHOST_ORB_IGNITE_2", 0.3F));
        this.addToBot(new SFXAction("GHOST_ORB_IGNITE_1", 0.3F));
        for (int i = 0 ; i < DETONATE_MULTIPLIER ; i++) {
            triggerEffect();
        }
        this.addToBot(new RemoveSpecificPowerAction(owner, owner, this));
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        triggerEffect();
    }

    public void triggerEffect() {
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && !AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            this.flash();
            this.addToTop(new DamageAction(owner, new DamageInfo(source, amount, DamageInfo.DamageType.HP_LOSS), AbstractGameAction.AttackEffect.NONE, true));
            if (AbstractDungeon.player.hasRelic(DataCollector.ID) && source == AbstractDungeon.player) {
                DataCollector dataCollector = (DataCollector) AbstractDungeon.player.getRelic(DataCollector.ID);
                dataCollector.onBurnDamage(amount);
            }
            //attackedThisTurn = true;
        }
    }

    /*@Override
    public boolean onReceivePower(AbstractPower abstractPower, AbstractCreature target, AbstractCreature source) {
        //If the new debuff is Drench, we need to set a delayed interaction (so it actually has time to apply)
        if (abstractPower instanceof DrenchPower) {
            this.addToTop(new AbstractGameAction() {
                @Override
                public void update() {
                    drenchInteraction();
                    this.isDone = true;
                }
            });
        }
        return true;
    }*/

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
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    @Override
    public AbstractPower makeCopy() {
        return new BurnPower(owner, source, amount);
    }

    @Override
    public int getHealthBarAmount() {
        int amt = amount;
        if (owner instanceof AbstractMonster) {
            if (((AbstractMonster) owner).getIntentBaseDmg() >= 0) {
                if (ReflectionHacks.getPrivate(owner, AbstractMonster.class, "isMultiDmg")) {
                    amt *= ReflectionHacks.<Integer>getPrivate(owner, AbstractMonster.class, "intentMultiAmt");
                }
            } else {
                amt = 0;
            }
        }
        return amt;
    }

    @Override
    public Color getColor() {
        return hpBarColor;
    }

    @Override
    public void onDeath() {
        AbstractDungeon.effectsQueue.add(new ExplosionSmallEffect(owner.hb.cX, owner.hb.cY));
        AbstractDungeon.effectsQueue.add(new BurnToAshEffect(owner.hb.cX, owner.hb.cY));
    }
}