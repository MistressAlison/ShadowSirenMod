package ShadowSiren.powers;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.powers.interfaces.OnModifyMaxHPPower;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.patches.NeutralPowertypePatch;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.HealthBarRenderPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.InstantKillAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.common.SuicideAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.SpeechBubble;
import com.megacrit.cardcrawl.vfx.combat.HeartBuffEffect;

import java.text.DecimalFormat;

public class ShadowPower extends AbstractPower implements CloneablePowerInterface, HealthBarRenderPower, OnModifyMaxHPPower {

    public static final String POWER_ID = ShadowSirenMod.makeID("ShadowPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private final Color hpBarColor = Color.DARK_GRAY.cpy();
    int loopCasts = 0;
    boolean sad;
    //DecimalFormat df = new DecimalFormat("#.##");
    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    //private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("placeholder_power84.png"));
    //private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("placeholder_power32.png"));

    boolean surpassedHP = false;

    public ShadowPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;

        this.type = NeutralPowertypePatch.NEUTRAL;
        this.priority = 1000;
        this.isTurnBased = false;

        // We load those txtures here.
        this.loadRegion("unawakened");
        //logger.info("Blasting Fuse?");
        //this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        //this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    public void delayedCheckIfSurpassedHP() {
        this.addToTop(new AbstractGameAction() {
            @Override
            public void update() {
                checkIfSurpassedHP();
                tickDuration();
            }
        });
    }

    public boolean checkIfSurpassedHP() {
        if (amount >= owner.currentHealth) {
            if (!surpassedHP && owner.currentHealth > 0) {
                if (loopCasts > 0 && !sad) {
                    //this.addToTop(new VFXAction(new SpeechBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 1.0f, ":(", true)));
                    sad = true;
                }
                loopCasts++;
                this.addToTop(new InstantKillAction(owner));
                playSurpassedVFX();
            }
        } else {
            surpassedHP = false;
        }
        updateDescription();
        return surpassedHP;
    }

    @Override
    public void onInitialApplication() {
        super.onInitialApplication();
        delayedCheckIfSurpassedHP();
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        delayedCheckIfSurpassedHP();

    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (loopCasts < 3 || damageAmount > 0) {
            delayedCheckIfSurpassedHP();
        }
        return super.onAttacked(info, damageAmount);
    }

    public void playSurpassedVFX() {
        AbstractDungeon.actionManager.addToTop(new VFXAction(new HeartBuffEffect(owner.hb.cX, owner.hb.cY), 0.2f));
    }

    @Override
    public void updateDescription() {
        //description = DESCRIPTIONS[0] + df.format(Math.min(100, amount * 100 / Math.max(owner.currentHealth, 1))) + "%" + DESCRIPTIONS[1];
        description = DESCRIPTIONS[0];
    }

    @Override
    public AbstractPower makeCopy() {
        return new ShadowPower(owner, amount);
    }

    @Override
    public int getHealthBarAmount() {
        return amount;
    }

    @Override
    public Color getColor() {
        return hpBarColor;
    }

    @Override
    public int onModifyMaxHP(AbstractCreature target, int amount) {
        delayedCheckIfSurpassedHP();
        return amount;
    }
}
