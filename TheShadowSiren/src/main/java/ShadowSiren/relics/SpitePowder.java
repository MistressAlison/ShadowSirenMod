package ShadowSiren.relics;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import static ShadowSiren.ShadowSirenMod.makeRelicOutlinePath;
import static ShadowSiren.ShadowSirenMod.makeRelicPath;

public class SpitePowder extends CustomRelic {

    // ID, images, text.
    public static final String ID = ShadowSirenMod.makeID("SpitePowder");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("SpitePowder.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("SpitePowder.png"));

    private static final int DAMAGE_PERCENT = 50;

    HashMap<String, Integer> stats = new HashMap<>();
    private final String DAMAGE_STAT = DESCRIPTIONS[2];
    private final String PER_COMBAT_STRING = DESCRIPTIONS[3];
    private final String PER_TURN_STRING = DESCRIPTIONS[4];

    public SpitePowder() {
        super(ID, IMG, OUTLINE, RelicTier.RARE, LandingSound.FLAT);
        resetStats();
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + DAMAGE_PERCENT + DESCRIPTIONS[1];
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (damageAmount > 0 && info.owner != null && info.owner != AbstractDungeon.player) {
            flash();
            this.addToBot(new RelicAboveCreatureAction(info.owner, this));
            this.addToBot(new DamageAction(info.owner, new DamageInfo(AbstractDungeon.player, damageAmount*DAMAGE_PERCENT/100, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.SLASH_DIAGONAL, true));
            stats.put(DAMAGE_STAT, stats.get(DAMAGE_STAT) + damageAmount*DAMAGE_PERCENT/100);
        }
        return super.onAttacked(info, damageAmount);
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
        SpitePowder newRelic = new SpitePowder();
        newRelic.stats = this.stats;
        return newRelic;
    }
}
