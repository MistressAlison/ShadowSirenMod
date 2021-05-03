package ShadowSiren.actions;

import ShadowSiren.cards.abstractCards.AbstractDualityCard;
import ShadowSiren.patches.relics.BottleFields;
import basemod.BaseMod;
import basemod.Pair;
import basemod.abstracts.CustomBottleRelic;
import com.evacipated.cardcrawl.mod.stslib.StSLib;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.TransformCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.Collection;
import java.util.Iterator;
import java.util.function.Predicate;

public class SwapDualityCardsAction extends AbstractGameAction {


    private final AbstractDualityCard toReplace;
    private final AbstractDualityCard newCard;

    public SwapDualityCardsAction(AbstractDualityCard toReplace, AbstractDualityCard newCard) {
        this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
        this.toReplace = toReplace;
        this.newCard = newCard;
    }

    public void update() {
        int index = 0;
        boolean found = false;
        for (AbstractCard card : AbstractDungeon.player.hand.group)
        {
            if (card.cardID.equals(toReplace.cardID)) {
                found = true;
                break;
            }
            index++;
        }
        if(found) {
            AbstractDualityCard copy = (AbstractDualityCard) toReplace.makeStatEquivalentCopy();
            newCard.dualCard = copy;
            newCard.cardsToPreview = copy;
            newCard.applyPowers();
            if (BottleFields.inBottledStar.get(toReplace)) {
                BottleFields.inBottledStar.set(newCard, true);
            }
            if (AbstractDungeon.player.hoveredCard == toReplace) {
                AbstractDungeon.player.releaseCard();
            }
            AbstractDungeon.actionManager.cardQueue.removeIf(q -> q.card == toReplace);
            this.addToTop(new TransformCardInHandAction(index, newCard));
        }
        this.isDone = true;
    }
}