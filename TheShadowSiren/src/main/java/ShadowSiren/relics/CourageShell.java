package ShadowSiren.relics;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import static ShadowSiren.ShadowSirenMod.makeRelicOutlinePath;
import static ShadowSiren.ShadowSirenMod.makeRelicPath;

public class CourageShell extends CustomRelic {

    // ID, images, text.
    public static final String ID = ShadowSirenMod.makeID("CourageShell");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("CourageShell.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("CourageShell.png"));

    private static final int DEX = 3;
    private boolean removed = false;

    HashMap<String, Integer> stats = new HashMap<>();
    private final String TURNS_STAT = DESCRIPTIONS[2];
    private final String PER_COMBAT_STRING = DESCRIPTIONS[3];
    private final String PER_TURN_STRING = DESCRIPTIONS[4];

    public CourageShell() {
        super(ID, IMG, OUTLINE, RelicTier.UNCOMMON, LandingSound.HEAVY);
        resetStats();
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + DEX + DESCRIPTIONS[1];
    }

    @Override
    public void onLoseHp(int damageAmount) {
        if (damageAmount > 0 && !removed) {
            removed = true;
            this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DexterityPower(AbstractDungeon.player, -DEX), -DEX, true));
        }
    }

    @Override
    public void atBattleStart() {
        flash();
        this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DexterityPower(AbstractDungeon.player, DEX), DEX, true));
        removed = false;
    }

    @Override
    public void atTurnStart() {
        super.atTurnStart();
        if (!removed) {
            stats.put(TURNS_STAT, stats.get(TURNS_STAT) + 1);
        }
    }

    @Override
    public AbstractRelic makeCopy() {
        // Relic Stats will always query the stats from the instance passed to BaseMod.addRelic()
        // Therefore, we make sure all copies share the same stats by copying the HashMap.
        CourageShell newRelic = new CourageShell();
        newRelic.stats = this.stats;
        return newRelic;
    }

    public String getStatsDescription() {
        return TURNS_STAT + stats.get(TURNS_STAT);
    }

    public String getExtendedStatsDescription(int totalCombats, int totalTurns) {
        // You would just return getStatsDescription() if you don't want to display per-combat and per-turn stats
        StringBuilder builder = new StringBuilder();
        builder.append(getStatsDescription());
        float stat = (float)stats.get(TURNS_STAT);
        // Relic Stats truncates these extended stats to 3 decimal places, so we do the same
        DecimalFormat perTurnFormat = new DecimalFormat("#.###");
        builder.append(PER_COMBAT_STRING);
        builder.append(perTurnFormat.format(stat / Math.max(totalCombats, 1)));
        builder.append(PER_TURN_STRING);
        builder.append(perTurnFormat.format(stat * 100 / Math.max(totalTurns, 1))).append("%");
        return builder.toString();
    }

    public void resetStats() {
        stats.put(TURNS_STAT, 0);
    }

    public JsonElement onSaveStats() {
        // An array makes more sense if you want to store more than one stat
        Gson gson = new Gson();
        ArrayList<Integer> statsToSave = new ArrayList<>();
        statsToSave.add(stats.get(TURNS_STAT));
        return gson.toJsonTree(statsToSave);
    }

    public void onLoadStats(JsonElement jsonElement) {
        if (jsonElement != null) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            stats.put(TURNS_STAT, jsonArray.get(0).getAsInt());
        } else {
            resetStats();
        }
    }
}
