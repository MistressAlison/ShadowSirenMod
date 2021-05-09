package ShadowSiren.powers;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.actions.BurnToAshEffect;
import ShadowSiren.actions.MakeTempCardInExhaustAction;
import ShadowSiren.cards.tempCards.Ash;
import ShadowSiren.powers.interfaces.OnRemoveOtherPowerPower;
import ShadowSiren.relics.DataCollector;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.HealthBarRenderPower;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnReceivePowerPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.beyond.AwakenedOne;
import com.megacrit.cardcrawl.monsters.beyond.Darkling;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.combat.DeckPoofEffect;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

public class BurnPower extends AbstractPower implements CloneablePowerInterface, HealthBarRenderPower, OnRemoveOtherPowerPower, OnReceivePowerPower {

    public static final String POWER_ID = ShadowSirenMod.makeID("BurnPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private final Color hpBarColor = new Color(-2686721);

    private final AbstractCreature source;
    private int damagePerTurn;
    //public boolean activated;

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
        this.priority = 0;
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
        tallyDebuffs();
        drenchInteraction();
        if (AbstractDungeon.player.hasRelic(DataCollector.ID) && source == AbstractDungeon.player) {
            DataCollector dataCollector = (DataCollector) AbstractDungeon.player.getRelic(DataCollector.ID);
            dataCollector.onApplyBurn(this.amount);
        }
        //deathCheck(0);
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        tallyDebuffs();
        drenchInteraction();
        if (AbstractDungeon.player.hasRelic(DataCollector.ID) && source == AbstractDungeon.player) {
            DataCollector dataCollector = (DataCollector) AbstractDungeon.player.getRelic(DataCollector.ID);
            dataCollector.onApplyBurn(stackAmount);
        }
        //deathCheck(0);
    }

    private void drenchInteraction() {
        if (owner.hasPower(DrenchPower.POWER_ID)) {
            DrenchPower drench = (DrenchPower) owner.getPower(DrenchPower.POWER_ID);
            boolean equal = drench.amount == this.amount;
            int limit = Math.min(drench.amount, this.amount);
            this.flashWithoutSound();
            drench.flashWithoutSound();
            for (int i = 0 ; i < limit ; i++) {
                if (AbstractDungeon.player.hasRelic(DataCollector.ID) && source == AbstractDungeon.player) {
                    DataCollector dataCollector = (DataCollector) AbstractDungeon.player.getRelic(DataCollector.ID);
                    dataCollector.onBurnDamage(damagePerTurn);
                }
                this.addToTop(new DamageAction(owner, new DamageInfo(source, damagePerTurn, DamageInfo.DamageType.HP_LOSS), AbstractGameAction.AttackEffect.FIRE, true));
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
            //Old effect
            /*AbstractDungeon.effectList.add(new FlashAtkImgEffect(owner.hb.cX, owner.hb.cY, AbstractGameAction.AttackEffect.FIRE));
            AbstractDungeon.effectList.add(new ExplosionSmallEffect(owner.hb.cX, owner.hb.cY));
            this.addToTop(new RemoveSpecificPowerAction(owner, owner, DrenchPower.POWER_ID));
            this.addToBot(new DamageAllEnemiesAction(source, DamageInfo.createDamageMatrix(owner.getPower(DrenchPower.POWER_ID).amount*DrenchPower.STEAM_DAMAGE, true), DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.FIRE, true));
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                if (!m.isDeadOrEscaped()) {
                    this.addToBot(new ApplyPowerAction(m, source, new BurnPower(m, source, owner.getPower(DrenchPower.POWER_ID).amount*DrenchPower.STEAM_BURN), owner.getPower(DrenchPower.POWER_ID).amount*DrenchPower.STEAM_BURN, true));
                }
            }*/
        }
    }

    @Override
    public void atEndOfRound() {
        triggerEffect();
    }

    public void triggerEffect() {
        this.flashWithoutSound();
        if (AbstractDungeon.player.hasRelic(DataCollector.ID) && source == AbstractDungeon.player) {
            DataCollector dataCollector = (DataCollector) AbstractDungeon.player.getRelic(DataCollector.ID);
            dataCollector.onBurnDamage(damagePerTurn);
        }
        this.addToBot(new DamageAction(owner, new DamageInfo(source, damagePerTurn, DamageInfo.DamageType.HP_LOSS), AbstractGameAction.AttackEffect.FIRE, true));
        if (this.amount == 1) {
            this.addToBot(new RemoveSpecificPowerAction(owner, owner, this));
        } else {
            this.addToBot(new ReducePowerAction(owner, owner, this, 1));
        }
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + damagePerTurn + DESCRIPTIONS[1];
    }

    @Override
    public boolean onReceivePower(AbstractPower abstractPower, AbstractCreature target, AbstractCreature source) {
        //We have to use a delayed tally here since the power hasnt actually been added yet
        delayedTallyDebuffs();
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
    }

    @Override
    public void onRemoveOtherPower(AbstractPower powerRemoved) {
        //We can tally right away as this patch takes place directly after the power is removed
        tallyDebuffs();
    }

    public void tallyDebuffs() {
        damagePerTurn = 0;
        for (AbstractPower pow : owner.powers) {
            if (pow.type == PowerType.DEBUFF) {
                damagePerTurn++;
            }
        }
        updateDescription();
    }

    public void delayedTallyDebuffs() {
        this.addToTop(new AbstractGameAction() {
            @Override
            public void update() {
                tallyDebuffs();
                this.isDone = true;
            }
        });
    }

    public void renderAmount(SpriteBatch sb, float x, float y, Color c) {
        FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, Integer.toString(this.amount), x, y, this.fontScale, c);
        FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, Integer.toString(damagePerTurn), x, y + 15.0F * Settings.scale, this.fontScale, c);
    }

    @Override
    public AbstractPower makeCopy() {
        return new BurnPower(owner, source, amount);
    }

    @Override
    public int getHealthBarAmount() {
        return damagePerTurn;
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
