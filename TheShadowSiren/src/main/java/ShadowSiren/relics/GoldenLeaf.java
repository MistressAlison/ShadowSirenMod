package ShadowSiren.relics;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.powers.FreezePower;
import ShadowSiren.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import static ShadowSiren.ShadowSirenMod.makeRelicOutlinePath;
import static ShadowSiren.ShadowSirenMod.makeRelicPath;

public class GoldenLeaf extends CustomRelic {

    // ID, images, text.
    public static final String ID = ShadowSirenMod.makeID("GoldenLeaf");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("GoldenLeaf.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("GoldenLeaf.png"));

    private static final int BONUS_GOLD = 2;
    private static final int DAMAGE_REQUIRED = 5;

    HashMap<String, Integer> stats = new HashMap<>();
    private final String GOLD_STAT = DESCRIPTIONS[3];
    private final String PER_COMBAT_STRING = DESCRIPTIONS[4];
    private final String PER_TURN_STRING = DESCRIPTIONS[5];

    public GoldenLeaf() {
        super(ID, IMG, OUTLINE, RelicTier.COMMON, LandingSound.FLAT);
        resetStats();
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + DAMAGE_REQUIRED + DESCRIPTIONS[1] + BONUS_GOLD + DESCRIPTIONS[2];
    }

    @Override
    public void atBattleStart() {
        this.counter = 0;
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (damageAmount >= DAMAGE_REQUIRED && info.type == DamageInfo.DamageType.NORMAL) {
            counter += BONUS_GOLD;
            stats.put(GOLD_STAT, stats.get(GOLD_STAT) + BONUS_GOLD);
            flash();
        }
    }

    @Override
    public void onVictory() {
        if (counter > 0) {
            flash();
            this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        }
    }

    public String getStatsDescription() {
        return GOLD_STAT + stats.get(GOLD_STAT);
    }

    public String getExtendedStatsDescription(int totalCombats, int totalTurns) {
        // You would just return getStatsDescription() if you don't want to display per-combat and per-turn stats
        StringBuilder builder = new StringBuilder();
        builder.append(getStatsDescription());
        float stat = (float)stats.get(GOLD_STAT);
        // Relic Stats truncates these extended stats to 3 decimal places, so we do the same
        DecimalFormat perTurnFormat = new DecimalFormat("#.###");
        builder.append(PER_TURN_STRING);
        builder.append(perTurnFormat.format(stat / Math.max(totalTurns, 1)));
        builder.append(PER_COMBAT_STRING);
        builder.append(perTurnFormat.format(stat / Math.max(totalCombats, 1)));
        return builder.toString();
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
        GoldenLeaf newRelic = new GoldenLeaf();
        newRelic.stats = this.stats;
        return newRelic;
    }
}
