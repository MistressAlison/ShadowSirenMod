package ShadowSiren.actions;

import ShadowSiren.cards.abstractCards.AbstractPrismaticCard;
import ShadowSiren.cards.abstractCards.prismatics.*;
import ShadowSiren.patches.relics.BottleFields;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.TransformCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class SwapPrismaticCardsAction extends AbstractGameAction {


    private final AbstractPrismaticCard toReplace;
    private final AbstractPrismaticCard newCard;

    public SwapPrismaticCardsAction(AbstractPrismaticCard toReplace, AbstractPrismaticCard newCard) {
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
            AbstractPrismaticBaseCard base = toReplace instanceof AbstractPrismaticBaseCard ? (AbstractPrismaticBaseCard) toReplace.makeStatEquivalentCopy() : newCard instanceof  AbstractPrismaticBaseCard ? null : toReplace.baseCard;
            AbstractPrismaticVeilCard veil = toReplace instanceof AbstractPrismaticVeilCard ? (AbstractPrismaticVeilCard) toReplace.makeStatEquivalentCopy() : newCard instanceof  AbstractPrismaticVeilCard ? null : toReplace.veilCard;
            AbstractPrismaticAbyssCard abyss = toReplace instanceof AbstractPrismaticAbyssCard ? (AbstractPrismaticAbyssCard) toReplace.makeStatEquivalentCopy() : newCard instanceof  AbstractPrismaticAbyssCard ? null : toReplace.abyssCard;
            AbstractPrismaticSmokeCard smoke = toReplace instanceof AbstractPrismaticSmokeCard ? (AbstractPrismaticSmokeCard) toReplace.makeStatEquivalentCopy() : newCard instanceof  AbstractPrismaticSmokeCard ? null : toReplace.smokeCard;
            AbstractPrismaticHugeCard huge = toReplace instanceof AbstractPrismaticHugeCard ? (AbstractPrismaticHugeCard) toReplace.makeStatEquivalentCopy() : newCard instanceof  AbstractPrismaticHugeCard ? null : toReplace.hugeCard;
            AbstractPrismaticHyperCard hyper = toReplace instanceof AbstractPrismaticHyperCard ? (AbstractPrismaticHyperCard) toReplace.makeStatEquivalentCopy() : newCard instanceof  AbstractPrismaticHyperCard ? null : toReplace.hyperCard;

            newCard.initializeFormCards(base, veil, abyss, smoke, huge, hyper);
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