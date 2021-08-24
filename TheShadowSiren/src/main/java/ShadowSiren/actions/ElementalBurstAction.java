package ShadowSiren.actions;

import ShadowSiren.util.ParticleOrbitRenderer;
import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;

public class ElementalBurstAction extends AbstractGameAction {
    float waitTimer;
    boolean firstPass = true;
    boolean mouseReturn;
    int actionPhase;
    DamageInfo info;


    public ElementalBurstAction(AbstractCreature target, DamageInfo info, int hits) {
        this.target = target;
        this.source = info.owner;
        this.info = info;
        this.amount = hits;
        this.actionType = ActionType.DAMAGE;
    }

    @Override
    public void update() {
        if (firstPass) {
            firstPass = false;
            mouseReturn = ParticleOrbitRenderer.getCurrentTarget() == ParticleOrbitRenderer.TargetLocation.MOUSE;
            ParticleOrbitRenderer.moveOrbit(target.hb);
        }
        if (waitTimer > 0) {
            waitTimer -= Gdx.graphics.getDeltaTime();
        } else if (actionPhase == 0) {
            if (ParticleOrbitRenderer.reachedDestination()) {
                actionPhase++;
                waitTimer += 0.15f;
            }
        } else if (actionPhase == 1) {
            if (amount > 0) {
                AbstractDungeon.effectsQueue.add(new ExplosionSmallEffect(target.hb.cX, target.hb.cY));
                target.damage(info);
                amount--;
                waitTimer += 0.15f;
            } else {
                this.isDone = true;
                if (mouseReturn) {
                    ParticleOrbitRenderer.returnOrbitToMouse();
                } else {
                    ParticleOrbitRenderer.returnOrbitToPlayer();
                }
            }
        }
        if (this.isDone) {
            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions();
            }
        }
    }
}
