package ShadowSiren.relics;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.ArrayList;
import java.util.HashMap;

import static ShadowSiren.ShadowSirenMod.makeRelicOutlinePath;
import static ShadowSiren.ShadowSirenMod.makeRelicPath;

public class PointSwap extends CustomRelic {

    // ID, images, text.
    public static final String ID = ShadowSirenMod.makeID("PointSwap");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("PointSwap.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("PointSwap.png"));

    public boolean activated = false;
    public boolean firstTurn = false;
    HashMap<String, Integer> stats = new HashMap<>();
    private final String STEAL_STAT = DESCRIPTIONS[1];
    private final String NEGATE_STAT = DESCRIPTIONS[2];

    //TODO buggy
    public PointSwap() {
        super(ID, IMG, OUTLINE, RelicTier.BOSS, LandingSound.MAGICAL);
        resetStats();
        activated = true;
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void atPreBattle() {
        activated = true;
        firstTurn = true;
    }

    @Override
    public void atTurnStartPostDraw() {
        if (firstTurn) {
            activateRelic();
            firstTurn = false;
        }
    }

    @Override
    public void onVictory() {
        activated = true;
        grayscale = false;
    }

    public void onStealPower() {
        flash();
        deactivateRelic();
        this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        stats.put(STEAL_STAT, stats.get(STEAL_STAT) + 1);
    }

    public void onNegatePower() {
        flash();
        deactivateRelic();
        this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        stats.put(NEGATE_STAT, stats.get(NEGATE_STAT) + 1);
    }

    public boolean relicIsActive() {
        return !activated;
    }

    public void activateRelic() {
        activated = false;
        grayscale = false;
    }

    public void deactivateRelic() {
        activated = true;
        grayscale = true;
    }

    public String getStatsDescription() {
        return STEAL_STAT + stats.get(STEAL_STAT) + NEGATE_STAT + stats.get(NEGATE_STAT);
    }

    public String getExtendedStatsDescription(int totalCombats, int totalTurns) {
        // You would just return getStatsDescription() if you don't want to display per-combat and per-turn stats
        return getStatsDescription();
    }

    public void resetStats() {
        stats.put(STEAL_STAT, 0);
        stats.put(NEGATE_STAT, 0);
    }

    public JsonElement onSaveStats() {
        // An array makes more sense if you want to store more than one stat
        Gson gson = new Gson();
        ArrayList<Integer> statsToSave = new ArrayList<>();
        statsToSave.add(stats.get(STEAL_STAT));
        statsToSave.add(stats.get(NEGATE_STAT));
        return gson.toJsonTree(statsToSave);
    }

    public void onLoadStats(JsonElement jsonElement) {
        if (jsonElement != null) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            stats.put(STEAL_STAT, jsonArray.get(0).getAsInt());
            stats.put(NEGATE_STAT, jsonArray.get(1).getAsInt());
        } else {
            resetStats();
        }
    }

    @Override
    public AbstractRelic makeCopy() {
        // Relic Stats will always query the stats from the instance passed to BaseMod.addRelic()
        // Therefore, we make sure all copies share the same stats by copying the HashMap.
        PointSwap newRelic = new PointSwap();
        newRelic.stats = this.stats;
        return newRelic;
    }
}
