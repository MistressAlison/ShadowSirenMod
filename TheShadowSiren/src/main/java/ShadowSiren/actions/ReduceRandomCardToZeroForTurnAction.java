package ShadowSiren.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class ReduceRandomCardToZeroForTurnAction extends AbstractGameAction {
    private final CardGroup group;

    public ReduceRandomCardToZeroForTurnAction(CardGroup group) {
        this.group = group;
    }

    @Override
    public void update() {
        CardGroup tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (AbstractCard c : group.group) {
            if (c.cost > 0 && c.costForTurn > 0 && !c.freeToPlayOnce) {
                tmp.addToTop(c);
            }
        }
        if (group == AbstractDungeon.player.hand) {
            for (CardQueueItem i : AbstractDungeon.actionManager.cardQueue) {
                if (i.card != null) {
                    tmp.removeCard(i.card);
                }
            }
        }

        if (tmp.size() > 0) {
            AbstractCard c = tmp.getRandomCard(AbstractDungeon.cardRandomRng);
            if (c != null) {
                c.setCostForTurn(0);
            }
        }

        this.isDone = true;
    }
}
