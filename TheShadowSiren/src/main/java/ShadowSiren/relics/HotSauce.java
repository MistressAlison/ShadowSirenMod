package ShadowSiren.relics;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.oldStuff.powers.BurnPower;
import ShadowSiren.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import static ShadowSiren.ShadowSirenMod.makeRelicOutlinePath;
import static ShadowSiren.ShadowSirenMod.makeRelicPath;

public class HotSauce extends CustomRelic {

    // ID, images, text.
    public static final String ID = ShadowSirenMod.makeID("HotSauce");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("HotSauce.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("HotSauce.png"));

    private static final int BURN = 1;

    HashMap<String, Integer> stats = new HashMap<>();
    private final String BURN_STAT = DESCRIPTIONS[2];
    private final String PER_COMBAT_STRING = DESCRIPTIONS[3];
    private final String PER_TURN_STRING = DESCRIPTIONS[4];

    public HotSauce() {
        super(ID, IMG, OUTLINE, RelicTier.COMMON, LandingSound.CLINK);
        resetStats();
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + BURN + DESCRIPTIONS[1];
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (info.owner != null && info.owner != AbstractDungeon.player) {
            flash();
            this.addToBot(new ApplyPowerAction(info.owner, AbstractDungeon.player, new BurnPower(info.owner, AbstractDungeon.player, BURN)));
            stats.put(BURN_STAT, stats.get(BURN_STAT) + BURN);
        }
        return super.onAttacked(info, damageAmount);
    }

    @Override
    public AbstractRelic makeCopy() {
        // Relic Stats will always query the stats from the instance passed to BaseMod.addRelic()
        // Therefore, we make sure all copies share the same stats by copying the HashMap.
        HotSauce newRelic = new HotSauce();
        newRelic.stats = this.stats;
        return newRelic;
    }

    public String getStatsDescription() {
        return BURN_STAT + stats.get(BURN_STAT);
    }

    public String getExtendedStatsDescription(int totalCombats, int totalTurns) {
        // You would just return getStatsDescription() if you don't want to display per-combat and per-turn stats
        StringBuilder builder = new StringBuilder();
        builder.append(getStatsDescription());
        float stat = (float)stats.get(BURN_STAT);
        // Relic Stats truncates these extended stats to 3 decimal places, so we do the same
        DecimalFormat perTurnFormat = new DecimalFormat("#.###");
        builder.append(PER_TURN_STRING);
        builder.append(perTurnFormat.format(stat / Math.max(totalTurns, 1)));
        builder.append(PER_COMBAT_STRING);
        builder.append(perTurnFormat.format(stat / Math.max(totalCombats, 1)));
        return builder.toString();
    }

    public void resetStats() {
        stats.put(BURN_STAT, 0);
    }

    public JsonElement onSaveStats() {
        // An array makes more sense if you want to store more than one stat
        Gson gson = new Gson();
        ArrayList<Integer> statsToSave = new ArrayList<>();
        statsToSave.add(stats.get(BURN_STAT));
        return gson.toJsonTree(statsToSave);
    }

    public void onLoadStats(JsonElement jsonElement) {
        if (jsonElement != null) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            stats.put(BURN_STAT, jsonArray.get(0).getAsInt());
        } else {
            resetStats();
        }
    }
}
