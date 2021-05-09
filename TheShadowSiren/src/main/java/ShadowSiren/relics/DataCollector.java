package ShadowSiren.relics;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import basemod.abstracts.CustomSavable;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import static ShadowSiren.ShadowSirenMod.makeRelicOutlinePath;
import static ShadowSiren.ShadowSirenMod.makeRelicPath;

public class DataCollector extends CustomRelic implements CustomSavable<Integer>, ClickableRelic {

    // ID, images, text.
    public static final String ID = ShadowSirenMod.makeID("DataCollector");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("SunStone.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("SunStone.png"));

    private static int mode;
    private static final int maxModes = 2;
    // Relic Stats truncates these extended stats to 3 decimal places, so we do the same
    private final DecimalFormat perTurnFormat = new DecimalFormat("#.###");

    HashMap<String, Integer> stats = new HashMap<>();
    private final String PER_COMBAT_STRING = DESCRIPTIONS[2];
    private final String PER_TURN_STRING = DESCRIPTIONS[3];
    private final String PER_STACK_STRING = DESCRIPTIONS[4];
    private final String AVERAGE_STRING = DESCRIPTIONS[5];
    private final String BURN_APPLY_STAT = DESCRIPTIONS[6];
    private final String BURN_DAMAGE_STAT = DESCRIPTIONS[7];
    private final String HEX_APPLY_STAT = DESCRIPTIONS[8];
    private final String HEX_DAMAGE_STAT = DESCRIPTIONS[9];
    private final String SOFT_APPLY_STAT = DESCRIPTIONS[10];
    private final String SOFT_DAMAGE_STAT = DESCRIPTIONS[11];
    private final String CHILL_APPLY_STAT = DESCRIPTIONS[12];
    private final String CHILL_TO_FREEZE_STAT = DESCRIPTIONS[13];
    private final String FREEZE_APPLY_STAT = DESCRIPTIONS[14];
    private final String DIZZY_APPLY_STAT = DESCRIPTIONS[15];
    private final String DIZZY_MISS_STAT = DESCRIPTIONS[16];
    private final String CONFUSE_APPLY_STAT = DESCRIPTIONS[17];
    private final String CONFUSE_REDIRECT_STAT = DESCRIPTIONS[18];
    private final String ELEC_GAIN_STAT = DESCRIPTIONS[19];
    private final String ELEC_DIRECT_STAT = DESCRIPTIONS[20];
    private final String ELEC_INDIRECT_STAT = DESCRIPTIONS[21];

    public void onApplyBurn(int amount) {
        stats.put(BURN_APPLY_STAT, stats.get(BURN_APPLY_STAT) + amount);
    }
    public void onBurnDamage(int amount) {
        stats.put(BURN_DAMAGE_STAT, stats.get(BURN_DAMAGE_STAT) + amount);
    }
    public void onApplyHex(int amount) {
        stats.put(HEX_APPLY_STAT, stats.get(HEX_APPLY_STAT) + amount);
    }
    public void onHexDamage(int amount) {
        stats.put(HEX_DAMAGE_STAT, stats.get(HEX_DAMAGE_STAT) + amount);
    }
    public void onApplySoft(int amount) {
        stats.put(SOFT_APPLY_STAT, stats.get(SOFT_DAMAGE_STAT) + amount);
    }
    public void onSoftDamage(int amount) {
        stats.put(SOFT_DAMAGE_STAT, stats.get(SOFT_DAMAGE_STAT) + amount);
    }
    public void onApplyChill(int amount) {
        stats.put(CHILL_APPLY_STAT, stats.get(CHILL_APPLY_STAT) + amount);
    }
    public void onChillRollover(int amount) {
        stats.put(CHILL_TO_FREEZE_STAT, stats.get(CHILL_TO_FREEZE_STAT) + amount);
    }
    public void onApplyFreeze(int amount) {
        stats.put(FREEZE_APPLY_STAT, stats.get(FREEZE_APPLY_STAT) + amount);
    }
    public void onApplyDizzy(int amount) {
        stats.put(DIZZY_APPLY_STAT, stats.get(DIZZY_APPLY_STAT) + amount);
    }
    public void onDizzyMiss(int amount) {
        stats.put(DIZZY_MISS_STAT, stats.get(DIZZY_MISS_STAT) + amount);
    }
    public void onApplyConfused(int amount) {
        stats.put(CONFUSE_APPLY_STAT, stats.get(CONFUSE_APPLY_STAT) + amount);
    }
    public void onConfusedDamage(int amount) {
        stats.put(CONFUSE_REDIRECT_STAT, stats.get(CONFUSE_REDIRECT_STAT) + amount);
    }
    public void onGainElec(int amount) {
        stats.put(ELEC_GAIN_STAT, stats.get(ELEC_GAIN_STAT) + amount);
    }
    public void onElecDirect(int amount) {
        stats.put(ELEC_DIRECT_STAT, stats.get(ELEC_DIRECT_STAT) + amount);
    }
    public void onElecIndirect(int amount) {
        stats.put(ELEC_INDIRECT_STAT, stats.get(ELEC_INDIRECT_STAT) + amount);
    }

    public DataCollector() {
        super(ID, IMG, OUTLINE, RelicTier.STARTER, LandingSound.HEAVY);
        resetStats();
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    public void setDescriptionAfterLoading() {
        this.description = this.DESCRIPTIONS[mode];
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        this.initializeTips();
    }

    @Override
    public AbstractRelic makeCopy() {
        // Relic Stats will always query the stats from the instance passed to BaseMod.addRelic()
        // Therefore, we make sure all copies share the same stats by copying the HashMap.
        DataCollector newRelic = new DataCollector();
        newRelic.stats = this.stats;
        return newRelic;
    }

    public String getStatsDescription() {
        StringBuilder builder = new StringBuilder();
        if (mode == 0) {
            builder.append(BURN_APPLY_STAT).append(stats.get(BURN_APPLY_STAT))
                    .append(BURN_DAMAGE_STAT).append(stats.get(BURN_DAMAGE_STAT))
                    .append(HEX_APPLY_STAT).append(stats.get(HEX_APPLY_STAT))
                    .append(HEX_DAMAGE_STAT).append(stats.get(HEX_DAMAGE_STAT))
                    .append(SOFT_APPLY_STAT).append(stats.get(SOFT_APPLY_STAT))
                    .append(SOFT_DAMAGE_STAT).append(stats.get(SOFT_DAMAGE_STAT))
                    .append(ELEC_GAIN_STAT).append(stats.get(ELEC_GAIN_STAT))
                    .append(ELEC_DIRECT_STAT).append(stats.get(ELEC_DIRECT_STAT))
                    .append(ELEC_INDIRECT_STAT).append(stats.get(ELEC_INDIRECT_STAT));
        } else if (mode == 1) {
            builder.append(CHILL_APPLY_STAT).append(stats.get(CHILL_APPLY_STAT))
                    .append(CHILL_TO_FREEZE_STAT).append(stats.get(CHILL_TO_FREEZE_STAT))
                    .append(FREEZE_APPLY_STAT).append(stats.get(FREEZE_APPLY_STAT))
                    .append(DIZZY_APPLY_STAT).append(stats.get(DIZZY_APPLY_STAT))
                    .append(DIZZY_MISS_STAT).append(stats.get(DIZZY_MISS_STAT))
                    .append(CONFUSE_APPLY_STAT).append(stats.get(CONFUSE_APPLY_STAT))
                    .append(CONFUSE_REDIRECT_STAT).append(stats.get(CONFUSE_REDIRECT_STAT));
        }
        return builder.toString();
    }

    public String getExtendedStatsDescription(int totalCombats, int totalTurns) {
        // You would just return getStatsDescription() if you don't want to display per-combat and per-turn stats
        StringBuilder builder = new StringBuilder();
        if (mode == 0) {

            appendData(builder, BURN_APPLY_STAT, totalCombats, totalTurns);

            appendData(builder, BURN_DAMAGE_STAT, totalCombats, totalTurns);
            builder.append(PER_STACK_STRING);
            builder.append(perTurnFormat.format((float)stats.get(BURN_DAMAGE_STAT) / Math.max(1,stats.get(BURN_APPLY_STAT))));

            appendData(builder, HEX_APPLY_STAT, totalCombats, totalTurns);

            appendData(builder, HEX_DAMAGE_STAT, totalCombats, totalTurns);

            appendData(builder, SOFT_APPLY_STAT, totalCombats, totalTurns);

            appendData(builder, SOFT_DAMAGE_STAT, totalCombats, totalTurns);

            appendData(builder, ELEC_GAIN_STAT, totalCombats, totalTurns);

            appendData(builder, ELEC_DIRECT_STAT, totalCombats, totalTurns);

            appendData(builder, ELEC_INDIRECT_STAT, totalCombats, totalTurns);

        } else if (mode == 1) {

            appendData(builder, CHILL_APPLY_STAT, totalCombats, totalTurns);

            appendData(builder, CHILL_TO_FREEZE_STAT, totalCombats, totalTurns);

            appendData(builder, FREEZE_APPLY_STAT, totalCombats, totalTurns);

            appendData(builder, DIZZY_APPLY_STAT, totalCombats, totalTurns);

            appendData(builder, DIZZY_MISS_STAT, totalCombats, totalTurns);

            appendData(builder, CONFUSE_APPLY_STAT, totalCombats, totalTurns);

            appendData(builder, CONFUSE_REDIRECT_STAT, totalCombats, totalTurns);

        }

        return builder.toString();
    }

    private void appendData(StringBuilder builder, String data, int totalCombats, int totalTurns) {
        float stat = (float)stats.get(data);
        builder.append(data).append(stats.get(data));
        builder.append(PER_COMBAT_STRING);
        builder.append(perTurnFormat.format(stat / Math.max(totalCombats, 1)));
        builder.append(PER_TURN_STRING);
        builder.append(perTurnFormat.format(stat / Math.max(totalTurns, 1)));
    }

    public void resetStats() {
        stats.put(BURN_APPLY_STAT, 0);
        stats.put(BURN_DAMAGE_STAT, 0);
        stats.put(HEX_APPLY_STAT, 0);
        stats.put(HEX_DAMAGE_STAT, 0);
        stats.put(SOFT_APPLY_STAT, 0);
        stats.put(SOFT_DAMAGE_STAT, 0);
        stats.put(CHILL_APPLY_STAT, 0);
        stats.put(CHILL_TO_FREEZE_STAT, 0);
        stats.put(FREEZE_APPLY_STAT, 0);
        stats.put(DIZZY_APPLY_STAT, 0);
        stats.put(DIZZY_MISS_STAT, 0);
        stats.put(CONFUSE_APPLY_STAT, 0);
        stats.put(CONFUSE_REDIRECT_STAT, 0);
        stats.put(ELEC_GAIN_STAT, 0);
        stats.put(ELEC_DIRECT_STAT, 0);
        stats.put(ELEC_INDIRECT_STAT, 0);
    }

    public JsonElement onSaveStats() {
        // An array makes more sense if you want to store more than one stat
        Gson gson = new Gson();
        ArrayList<Integer> statsToSave = new ArrayList<>();
        statsToSave.add(stats.get(BURN_APPLY_STAT));
        statsToSave.add(stats.get(BURN_DAMAGE_STAT));
        statsToSave.add(stats.get(HEX_APPLY_STAT));
        statsToSave.add(stats.get(HEX_DAMAGE_STAT));
        statsToSave.add(stats.get(SOFT_APPLY_STAT));
        statsToSave.add(stats.get(SOFT_DAMAGE_STAT));
        statsToSave.add(stats.get(CHILL_APPLY_STAT));
        statsToSave.add(stats.get(CHILL_TO_FREEZE_STAT));
        statsToSave.add(stats.get(FREEZE_APPLY_STAT));
        statsToSave.add(stats.get(DIZZY_APPLY_STAT));
        statsToSave.add(stats.get(DIZZY_MISS_STAT));
        statsToSave.add(stats.get(CONFUSE_APPLY_STAT));
        statsToSave.add(stats.get(CONFUSE_REDIRECT_STAT));
        statsToSave.add(stats.get(ELEC_GAIN_STAT));
        statsToSave.add(stats.get(ELEC_DIRECT_STAT));
        statsToSave.add(stats.get(ELEC_INDIRECT_STAT));
        return gson.toJsonTree(statsToSave);
    }

    public void onLoadStats(JsonElement jsonElement) {
        if (jsonElement != null) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            stats.put(BURN_APPLY_STAT, jsonArray.get(0).getAsInt());
            stats.put(BURN_DAMAGE_STAT, jsonArray.get(1).getAsInt());
            stats.put(HEX_APPLY_STAT, jsonArray.get(2).getAsInt());
            stats.put(HEX_DAMAGE_STAT, jsonArray.get(3).getAsInt());
            stats.put(SOFT_APPLY_STAT, jsonArray.get(4).getAsInt());
            stats.put(SOFT_DAMAGE_STAT, jsonArray.get(5).getAsInt());
            stats.put(CHILL_APPLY_STAT, jsonArray.get(6).getAsInt());
            stats.put(CHILL_TO_FREEZE_STAT, jsonArray.get(7).getAsInt());
            stats.put(FREEZE_APPLY_STAT, jsonArray.get(8).getAsInt());
            stats.put(DIZZY_APPLY_STAT, jsonArray.get(9).getAsInt());
            stats.put(DIZZY_MISS_STAT, jsonArray.get(10).getAsInt());
            stats.put(CONFUSE_APPLY_STAT, jsonArray.get(11).getAsInt());
            stats.put(CONFUSE_REDIRECT_STAT, jsonArray.get(12).getAsInt());
            stats.put(ELEC_GAIN_STAT, jsonArray.get(13).getAsInt());
            stats.put(ELEC_DIRECT_STAT, jsonArray.get(14).getAsInt());
            stats.put(ELEC_INDIRECT_STAT, jsonArray.get(15).getAsInt());
        } else {
            resetStats();
        }
    }

    @Override
    public Integer onSave() {
        return mode;
    }

    @Override
    public void onLoad(Integer integer) {
        if (integer != null) {
            mode = integer;
        } else {
            mode = 0;
        }
        setDescriptionAfterLoading();
    }

    @Override
    public void onRightClick() {
        mode++;
        mode %= maxModes;
        setDescriptionAfterLoading();
    }
}
