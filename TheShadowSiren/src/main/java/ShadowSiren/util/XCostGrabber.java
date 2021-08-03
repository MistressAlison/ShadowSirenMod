package ShadowSiren.util;

import basemod.interfaces.XCostModifier;
import basemod.patches.com.megacrit.cardcrawl.cards.AbstractCard.CardModifierPatches;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

import java.util.ArrayList;
import java.util.List;

public class XCostGrabber {

    public static int getXCostAmount(AbstractCard card) {
        return getXCostAmount(card, false);
    }

    public static int getXCostAmount(AbstractCard card, boolean nonUseCall) {
        int effect = EnergyPanel.totalCount;

        if (!nonUseCall && card.energyOnUse != -1) {
            effect = card.energyOnUse;
        }

        ArrayList<List<?>> lists = new ArrayList<>();

        if (AbstractDungeon.player != null) {
            if (AbstractDungeon.player.hasRelic("Chemical X")) {
                effect += 2;
                if (!nonUseCall) {
                    AbstractDungeon.player.getRelic("Chemical X").flash();
                }
            }

            lists.add(AbstractDungeon.player.hand.group);
            lists.add(AbstractDungeon.player.drawPile.group);
            lists.add(AbstractDungeon.player.discardPile.group);
            lists.add(AbstractDungeon.player.powers);
            lists.add(AbstractDungeon.player.relics);
        }
        lists.add(CardModifierPatches.CardModifierFields.cardModifiers.get(card));
        for (List<?> list : lists) {
            for (Object item : list) {
                if (item instanceof XCostModifier) {
                    XCostModifier mod = (XCostModifier)item;
                    if (mod.xCostModifierActive(card)) {
                        effect += mod.modifyX(card);
                    }
                }
            }
        }
        return effect;
    }
}
