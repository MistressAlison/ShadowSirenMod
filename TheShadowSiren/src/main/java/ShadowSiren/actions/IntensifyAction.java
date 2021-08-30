package ShadowSiren.actions;

import ShadowSiren.cards.WildMagic;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class IntensifyAction extends AbstractGameAction {
    public enum EffectType {
        DAMAGE,
        BLOCK,
        MAGIC
    }
    AbstractCard card;
    EffectType type;

    public IntensifyAction(AbstractCard card, int amount, EffectType type) {
        this.amount = amount;
        this.card = card;
        this.type = type;
    }

    @Override
    public void update() {
        switch (type) {
            case DAMAGE:
                card.baseDamage += amount;
                card.applyPowers();

                for(AbstractCard c : AbstractDungeon.player.discardPile.group) {
                    if (c.getClass().equals(card.getClass())) {
                        c.baseDamage += amount;
                        c.applyPowers();
                    }
                }

                for (AbstractCard c: AbstractDungeon.player.drawPile.group) {
                    if (c.getClass().equals(card.getClass())) {
                        c.baseDamage += amount;
                        c.applyPowers();
                    }
                }

                for (AbstractCard c: AbstractDungeon.player.hand.group) {
                    if (c.getClass().equals(card.getClass())) {
                        c.baseDamage += amount;
                        c.applyPowers();
                    }
                }
                break;
            case BLOCK:
                card.baseBlock += amount;
                card.applyPowers();

                for(AbstractCard c : AbstractDungeon.player.discardPile.group) {
                    if (c.getClass().equals(card.getClass())) {
                        c.baseBlock += amount;
                        c.applyPowers();
                    }
                }

                for (AbstractCard c: AbstractDungeon.player.drawPile.group) {
                    if (c.getClass().equals(card.getClass())) {
                        c.baseBlock += amount;
                        c.applyPowers();
                    }
                }

                for (AbstractCard c: AbstractDungeon.player.hand.group) {
                    if (c.getClass().equals(card.getClass())) {
                        c.baseBlock += amount;
                        c.applyPowers();
                    }
                }
                break;
            case MAGIC:
                card.baseMagicNumber += amount;
                card.magicNumber += amount;
                card.applyPowers();

                for(AbstractCard c : AbstractDungeon.player.discardPile.group) {
                    if (c.getClass().equals(card.getClass())) {
                        c.baseMagicNumber += amount;
                        c.magicNumber += amount;
                        c.applyPowers();
                    }
                }

                for (AbstractCard c: AbstractDungeon.player.drawPile.group) {
                    if (c.getClass().equals(card.getClass())) {
                        c.baseMagicNumber += amount;
                        c.magicNumber += amount;
                        c.applyPowers();
                    }
                }

                for (AbstractCard c: AbstractDungeon.player.hand.group) {
                    if (c.getClass().equals(card.getClass())) {
                        c.baseMagicNumber += amount;
                        c.magicNumber += amount;
                        c.applyPowers();
                    }
                }
                break;
        }

        this.isDone = true;
    }
}
