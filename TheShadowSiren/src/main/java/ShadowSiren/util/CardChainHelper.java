package ShadowSiren.util;

import ShadowSiren.cards.Barrage;
import ShadowSiren.cards.Ferment;
import ShadowSiren.cards.PocketDimension;
import ShadowSiren.cards.abstractCards.AbstractPrismaticCard;
import ShadowSiren.cards.prismaticCards.*;
import com.badlogic.gdx.Gdx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class CardChainHelper {
    private static final ArrayList<AbstractPrismaticCard> PocketChain = new ArrayList<>(Arrays.asList(new PocketDimension(), new PocketDimensionVeil(), new PocketDimensionAbyss(), new PocketDimensionSmoke(), new PocketDimensionHuge(), new PocketDimensionHyper()));
    private static final ArrayList<AbstractPrismaticCard> BarrageChain = new ArrayList<>(Arrays.asList(new Barrage(), new BarrageVeil(), new BarrageAbyss(), new BarrageSmoke(), new BarrageHuge(), new BarrageHyper()));
    private static final ArrayList<AbstractPrismaticCard> FermentChain = new ArrayList<>(Arrays.asList(new Ferment(), new FermentVeil(), new FermentAbyss(), new FermentSmoke(), new FermentHuge(), new FermentHyper()));
    private static final ArrayList<ArrayList<AbstractPrismaticCard>> masterList = new ArrayList<>(Arrays.asList(PocketChain, BarrageChain, FermentChain));
    private static final HashMap<ArrayList<AbstractPrismaticCard>, Float> timerMap = new HashMap<ArrayList<AbstractPrismaticCard>, Float>(){{put(PocketChain, 0f);put(BarrageChain, 0f);put(FermentChain, 0f);}};
    private static final HashMap<ArrayList<AbstractPrismaticCard>, Integer> stateMap = new HashMap<ArrayList<AbstractPrismaticCard>, Integer>(){{put(PocketChain, 0);put(BarrageChain, 0);put(FermentChain, 0);}};
    private static final float ROLLOVER = 3.0f;

    public static AbstractPrismaticCard findCardInList(AbstractPrismaticCard oldCard) {
        for (ArrayList<AbstractPrismaticCard> l : masterList) {
            for (AbstractPrismaticCard c : l) {
                if (oldCard.getClass() == c.getClass()) {
                    //Grab the appropriate timer and increment it
                    timerMap.put(l, timerMap.get(l) + Gdx.graphics.getDeltaTime());
                    //If we pass the rollover, increment the appropriate state
                    if (timerMap.get(l) > ROLLOVER) {
                        timerMap.put(l, 0f);
                        stateMap.put(l, (stateMap.get(l)+1)%l.size());
                    }
                    AbstractPrismaticCard preview = (AbstractPrismaticCard) l.get(stateMap.get(l)).makeStatEquivalentCopy();
                    preview.cardsToPreview = l.get((stateMap.get(l)+1)%l.size()).makeStatEquivalentCopy();
                    return preview;
                    //Grab the card from the correct spot
                    /*if (oldCard.getClass() == l.get(index % l.size()).getClass()) {
                        return oldCard;
                    } else {
                        return (AbstractPrismaticCard) l.get(index % l.size()).makeStatEquivalentCopy();
                    }*/
                }
            }
        }
        return oldCard;
    }
}
