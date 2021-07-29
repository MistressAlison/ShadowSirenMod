package ShadowSiren.relics;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.oldStuff.cards.MarkingFist;
import ShadowSiren.oldStuff.cards.tempCards.Anchor;
import ShadowSiren.util.TextureLoader;
//import ShadowSiren.util.starStore.StarPieceShopScreen;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.shop.ShopScreen;

import java.util.ArrayList;
import java.util.HashMap;

import static ShadowSiren.ShadowSirenMod.makeRelicOutlinePath;
import static ShadowSiren.ShadowSirenMod.makeRelicPath;

public class StarPiece extends CustomRelic implements ClickableRelic {

    // ID, images, text.
    public static final String ID = ShadowSirenMod.makeID("StarPiece");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("StarPiece.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("StarPiece.png"));

    private ShopScreen shop;

    HashMap<String, Integer> stats = new HashMap<>();
    private final String PURCHASES_STAT = DESCRIPTIONS[1];

    public StarPiece() {
        super(ID, IMG, OUTLINE, RelicTier.STARTER, LandingSound.MAGICAL);
        counter = 0;
        shop = new ShopScreen();
        resetStats();
    }

    @Override
    public void onEnterRoom(AbstractRoom room) {
        this.flash();
        counter++;
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        // Relic Stats will always query the stats from the instance passed to BaseMod.addRelic()
        // Therefore, we make sure all copies share the same stats by copying the HashMap.
        StarPiece newRelic = new StarPiece();
        newRelic.stats = this.stats;
        return newRelic;
    }

    public String getStatsDescription() {
        return PURCHASES_STAT + stats.get(PURCHASES_STAT);
    }

    public String getExtendedStatsDescription(int totalCombats, int totalTurns) {
        // You would just return getStatsDescription() if you don't want to display per-combat and per-turn stats
        return getStatsDescription();
    }

    public void resetStats() {
        stats.put(PURCHASES_STAT, 0);
    }

    public JsonElement onSaveStats() {
        // An array makes more sense if you want to store more than one stat
        Gson gson = new Gson();
        ArrayList<Integer> statsToSave = new ArrayList<>();
        statsToSave.add(stats.get(PURCHASES_STAT));
        return gson.toJsonTree(statsToSave);
    }

    public void onLoadStats(JsonElement jsonElement) {
        if (jsonElement != null) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            stats.put(PURCHASES_STAT, jsonArray.get(0).getAsInt());
        } else {
            resetStats();
        }
    }

    /*@Override
    public ShopScreen onSave() {
        return null;
    }

    @Override
    public void onLoad(ShopScreen starPieceShopScreen) {
        if (starPieceShopScreen != null) {
            shop = starPieceShopScreen;
        }
    }*/

    @Override
    public void onRightClick() {
        ArrayList<AbstractCard> c1 = new ArrayList<>();
        ArrayList<AbstractCard> c2 = new ArrayList<>();
        c1.add(new MarkingFist());
        c1.add(new MarkingFist());
        c1.add(new MarkingFist());
        c1.add(new MarkingFist());
        c1.add(new MarkingFist());
        c2.add(new Anchor());
        c2.add(new Anchor());
        shop.init(c1, c2);
        this.addToTop(new AbstractGameAction() {
            @Override
            public void update() {
                shop.open();
                this.isDone = true;
            }
        });
    }

    /*@Override
    public Type savedType() {
        return new TypeToken<StarPieceShopScreen>(){}.getType();
    }*/
}