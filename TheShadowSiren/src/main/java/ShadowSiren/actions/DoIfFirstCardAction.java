package ShadowSiren.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class DoIfFirstCardAction extends AbstractGameAction {
    private final AbstractGameAction action;

    public DoIfFirstCardAction(AbstractGameAction action) {
        this.action = action;
        this.actionType = ActionType.SPECIAL;
    }

    public void update() {
        if (AbstractDungeon.actionManager.cardsPlayedThisTurn.size() == 1) {
            this.addToTop(action);
        }
        this.isDone = true;
    }
}