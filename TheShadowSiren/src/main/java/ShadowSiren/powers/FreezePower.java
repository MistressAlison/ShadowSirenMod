package ShadowSiren.powers;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.patches.FreezePatches;
import ShadowSiren.relics.DataCollector;
import basemod.ReflectionHacks;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.stslib.powers.StunMonsterPower;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.monsters.exordium.Hexaghost;
import com.megacrit.cardcrawl.monsters.exordium.HexaghostBody;
import com.megacrit.cardcrawl.monsters.exordium.HexaghostOrb;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.UncommonPotionParticleEffect;
import com.megacrit.cardcrawl.vfx.combat.FrostOrbActivateEffect;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Random;

public class FreezePower extends AbstractPower implements CloneablePowerInterface {

    public static final String POWER_ID = ShadowSirenMod.makeID("FreezePower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    //private int hitsToBreak = 3;
    private byte moveByte;
    private AbstractMonster.Intent moveIntent;
    private EnemyMoveInfo move;

    private static Random rng = new Random();
    //public static Color tintCol = new Color(163f/255, 247f/255, 1, 1);
    public static Color tintCol = new Color(0.9f, 1, 1, 1);
    //public static Color tintCol = new Color(0.9f, 1, 1, 1);
    private static final float INTERVAL = 0.1f;
    private float particleTimer;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    //private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("placeholder_power84.png"));
    //private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("placeholder_power32.png"));

    public FreezePower(AbstractMonster owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;

        this.isTurnBased = true;
        this.type = PowerType.DEBUFF;

        // We load those txtures here.
        //this.img = null;
        this.loadRegion("int");
        //this.priority = Integer.MAX_VALUE;
        //this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        //this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    //Reworked from StunMonsterPower
    @Override
    public void onInitialApplication() {
        //Oh boy, time for a bunch of exceptions
        if (owner instanceof Hexaghost) {
            HexaghostBody body = ReflectionHacks.getPrivate(owner, Hexaghost.class, "body");
            FreezePatches.setHexaghostBodyFreezeFlag(body, true);
            ArrayList<HexaghostOrb> orbs = ReflectionHacks.getPrivate(owner, Hexaghost.class, "orbs");
            for (HexaghostOrb o : orbs) {
                FreezePatches.setHexaghostOrbFreezeFlag(o, true);
            }
        }
        if (owner.state != null) {
            FreezePatches.setFreezeFlag(owner.state, true);
        }
        AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
            public void update() {
                if (FreezePower.this.owner instanceof AbstractMonster) {
                    FreezePower.this.moveByte = ((AbstractMonster)FreezePower.this.owner).nextMove;
                    FreezePower.this.moveIntent = ((AbstractMonster)FreezePower.this.owner).intent;

                    try {
                        Field f = AbstractMonster.class.getDeclaredField("move");
                        f.setAccessible(true);
                        FreezePower.this.move = (EnemyMoveInfo)f.get(FreezePower.this.owner);
                        FreezePower.this.move.intent = AbstractMonster.Intent.STUN;
                        ((AbstractMonster)FreezePower.this.owner).createIntent();
                    } catch (NoSuchFieldException | IllegalAccessException var2) {
                        var2.printStackTrace();
                    }
                }

                this.isDone = true;
            }
        });
        CardCrawlGame.sound.play("ORB_FROST_EVOKE", 0.1F);
        AbstractDungeon.effectsQueue.add(new FrostOrbActivateEffect(owner.hb_x, owner.hb_y));
        //owner.tint.color = Color.BLUE.cpy();
        //owner.tint.changeColor(Color.WHITE.cpy());
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
        //owner.tint.color = Color.BLUE.cpy();
        //owner.tint.changeColor(Color.WHITE.cpy());
        if (AbstractDungeon.player.hasRelic(DataCollector.ID)) {
            DataCollector dataCollector = (DataCollector) AbstractDungeon.player.getRelic(DataCollector.ID);
            dataCollector.onApplyFreeze(stackAmount);
        }
    }

    @Override
    public float atDamageReceive(float damage, DamageInfo.DamageType damageType) {
        //return damageType == DamageInfo.DamageType.NORMAL && hitsToBreak == 1 ? damage * 3 : 0;
        return 0;
    }

    /*public void renderAmount(SpriteBatch sb, float x, float y, Color c) {
        c = new Color(0.0F, 1.0F, 0.0F, 1.0F);
        FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, Integer.toString(this.amount), x, y, this.fontScale, c);
        FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, Integer.toString(hitsToBreak), x, y + 15.0F * Settings.scale, this.fontScale, c);
    }*/

    @Override
    public int onAttackedToChangeDamage(DamageInfo info, int damageAmount) {
        /*if (info.type == DamageInfo.DamageType.NORMAL) {
            hitsToBreak--;
            if (hitsToBreak == 0) {
                this.addToTop(new RemoveSpecificPowerAction(owner, owner, this));
            }
        }
        updateDescription();
        return hitsToBreak == 0 ? damageAmount : 0;*/
        return 0;
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
        /*sb.append(DESCRIPTIONS[3]).append(hitsToBreak);
        if (hitsToBreak == 1) {
            sb.append(DESCRIPTIONS[4]);
        } else {
            sb.append(DESCRIPTIONS[5]);
        }*/
        description = sb.toString();
    }

    @Override
    public AbstractPower makeCopy() {
        return new FreezePower((AbstractMonster) owner, amount);
    }


    @Override
    public void atEndOfRound() {
        if (this.amount <= 0) {
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        } else {
            AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(this.owner, this.owner, this, 1));
        }

    }

    //Completely copied from StunMonsterPower
    @Override
    public void onRemove() {
        if (this.owner instanceof AbstractMonster) {
            AbstractMonster m = (AbstractMonster)this.owner;
            if (this.move != null) {
                m.setMove(this.moveByte, this.moveIntent, this.move.baseDamage, this.move.multiplier, this.move.isMultiDamage);
            } else {
                m.setMove(this.moveByte, this.moveIntent);
            }

            m.createIntent();
            m.applyPowers();
            if (owner instanceof Hexaghost) {
                HexaghostBody body = ReflectionHacks.getPrivate(owner, Hexaghost.class, "body");
                FreezePatches.setHexaghostBodyFreezeFlag(body, false);
                ArrayList<HexaghostOrb> orbs = ReflectionHacks.getPrivate(owner, Hexaghost.class, "orbs");
                for (HexaghostOrb o : orbs) {
                    FreezePatches.setHexaghostOrbFreezeFlag(o, false);
                }
            }
            if (owner.state != null) {
                FreezePatches.setFreezeFlag(owner.state, false);
            }
        }
    }

    //Stolen from GK
    @SpirePatch(clz = AbstractMonster.class, method = "render")
    public static class ChangeColorOfAffectedMonsterPls {
        @SpirePrefixPatch
        public static void patch(AbstractMonster __instance, SpriteBatch sb) {
            if(__instance.hasPower(FreezePower.POWER_ID)) {
                __instance.tint.color.mul(FreezePower.tintCol);
            }
        }
    }

    @Override
    public void updateParticles() {
        this.particleTimer -= Gdx.graphics.getRawDeltaTime();
        if (this.particleTimer < 0.0F) {
            float xOff = ((owner.hb_w) * (float) rng.nextGaussian())*0.25f;
            if(MathUtils.randomBoolean()) {
                xOff = -xOff;
            }
            float yOff = ((owner.hb_w) * (float) rng.nextGaussian())*0.25f;
            if(MathUtils.randomBoolean()) {
                yOff = -xOff;
            }
            //AbstractDungeon.effectList.add(new StraightFireParticle(owner.drawX + xOff, owner.drawY + MathUtils.random(owner.hb_h/2f), 75f));
            AbstractDungeon.effectList.add(new UncommonPotionParticleEffect(owner.hb.cX+xOff, owner.hb.cY+yOff));
            this.particleTimer = INTERVAL;
        }
    }
}
