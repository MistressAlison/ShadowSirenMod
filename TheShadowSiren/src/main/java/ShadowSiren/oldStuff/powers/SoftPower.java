package ShadowSiren.oldStuff.powers;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.actions.FastLoseBlockAction;
import ShadowSiren.powers.FreezePower;
import ShadowSiren.relics.DataCollector;
import ShadowSiren.relics.RuinPowder;
import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;

public class SoftPower extends AbstractPower implements CloneablePowerInterface {

    public static final String POWER_ID = ShadowSirenMod.makeID("SoftPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public static final int BONUS_DAMAGE = 3;
    public static final int BLOCK_LOSS = 3;


    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    //private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("placeholder_power84.png"));
    //private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("placeholder_power32.png"));

    public SoftPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;

        this.type = PowerType.DEBUFF;
        this.isTurnBased = true;

        // We load those txtures here.
        //this.loadRegion("cExplosion");
        this.loadRegion("brutality");
        //logger.info("Blasting Fuse?");
        //this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        //this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void onInitialApplication() {
        if (AbstractDungeon.player.hasRelic(RuinPowder.ID)) {
            this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, AbstractDungeon.player.getRelic(RuinPowder.ID)));
            AbstractDungeon.player.getRelic(RuinPowder.ID).flash();
        }
        if (AbstractDungeon.player.hasRelic(DataCollector.ID)) {
            DataCollector dataCollector = (DataCollector) AbstractDungeon.player.getRelic(DataCollector.ID);
            dataCollector.onApplySoft(amount);
        }
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        if (AbstractDungeon.player.hasRelic(DataCollector.ID)) {
            DataCollector dataCollector = (DataCollector) AbstractDungeon.player.getRelic(DataCollector.ID);
            dataCollector.onApplySoft(stackAmount);
        }
    }

    public float atDamageReceive(float damage, DamageInfo.DamageType type) {
        return damage + (AbstractDungeon.player.hasRelic(RuinPowder.ID) ? RuinPowder.RELIC_BONUS_DAMAGE : BONUS_DAMAGE);
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        flash();
        this.addToTop(new FastLoseBlockAction(owner, info.owner, BLOCK_LOSS));
        if (AbstractDungeon.player.hasRelic(RuinPowder.ID) && !owner.hasPower(IntangiblePlayerPower.POWER_ID) && !owner.hasPower(FreezePower.POWER_ID)) {
            ((RuinPowder)AbstractDungeon.player.getRelic(RuinPowder.ID)).onIncreaseDamage(RuinPowder.RELIC_BONUS_DAMAGE-BONUS_DAMAGE);
        }
        if (AbstractDungeon.player.hasRelic(DataCollector.ID)) {
            DataCollector dataCollector = (DataCollector) AbstractDungeon.player.getRelic(DataCollector.ID);
            dataCollector.onSoftDamage(AbstractDungeon.player.hasRelic(RuinPowder.ID) ? RuinPowder.RELIC_BONUS_DAMAGE : BONUS_DAMAGE);
        }
        return super.onAttacked(info, damageAmount);
    }

    public void atEndOfRound() {
        if (this.amount == 0) {
            this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        } else {
            this.addToBot(new ReducePowerAction(this.owner, this.owner, this, 1));
        }
    }

    @Override
    public void updateDescription() {
        int dam = (AbstractDungeon.player.hasRelic(RuinPowder.ID) ? RuinPowder.RELIC_BONUS_DAMAGE : BONUS_DAMAGE);
        if (amount == 1) {
            description = DESCRIPTIONS[0] + DESCRIPTIONS[3] + dam + DESCRIPTIONS[4] + BLOCK_LOSS + DESCRIPTIONS[5];
        } else {
            description = DESCRIPTIONS[1] + amount + DESCRIPTIONS[2] + DESCRIPTIONS[3] + dam + DESCRIPTIONS[4] + BLOCK_LOSS + DESCRIPTIONS[5];
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new SoftPower(owner, amount);
    }

}
