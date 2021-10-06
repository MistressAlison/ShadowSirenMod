package ShadowSiren.relics;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import static ShadowSiren.ShadowSirenMod.makeRelicOutlinePath;
import static ShadowSiren.ShadowSirenMod.makeRelicPath;

public class FireFlower extends CustomRelic {

    // ID, images, text.
    public static final String ID = ShadowSirenMod.makeID("FireFlower");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("FireFlower.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("FireFlower.png"));

    private static final int BURN = 5;

    HashMap<String, Integer> stats = new HashMap<>();
    private final String MAX_HP_LOSS_STAT = DESCRIPTIONS[2];
    private final String HP_LOSS_STAT = DESCRIPTIONS[3];
    private final String PER_TURN_STRING = DESCRIPTIONS[5];
    private final String PER_COMBAT_STRING = DESCRIPTIONS[4];

    public FireFlower() {
        super(ID, IMG, OUTLINE, RelicTier.UNCOMMON, LandingSound.FLAT);
        resetStats();
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + BURN + DESCRIPTIONS[1];
    }

    @Override
    public void atTurnStart() {
        flash();
        this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        for (AbstractMonster aM : AbstractDungeon.getMonsters().monsters) {
            if(!aM.isDeadOrEscaped()) {
                int loss = aM.maxHealth > BURN ? BURN : aM.maxHealth - 1;
                int hpLoss = aM.maxHealth - loss >= aM.currentHealth ? 0 : aM.currentHealth - aM.maxHealth - loss;
                this.addToBot(new LoseHPAction(aM, AbstractDungeon.player, loss));
                stats.put(MAX_HP_LOSS_STAT, stats.get(MAX_HP_LOSS_STAT) + loss);
                stats.put(HP_LOSS_STAT, stats.get(HP_LOSS_STAT) + hpLoss);
            }
        }
    }

    @Override
    public AbstractRelic makeCopy() {
        // Relic Stats will always query the stats from the instance passed to BaseMod.addRelic()
        // Therefore, we make sure all copies share the same stats by copying the HashMap.
        FireFlower newRelic = new FireFlower();
        newRelic.stats = this.stats;
        return newRelic;
    }

    public String getStatsDescription() {
        return MAX_HP_LOSS_STAT + stats.get(MAX_HP_LOSS_STAT) + HP_LOSS_STAT + stats.get(HP_LOSS_STAT);
    }

    public String getExtendedStatsDescription(int totalCombats, int totalTurns) {
        StringBuilder builder = new StringBuilder();
        builder.append(getStatsDescription());
        float stat = (float)stats.get(MAX_HP_LOSS_STAT);
        // Relic Stats truncates these extended stats to 3 decimal places, so we do the same
        DecimalFormat perTurnFormat = new DecimalFormat("#.###");
        builder.append(PER_TURN_STRING);
        builder.append(perTurnFormat.format(stat / Math.max(totalTurns, 1)));
        builder.append(PER_COMBAT_STRING);
        builder.append(perTurnFormat.format(stat / Math.max(totalCombats, 1)));

        stat = (float)stats.get(HP_LOSS_STAT);
        builder.append(PER_TURN_STRING);
        builder.append(perTurnFormat.format(stat / Math.max(totalTurns, 1)));
        builder.append(PER_COMBAT_STRING);
        builder.append(perTurnFormat.format(stat / Math.max(totalCombats, 1)));
        return builder.toString();
    }

    public void resetStats() {
        stats.put(MAX_HP_LOSS_STAT, 0);
        stats.put(HP_LOSS_STAT, 0);
    }

    public JsonElement onSaveStats() {
        // An array makes more sense if you want to store more than one stat
        Gson gson = new Gson();
        ArrayList<Integer> statsToSave = new ArrayList<>();
        statsToSave.add(stats.get(MAX_HP_LOSS_STAT));
        statsToSave.add(stats.get(HP_LOSS_STAT));
        return gson.toJsonTree(statsToSave);
    }

    public void onLoadStats(JsonElement jsonElement) {
        if (jsonElement != null) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            stats.put(MAX_HP_LOSS_STAT, jsonArray.get(0).getAsInt());
            stats.put(HP_LOSS_STAT, jsonArray.get(1).getAsInt());
        } else {
            resetStats();
        }
    }
}
