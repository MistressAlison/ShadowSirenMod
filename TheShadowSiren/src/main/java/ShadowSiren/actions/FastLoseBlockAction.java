package ShadowSiren.actions;

import com.megacrit.cardcrawl.actions.utility.LoseBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;

public class FastLoseBlockAction extends LoseBlockAction {
    public FastLoseBlockAction(AbstractCreature target, AbstractCreature source, int amount) {
        super(target, source, amount);
    }

    @Override
    public void update() {
        if (this.target.currentBlock > 0) {
            this.target.loseBlock(this.amount);
        }
        this.isDone = true;
    }
}
