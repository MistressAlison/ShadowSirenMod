package ShadowSiren.relics;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.powers.SoftPower;
import ShadowSiren.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import static ShadowSiren.ShadowSirenMod.makeRelicOutlinePath;
import static ShadowSiren.ShadowSirenMod.makeRelicPath;

public class RuinPowder extends CustomRelic {

    // ID, images, text.
    public static final String ID = ShadowSirenMod.makeID("RuinPowder");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("RuinPowder.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("RuinPowder.png"));

    public static final int RELIC_BONUS_DAMAGE = 5;

    HashMap<String, Integer> stats = new HashMap<>();
    private final String DAMAGE_STAT = DESCRIPTIONS[3];
    private final String PER_COMBAT_STRING = DESCRIPTIONS[4];
    private final String PER_TURN_STRING = DESCRIPTIONS[5];

    public RuinPowder() {
        super(ID, IMG, OUTLINE, RelicTier.COMMON, LandingSound.FLAT);
        resetStats();
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + RELIC_BONUS_DAMAGE + DESCRIPTIONS[1] + SoftPower.BONUS_DAMAGE + DESCRIPTIONS[2];
    }

    //functionally handled by SoftPower
    public void onIncreaseDamage(int damageIncrease) {
        stats.put(DAMAGE_STAT, stats.get(DAMAGE_STAT) + damageIncrease);
    }

    public String getStatsDescription() {
        return DAMAGE_STAT + stats.get(DAMAGE_STAT);
    }

    public String getExtendedStatsDescription(int totalCombats, int totalTurns) {
        // You would just return getStatsDescription() if you don't want to display per-combat and per-turn stats
        StringBuilder builder = new StringBuilder();
        builder.append(getStatsDescription());
        float stat = (float)stats.get(DAMAGE_STAT);
        // Relic Stats truncates these extended stats to 3 decimal places, so we do the same
        DecimalFormat perTurnFormat = new DecimalFormat("#.###");
        builder.append(PER_TURN_STRING);
        builder.append(perTurnFormat.format(stat / Math.max(totalTurns, 1)));
        builder.append(PER_COMBAT_STRING);
        builder.append(perTurnFormat.format(stat / Math.max(totalCombats, 1)));
        return builder.toString();
    }

    public void resetStats() {
        stats.put(DAMAGE_STAT, 0);
    }

    public JsonElement onSaveStats() {
        // An array makes more sense if you want to store more than one stat
        Gson gson = new Gson();
        ArrayList<Integer> statsToSave = new ArrayList<>();
        statsToSave.add(stats.get(DAMAGE_STAT));
        return gson.toJsonTree(statsToSave);
    }

    public void onLoadStats(JsonElement jsonElement) {
        if (jsonElement != null) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            stats.put(DAMAGE_STAT, jsonArray.get(0).getAsInt());
        } else {
            resetStats();
        }
    }

    @Override
    public AbstractRelic makeCopy() {
        // Relic Stats will always query the stats from the instance passed to BaseMod.addRelic()
        // Therefore, we make sure all copies share the same stats by copying the HashMap.
        RuinPowder newRelic = new RuinPowder();
        newRelic.stats = this.stats;
        return newRelic;
    }
}
