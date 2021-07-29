package ShadowSiren.powers;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.actions.BurnToAshEffect;
import ShadowSiren.powers.interfaces.OnRemoveOtherPowerPower;
import ShadowSiren.relics.DataCollector;
import basemod.ReflectionHacks;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.HealthBarRenderPower;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnReceivePowerPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;

public class HexingPower extends AbstractPower implements CloneablePowerInterface, HealthBarRenderPower, OnRemoveOtherPowerPower, OnReceivePowerPower {

    public static final String POWER_ID = ShadowSirenMod.makeID("HexingPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private final Color hpBarColor = CardHelper.getColor(83.0f, 52.0f, 85.0f);

    private final AbstractCreature source;
    private int damagePerTurn;
    private int baseDamage;
    //public boolean activated;
    //private final int PER_N_BOOST = 5;

    public static final int HEX_TO_HEXING_RATIO = 5;
    public static final int STACKS_PER_INCREASE = 5;
    public static final int BASE_EFFECT = 1;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    //private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("placeholder_power84.png"));
    //private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("placeholder_power32.png"));


    public HexingPower(AbstractCreature owner, AbstractCreature source, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.baseDamage = BASE_EFFECT + amount/STACKS_PER_INCREASE;
        this.source = source;
        this.priority = 0;
        this.type = PowerType.DEBUFF;
        this.isTurnBased = true;

        // We load those txtures here.
        //this.loadRegion("cExplosion");
        this.loadRegion("hex");
        //this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        //this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void onInitialApplication() {
        super.onInitialApplication();
        this.baseDamage = BASE_EFFECT + amount/STACKS_PER_INCREASE;
        tallyDebuffs();
        if (AbstractDungeon.player.hasRelic(DataCollector.ID) && source == AbstractDungeon.player) {
            DataCollector dataCollector = (DataCollector) AbstractDungeon.player.getRelic(DataCollector.ID);
            dataCollector.onApplyHex(this.amount);
        }
        //deathCheck(0);
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        this.baseDamage = BASE_EFFECT + amount/STACKS_PER_INCREASE;
        tallyDebuffs();
        if (AbstractDungeon.player.hasRelic(DataCollector.ID) && source == AbstractDungeon.player) {
            DataCollector dataCollector = (DataCollector) AbstractDungeon.player.getRelic(DataCollector.ID);
            dataCollector.onApplyHex(stackAmount);
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
            dataCollector.onHexDamage(baseDamage*damagePerTurn);
        }
        this.addToBot(new DamageAction(owner, new DamageInfo(source, baseDamage*damagePerTurn, DamageInfo.DamageType.HP_LOSS), AbstractGameAction.AttackEffect.FIRE, true));
        if (this.amount <= baseDamage) {
            this.addToBot(new RemoveSpecificPowerAction(owner, owner, this));
        } else {
            this.addToBot(new ReducePowerAction(owner, owner, this, baseDamage));
        }
        this.baseDamage = BASE_EFFECT + (amount-baseDamage)/STACKS_PER_INCREASE;
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + baseDamage + DESCRIPTIONS[1] + baseDamage + DESCRIPTIONS[2] + STACKS_PER_INCREASE + DESCRIPTIONS[3] + (baseDamage*damagePerTurn) + DESCRIPTIONS[4];
    }

    @Override
    public boolean onReceivePower(AbstractPower abstractPower, AbstractCreature target, AbstractCreature source) {
        //We have to use a delayed tally here since the power hasnt actually been added yet. Since both ATT, this should happen first
        delayedTallyDebuffs();
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
                if (pow instanceof FoxFirePower && pow.amount > 1) {
                    damagePerTurn += (pow.amount - 1);
                }
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
        FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, Integer.toString(baseDamage*damagePerTurn), x, y + 15.0F * Settings.scale, this.fontScale, c);
    }

    @Override
    public AbstractPower makeCopy() {
        return new HexingPower(owner, source, amount);
    }

    @Override
    public int getHealthBarAmount() {
        return baseDamage*damagePerTurn;
    }

    @Override
    public Color getColor() {
        return hpBarColor;
    }
}