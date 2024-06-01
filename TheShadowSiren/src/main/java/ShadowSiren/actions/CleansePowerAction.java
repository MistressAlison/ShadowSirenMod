package ShadowSiren.actions;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cards.abstractCards.AbstractPowerCard;
import com.evacipated.cardcrawl.mod.stslib.patches.NeutralPowertypePatch;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class CleansePowerAction extends AbstractGameAction {
    private static final String[] TEXT = CardCrawlGame.languagePack.getUIString(ShadowSirenMod.makeID("CleanseAction")).TEXT;
    private final Predicate<AbstractPower> filter;
    private final Consumer<ArrayList<AbstractPower>> callBack;

    public CleansePowerAction(AbstractCreature target, int amount, Predicate<AbstractPower> filter) {
        this(target, amount, filter, l -> {});
    }

    public CleansePowerAction(AbstractCreature target, int amount, Predicate<AbstractPower> filter, Consumer<ArrayList<AbstractPower>> callBack) {
        this.target = target;
        this.amount = amount;
        this.filter = filter;
        this.callBack = callBack;
    }


    @Override
    public void update() {
        if (amount <= 0 || target.powers.stream().noneMatch(filter)) {
            this.isDone = true;
            return;
        }
        if (amount >= target.powers.stream().filter(filter).count()) {
            ArrayList<AbstractPower> removedPowers = new ArrayList<>();
            for (AbstractPower pow : target.powers) {
                if (filter.test(pow)) {
                    addToTop(new RemoveSpecificPowerAction(target, target, pow));
                    removedPowers.add(pow);
                }
            }
            callBack.accept(removedPowers);
        } else {
            ArrayList<AbstractCard> validPowerCards = new ArrayList<>();
            HashMap<AbstractCard, AbstractPower> powerMap = new HashMap<>();
            for (AbstractPower pow : target.powers) {
                if (!(pow instanceof InvisiblePower) && pow.type != NeutralPowertypePatch.NEUTRAL && filter.test(pow)) {
                    AbstractCard card = new AbstractPowerCard(pow){};
                    validPowerCards.add(card);
                    powerMap.put(card, pow);
                }
            }
            addToTop(new BetterSelectCardsCenteredAction(validPowerCards, amount, amount == 1 ? TEXT[1] : TEXT[2] + amount + TEXT[3], cards -> {
                ArrayList<AbstractPower> removedPowers = new ArrayList<>();
                for (AbstractCard card : cards) {
                    addToTop(new RemoveSpecificPowerAction(target, target, powerMap.get(card)));
                    removedPowers.add(powerMap.get(card));
                }
                callBack.accept(removedPowers);
            }));
        }
        this.isDone = true;
    }
}
