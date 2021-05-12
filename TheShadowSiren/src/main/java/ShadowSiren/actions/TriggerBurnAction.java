package ShadowSiren.actions;

import ShadowSiren.powers.BurnPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;

public class TriggerBurnAction extends AbstractGameAction {
    private static final float DURATION = 0.1F;

    public TriggerBurnAction(AbstractCreature target, int amount) {
        this.target = target;
        this.amount = amount;
        this.actionType = ActionType.POWER;
        this.duration = DURATION;
    }

    public void update() {
        if (this.duration == DURATION && this.target != null) {
            if (this.target.hasPower(BurnPower.POWER_ID)) {
                BurnPower burn = (BurnPower) this.target.getPower(BurnPower.POWER_ID);
                int effect = Math.min(this.amount, burn.amount);
                for (int i = 0 ; i < effect ; i++) {
                    burn.triggerEffect();
                }
            }
        }
        this.tickDuration();
    }
}
