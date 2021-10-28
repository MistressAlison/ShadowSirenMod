package ShadowSiren.actions;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class MoveCardsOfPredicateToHandAction extends AbstractGameAction {

    private final Predicate<AbstractCard> pred;
    private final Consumer<AbstractCard> effect;

    public MoveCardsOfPredicateToHandAction(Predicate<AbstractCard> pred) {
        this(pred, 1, null);
    }

    public MoveCardsOfPredicateToHandAction(Predicate<AbstractCard> pred, int amount) {
        this(pred, amount, null);
    }

    public MoveCardsOfPredicateToHandAction(Predicate<AbstractCard> pred, int amount, Consumer<AbstractCard> effect) {
        this.pred = pred;
        this.amount = amount;
        this.effect = effect;
    }

    @Override
    public void update() {
        if (AbstractDungeon.player.drawPile.isEmpty() || amount <= 0) {
            this.isDone = true;
            return;
        }

        CardGroup tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

        for (AbstractCard c : AbstractDungeon.player.drawPile.group) {
            if (pred.test(c)) {
                tmp.addToRandomSpot(c);
            }
        }

        if (tmp.size() == 0) {
            this.isDone = true;
            return;
        }

        if (amount > tmp.size()) {
            amount = tmp.size();
        }

        tmp.shuffle();

        for (int i = 0 ; i < amount ; i++) {
            AbstractCard card = tmp.getBottomCard();
            tmp.removeCard(card);
            if (AbstractDungeon.player.hand.size() + i >= BaseMod.MAX_HAND_SIZE) {
                AbstractDungeon.player.drawPile.moveToDiscardPile(card);
                AbstractDungeon.player.createHandIsFullDialog();
            } else {
                if(effect != null) {
                    effect.accept(card);
                }
                card.unhover();
                card.lighten(true);
                card.setAngle(0.0F);
                card.drawScale = 0.12F;
                card.targetDrawScale = 0.75F;
                card.current_x = CardGroup.DRAW_PILE_X;
                card.current_y = CardGroup.DRAW_PILE_Y;
                AbstractDungeon.player.drawPile.removeCard(card);
                AbstractDungeon.player.hand.addToTop(card);
                AbstractDungeon.player.hand.refreshHandLayout();
                AbstractDungeon.player.hand.applyPowers();
            }
        }

        this.isDone = true;
    }
}
