package ShadowSiren.powers;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.actions.BurningLoseHPAction;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.HealthBarRenderPower;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.ExhaustEmberEffect;
import com.megacrit.cardcrawl.vfx.combat.FlameParticleEffect;

public class BurningPower extends AbstractPower implements HealthBarRenderPower {
    public static final String POWER_ID = ShadowSirenMod.makeID(BurningPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private static final Color color = Color.ORANGE.cpy();
    private float particleTimer;
    private AbstractCreature source;

    public BurningPower(AbstractCreature owner, AbstractCreature source, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.source = source;
        this.amount = amount;
        this.type = PowerType.DEBUFF;
        this.loadRegion("combust");
        updateDescription();
    }

    @Override
    public void playApplyPowerSfx() {
        CardCrawlGame.sound.play("ATTACK_FIRE", 0.05F);
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    @Override
    public void atStartOfTurn() {
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && !AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            flashWithoutSound();
            addToBot(new BurningLoseHPAction(owner, source, amount));
            //addToBot(new ReducePowerAction(owner, owner, this, 1));
        }
    }

    @Override
    public int getHealthBarAmount() {
        return amount;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public void updateParticles() {
        particleTimer -= Gdx.graphics.getDeltaTime();
        if (particleTimer <= 0) {
            particleTimer = 1f/Math.min(10, Math.max(1, amount));
            for(int i = 0; i < 4; ++i) {// 21
                AbstractDungeon.effectsQueue.add(new FlameParticleEffect(owner.hb.cX, owner.hb.cY));
            }

            for(int i = 0; i < 1; ++i) {// 24
                AbstractDungeon.effectsQueue.add(new ExhaustEmberEffect(owner.hb.cX, owner.hb.cY));
            }
        }
    }
}
