package ShadowSiren.powers;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.relics.DataCollector;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
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

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    //private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("placeholder_power84.png"));
    //private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("placeholder_power32.png"));

    public ChillPower(AbstractMonster owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;

        this.type = PowerType.DEBUFF;
        this.isTurnBased = false;

        // We load those txtures here.
        this.loadRegion("skillBurn");
        //this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        //this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void onInitialApplication() {
        super.onInitialApplication();
        CardCrawlGame.sound.play("ORB_FROST_EVOKE", 0.1F);
        AbstractDungeon.effectsQueue.add(new FrostOrbActivateEffect(owner.hb_x, owner.hb_y));
        owner.tint.color = Color.BLUE.cpy();
        owner.tint.changeColor(Color.WHITE.cpy());
        if (AbstractDungeon.player.hasRelic(DataCollector.ID)) {
            DataCollector dataCollector = (DataCollector) AbstractDungeon.player.getRelic(DataCollector.ID);
            dataCollector.onApplyChill(amount);
        }
        if (owner.hasPower(DrenchPower.POWER_ID)) {
            owner.getPower(DrenchPower.POWER_ID).flash();
            this.addToTop(new RemoveSpecificPowerAction(owner, owner, DrenchPower.POWER_ID));
            this.addToBot(new ApplyPowerAction(owner, owner, new ChillPower((AbstractMonster) owner, owner.getPower(DrenchPower.POWER_ID).amount*DrenchPower.CHILL_BONUS)));
        }
        if (amount >= 10) {
            int effect = amount / 10;
            this.addToBot(new ReducePowerAction(owner, owner, this, effect*10));
            this.addToBot(new ApplyPowerAction(owner, owner, new FreezePower((AbstractMonster) owner, effect)));
            if (AbstractDungeon.player.hasRelic(DataCollector.ID)) {
                DataCollector dataCollector = (DataCollector) AbstractDungeon.player.getRelic(DataCollector.ID);
                dataCollector.onChillRollover(effect);
            }
        }
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        CardCrawlGame.sound.play("ORB_FROST_EVOKE", 0.1F);
        AbstractDungeon.effectsQueue.add(new FrostOrbActivateEffect(owner.hb_x, owner.hb_y));
        owner.tint.color = Color.BLUE.cpy();
        owner.tint.changeColor(Color.WHITE.cpy());
        if (AbstractDungeon.player.hasRelic(DataCollector.ID)) {
            DataCollector dataCollector = (DataCollector) AbstractDungeon.player.getRelic(DataCollector.ID);
            dataCollector.onApplyChill(amount);
        }
        if (owner.hasPower(DrenchPower.POWER_ID)) {
            owner.getPower(DrenchPower.POWER_ID).flash();
            this.addToTop(new RemoveSpecificPowerAction(owner, owner, DrenchPower.POWER_ID));
            this.addToBot(new ApplyPowerAction(owner, owner, new ChillPower((AbstractMonster) owner, owner.getPower(DrenchPower.POWER_ID).amount*DrenchPower.CHILL_BONUS)));
        }
        if (amount >= 10) {
            int effect = amount / 10;
            this.addToBot(new ReducePowerAction(owner, owner, this, effect*10));
            this.addToBot(new ApplyPowerAction(owner, owner, new FreezePower((AbstractMonster) owner, effect)));
            if (AbstractDungeon.player.hasRelic(DataCollector.ID)) {
                DataCollector dataCollector = (DataCollector) AbstractDungeon.player.getRelic(DataCollector.ID);
                dataCollector.onChillRollover(effect);
            }
        }
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0];
    }

    @Override
    public AbstractPower makeCopy() {
        return new ChillPower((AbstractMonster) owner, amount);
    }
}
