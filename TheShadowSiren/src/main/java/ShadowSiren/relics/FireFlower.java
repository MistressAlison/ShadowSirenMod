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
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.ArrayList;
import java.util.HashMap;

import static ShadowSiren.ShadowSirenMod.makeRelicOutlinePath;
import static ShadowSiren.ShadowSirenMod.makeRelicPath;

public class FireFlower extends CustomRelic {

    // ID, images, text.
    public static final String ID = ShadowSirenMod.makeID("FireFlower");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("FireFlower.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("FireFlower.png"));

    private static final int BURN = 3;

    HashMap<String, Integer> stats = new HashMap<>();
    private final String BURN_STAT = DESCRIPTIONS[2];

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
    public void atBattleStart() {
        flash();
        this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        for (AbstractMonster aM : AbstractDungeon.getMonsters().monsters) {
            if(!aM.isDeadOrEscaped()) {
                this.addToBot(new ApplyPowerAction(aM, AbstractDungeon.player, new BurnPower(aM, AbstractDungeon.player, BURN)));
                stats.put(BURN_STAT, stats.get(BURN_STAT) + BURN);
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
        return BURN_STAT + stats.get(BURN_STAT);
    }

    public String getExtendedStatsDescription(int totalCombats, int totalTurns) {
        // You would just return getStatsDescription() if you don't want to display per-combat and per-turn stats
        return getStatsDescription();
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
