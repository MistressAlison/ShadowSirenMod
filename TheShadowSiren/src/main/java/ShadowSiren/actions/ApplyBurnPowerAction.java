package ShadowSiren.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;

public class ApplyBurnPowerAction extends AbstractGameAction {

    public ApplyBurnPowerAction(AbstractCreature source, AbstractCreature target, int amount) {
        this.source = source;
        this.target = target;
        this.amount = amount;
        this.actionType = ActionType.POWER;
    }

    public void update() {
    }
}
