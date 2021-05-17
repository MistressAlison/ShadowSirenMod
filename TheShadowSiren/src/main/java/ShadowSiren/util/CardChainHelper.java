package ShadowSiren.util;

import ShadowSiren.cards.*;
import ShadowSiren.cards.abstractCards.AbstractPrismaticCard;
import ShadowSiren.cards.interfaces.MultiCardPreviewHack;
import ShadowSiren.cards.prismaticCards.*;
import ShadowSiren.cards.tempCards.Pummel;
import ShadowSiren.cards.tempCards.Throw;
import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.google.gson.internal.$Gson$Preconditions;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class CardChainHelper {
    private static final ArrayList<AbstractCard> PocketChain = new ArrayList<>(Arrays.asList(new PocketDimension(), new PocketDimensionVeil(), new PocketDimensionAbyss(), new PocketDimensionSmoke(), new PocketDimensionHuge(), new PocketDimensionHyper()));
    private static final ArrayList<AbstractCard> BarrageChain = new ArrayList<>(Arrays.asList(new Barrage(), new BarrageVeil(), new BarrageAbyss(), new BarrageSmoke(), new BarrageHuge(), new BarrageHyper()));
    private static final ArrayList<AbstractCard> FermentChain = new ArrayList<>(Arrays.asList(new Ferment(), new FermentVeil(), new FermentAbyss(), new FermentSmoke(), new FermentHuge(), new FermentHyper()));
    private static final ArrayList<AbstractCard> DanceChain = new ArrayList<>(Arrays.asList(new StanceDance(), new StanceDanceVeil(), new StanceDanceAbyss(), new StanceDanceSmoke(), new StanceDanceHuge(), new StanceDanceHyper()));
    private static final ArrayList<AbstractCard> GrabChain = new ArrayList<>(Arrays.asList(new Grab(), new Pummel(), new Throw()));
    private static final ArrayList<ArrayList<AbstractCard>> masterList = new ArrayList<>(Arrays.asList(PocketChain, BarrageChain, FermentChain, DanceChain, GrabChain));
    private static final HashMap<ArrayList<AbstractCard>, Float> timerMap = new HashMap<ArrayList<AbstractCard>, Float>(){{put(PocketChain, 0f);put(BarrageChain, 0f);put(FermentChain, 0f);put(DanceChain, 0f);put(GrabChain, 0f);}};
    private static final HashMap<ArrayList<AbstractCard>, Integer> stateMap = new HashMap<ArrayList<AbstractCard>, Integer>(){{put(PocketChain, 0);put(BarrageChain, 0);put(FermentChain, 0);put(DanceChain, 0);put(GrabChain, 0);}};
    private static final float ROLLOVER = 3.0f;

    public static AbstractCard findCardInList(AbstractCard oldCard) {
        for (ArrayList<AbstractCard> l : masterList) {
            for (AbstractCard c : l) {
                if (oldCard.getClass() == c.getClass()) {
                    //Grab the appropriate timer and increment it
                    timerMap.put(l, timerMap.get(l) + Gdx.graphics.getDeltaTime());
                    //If we pass the rollover, increment the appropriate state
                    if (timerMap.get(l) > ROLLOVER) {
                        timerMap.put(l, 0f);
                        stateMap.put(l, (stateMap.get(l)+1)%l.size());
                    }
                    //Returning the original card breaks upgrade preview because god damn it why
                    if (!SingleCardViewPopup.isViewingUpgrade && oldCard.getClass().equals(l.get(stateMap.get(l)).getClass())) {
                        return oldCard;
                    } else {
                        //Peak inefficiency
                        AbstractCard preview = l.get(stateMap.get(l)).makeStatEquivalentCopy();
                        preview.cardsToPreview = l.get((stateMap.get(l)+1)%l.size()).makeStatEquivalentCopy();
                        return preview;
                    }
                }
            }
        }
        return oldCard;
    }

    @SpirePatch(clz = SingleCardViewPopup.class, method = "update")
    public static class UpdateListener {
        static boolean reload;
        @SpirePrefixPatch
        public static void updateListener(SingleCardViewPopup __instance, @ByRef AbstractCard[] ___card) {
            if (___card[0] instanceof MultiCardPreviewHack) {
                if (reload) {
                    ReflectionHacks.RMethod m = ReflectionHacks.privateMethod(__instance.getClass(), "loadPortraitImg");
                    m.invoke(__instance);
                }
                AbstractCard c = findCardInList(___card[0]);
                reload = !___card[0].getClass().equals(c.getClass());
                if (___card[0] != c) {
                    ___card[0] = c;
                }
            }
        }
    }
}
