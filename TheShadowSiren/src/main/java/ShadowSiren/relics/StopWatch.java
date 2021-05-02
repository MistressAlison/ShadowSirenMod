package ShadowSiren.relics;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.powers.FreezePower;
import ShadowSiren.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.actions.common.StunMonsterAction;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import static ShadowSiren.ShadowSirenMod.makeRelicOutlinePath;
import static ShadowSiren.ShadowSirenMod.makeRelicPath;

public class StopWatch extends CustomRelic {

    // ID, images, text.
    public static final String ID = ShadowSirenMod.makeID("StopWatch");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("StopWatch.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("StopWatch.png"));

    public boolean activated = false;
    HashMap<String, Integer> stats = new HashMap<>();
    private final String GOLD_STAT = DESCRIPTIONS[1];

    public StopWatch() {
        super(ID, IMG, OUTLINE, RelicTier.RARE, LandingSound.MAGICAL);
        resetStats();
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void atBattleStart() {
        activated = false;
        beginLongPulse();
    }

    //Functionally handled by StopWatchPatch
    public void activate(AbstractMonster m) {
        activated = true;
        if (m.getIntentBaseDmg() > 0) {
            flash();
            this.addToBot(new RelicAboveCreatureAction(m, this));
            this.addToBot(new ApplyPowerAction(m, AbstractDungeon.player, new FreezePower(m, 1)));
            stats.put(GOLD_STAT, stats.get(GOLD_STAT) + 1);
            //this.addToBot(new StunMonsterAction(m, AbstractDungeon.player));
        }
        stopPulse();
    }

    public String getStatsDescription() {
        return GOLD_STAT + stats.get(GOLD_STAT);
    }

    public String getExtendedStatsDescription(int totalCombats, int totalTurns) {
        // You would just return getStatsDescription() if you don't want to display per-combat and per-turn stats
        return getStatsDescription();
    }

    public void resetStats() {
        stats.put(GOLD_STAT, 0);
    }

    public JsonElement onSaveStats() {
        // An array makes more sense if you want to store more than one stat
        Gson gson = new Gson();
        ArrayList<Integer> statsToSave = new ArrayList<>();
        statsToSave.add(stats.get(GOLD_STAT));
        return gson.toJsonTree(statsToSave);
    }

    public void onLoadStats(JsonElement jsonElement) {
        if (jsonElement != null) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            stats.put(GOLD_STAT, jsonArray.get(0).getAsInt());
        } else {
            resetStats();
        }
    }

    @Override
    public AbstractRelic makeCopy() {
        // Relic Stats will always query the stats from the instance passed to BaseMod.addRelic()
        // Therefore, we make sure all copies share the same stats by copying the HashMap.
        StopWatch newRelic = new StopWatch();
        newRelic.stats = this.stats;
        return newRelic;
    }
}
