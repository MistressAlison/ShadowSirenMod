package ShadowSiren.powers;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.relics.DataCollector;
import basemod.ReflectionHacks;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.powers.StunMonsterPower;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.FrostOrbActivateEffect;

import java.awt.*;

public class FreezePower extends StunMonsterPower implements CloneablePowerInterface {

    public static final String POWER_ID = ShadowSirenMod.makeID("FreezePower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private int hitsToBreak = 3;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    //private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("placeholder_power84.png"));
    //private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("placeholder_power32.png"));

    public FreezePower(AbstractMonster owner, int amount) {
        super(owner, amount);
        this.name = NAME;
        //this.ID = POWER_ID;

        // We load those txtures here.
        this.img = null;
        this.loadRegion("int");
        this.priority = Integer.MAX_VALUE;
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
            dataCollector.onApplyFreeze(amount);
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
            dataCollector.onApplyFreeze(stackAmount);
        }
    }

    @Override
    public float atDamageReceive(float damage, DamageInfo.DamageType damageType) {
        return damageType == DamageInfo.DamageType.NORMAL && hitsToBreak == 1 ? damage * 3 : 0;
    }

    public void renderAmount(SpriteBatch sb, float x, float y, Color c) {
        c = new Color(0.0F, 1.0F, 0.0F, 1.0F);
        FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, Integer.toString(this.amount), x, y, this.fontScale, c);
        FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, Integer.toString(hitsToBreak), x, y + 15.0F * Settings.scale, this.fontScale, c);
    }

    @Override
    public int onAttackedToChangeDamage(DamageInfo info, int damageAmount) {
        if (info.type == DamageInfo.DamageType.NORMAL) {
            hitsToBreak--;
            if (hitsToBreak == 0) {
                this.addToTop(new RemoveSpecificPowerAction(owner, owner, this));
            }
        }
        updateDescription();
        return hitsToBreak == 0 ? damageAmount : 0;
    }

    @Override
    public void updateDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append(DESCRIPTIONS[0]).append(amount);
        if(amount == 1) {
            sb.append(DESCRIPTIONS[1]);
        } else {
            sb.append(DESCRIPTIONS[2]);
        }
        sb.append(DESCRIPTIONS[3]).append(hitsToBreak);
        if (hitsToBreak == 1) {
            sb.append(DESCRIPTIONS[4]);
        } else {
            sb.append(DESCRIPTIONS[5]);
        }
        description = sb.toString();
    }

    @Override
    public AbstractPower makeCopy() {
        return new FreezePower((AbstractMonster) owner, amount);
    }
}
